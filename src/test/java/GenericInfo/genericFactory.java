package GenericInfo;
import Context.DbConnection;
import Context.GlobalContext;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class genericFactory {
    private ArrayList<String[]> statusDetails = new ArrayList<>();

    public boolean isExistTable(String table) throws SQLException {
        boolean isExists = false;
        DatabaseMetaData dbm = GlobalContext.connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        if (tables.next()) {
            isExists = true;
        }
        return isExists;
    }

    public boolean checkServerConnection(String databaseName){
        boolean isConnected = false;
        String connectionUrl = DbConnection.connectionUrl +databaseName;
        try {
            // Load SQL Server JDBC driver and establish connection.
            System.out.print("Connecting to SQL Server ... ");
            GlobalContext.connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connected");
            GlobalContext.connectionStatus = "Success";
            isConnected = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            GlobalContext.connectionStatus = "Error";
        }
        return isConnected;
    }

    public boolean checkFile(String fileName) {
        File file = new File(fileName);
        boolean exists = file.exists();
        return exists;
    }

    public ArrayList<String[]> readDataFromFile(String fileName) {
        File file = new File(fileName);
        boolean exists = false;
        try {
            Scanner input = new Scanner(file);
            input.nextLine();
            while (input.hasNext()){
                String num = input.nextLine();
                String[] personData = num.split(",");
                GlobalContext.statusDetails.add(personData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       return GlobalContext.statusDetails;
    }

    public int getDataFromTableWithStatusUpdateId(String tableName) throws SQLException {
        String sql = "select count(*), Status from "+tableName+" where UpdateStatusID = "+ GlobalContext.UpdateStatusID+"Group By Status";
        PreparedStatement stmt = GlobalContext.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        GlobalContext.statusCheck = rs.getBoolean(1);
        int count = rs.getInt(1);
        return count;
    }

    public int getDataWithOrderIdAndBagNumber(String tableName) throws SQLException{
        GlobalContext.orderId = GlobalContext.statusDetails.get(0)[0].substring(1,8);
        GlobalContext.BagNumber = Integer.parseInt(GlobalContext.statusDetails.get(0)[20].substring(1,2));
        String sql = "select checked_by from "+tableName+" where batch = "+ GlobalContext.orderId + " and bag = "+GlobalContext.BagNumber+" and checked = 1 and is_result = 1";
        PreparedStatement stmt = GlobalContext.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int checkedBy = rs.getInt(1);
        return checkedBy;
    }

    public boolean getUpdateStatusIDCheckedXMLGeneratedAndCVSAcknowlogementDataFromTableWithOrderIdAndBagNumber(String tableName)  throws SQLException{
        boolean status = false;
        String sql = "select UpdateStatusID, Checked, XMLGenerated, CVSAcknowlogement from "+tableName+" where OrderID = '"+ GlobalContext.orderId+ "'and BagNumber ="+GlobalContext.BagNumber+" and CheckedBy = "+GlobalContext.checkedBy;
        PreparedStatement stmt = GlobalContext.connection.prepareStatement(sql);
        ResultSet response = stmt.executeQuery();
        while (response.next()) {
            GlobalContext.UpdateStatusID =Integer.parseInt(response.getString(1));
            GlobalContext.checked =Integer.parseInt(response.getString(2));
            GlobalContext.XMLGenerated = Integer.parseInt(response.getString(3));
            GlobalContext.CVSAcknowlogement = Integer.parseInt(response.getString(4));
            status = true;
            break;
        }
        return status;
    }

    public int GetOrphanData() throws SQLException {
        String sql = " select count(*) from [BacktalkDB].[dbo].[StatusUpdate] where DATEDIFF(DAY, CheckedDateTime, GETDATE()) > 14";
        PreparedStatement stmt = GlobalContext.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count;
    }
}
