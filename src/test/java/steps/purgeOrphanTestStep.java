package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import javax.xml.transform.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class purgeOrphanTestStep {
    private Connection connection;
    private String connectionStatus;
    private ArrayList<String[]> statusDetails = new ArrayList<>();
    private Integer checked = 0;
    private int checkedBy = 0;
    private Integer XMLGenerated = 0;
    private Integer CVSAcknowlogement = 0;
    private Integer UpdateStatusID = 0;
    private String orderId = "";
    private Integer BagNumber = 0;
    @Given("Sql server connection")
    public void sqlServerConnection() {
        String connectionUrl = "jdbc:sqlserver://localhost;databaseName=BacktalkDB;user=shawon;password=shawon963";
        try {
            // Load SQL Server JDBC driver and establish connection.
            System.out.print("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connected");
            connectionStatus = "Success";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connectionStatus = "Error";
        }
    }

    @Then("I should get connection status {string}")
    public void iShouldGetConnectionStatus(String status) {
        Assert.assertEquals(status, connectionStatus);
    }

    @And("Table {string} should exists")
    public void tableShouldExists(String table) throws SQLException {
        boolean isTableExists;
        DatabaseMetaData dbm = connection.getMetaData();
        ResultSet tables = dbm.getTables(null, null, table, null);
        if (tables.next()) {
            isTableExists = true;
        }
        else {
            isTableExists = false;
        }
        Assert.assertTrue(isTableExists);
    }

    @And("I should not get orphan data")
    public void iShouldNotGetOrphanData() throws SQLException {
        String sql = " select count(*) from [BacktalkDB].[dbo].[StatusUpdate] where DATEDIFF(DAY, CheckedDateTime, GETDATE()) < 14";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        Assert.assertEquals("Number of records in StatusUpDate Table", 7, count);
    }

    @Then("I should read data from {string} batch file")
    public void iShouldReadDataFromBatchFile(String fileName) {
        File file = new File(fileName);
        try {
            Scanner input = new Scanner(file);
            int i=0;
            while (input.hasNext()){
                input.nextLine();                         //do this to skip the first line (header)while (input.hasNext()) {
                String num = input.nextLine();
                String[] personData = num.split(","); //returns the array of strings computed by splitting this string around matches of the given delimiter
                statusDetails.add(personData);
                i++;
            }
            System.out.println(statusDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @And("I should check data with {string} table")
    public void iShouldCheckWithStatusUpdateTable(String tableName) throws SQLException {
        boolean status = false;
        String sql = "select UpdateStatusID, Checked, XMLGenerated, CVSAcknowlogement from "+tableName+" where OrderID = '"+ orderId+ "'and BagNumber ="+BagNumber+" and CheckedBy = "+checkedBy;
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            UpdateStatusID =Integer.parseInt(rs.getString(1));
            checked =Integer.parseInt(rs.getString(2));
            XMLGenerated = Integer.parseInt(rs.getString(3));
            CVSAcknowlogement = Integer.parseInt(rs.getString(4));
            break;
        }
        if((checked == 1) && (XMLGenerated == 1) && (CVSAcknowlogement == 1)){
            status = true;
        }
        else {
            status = false;
        }
        Assert.assertTrue("Checked Data With StatusUpDate Table",status);
    }

    @And("I should check {string} table data")
    public void iShouldCheckTableData(String tableName) throws SQLException {
        String sql = "select count(*) from "+tableName+" where UpdateStatusID = "+ UpdateStatusID + " and Status = 'true'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        Assert.assertEquals("Checked Data with PMSMessageLog Table",1, count);
    }

    @Given("Sql server connection with inspectRxDB")
    public void sqlServerConnectionWithinspectRxDB() {
        String connectionUrl = "jdbc:sqlserver://localhost;databaseName=dispensercheck;user=shawon;password=shawon963";
        try {
            // Load SQL Server JDBC driver and establish connection.
            System.out.print("Connecting to SQL Server ... ");
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connected");
            connectionStatus = "Success";

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connectionStatus = "Error";
        }
    }

    @Then("I should get data {string} table with orderId and bagNumber")
    public void iShouldGetDataWithOrderIdAndBagNumber(String tableName) throws SQLException{
        orderId = statusDetails.get(0)[0].substring(1,8);
        BagNumber = Integer.parseInt(statusDetails.get(0)[20].substring(1,2));
        String sql = "select checked_by from "+tableName+" where batch = "+ orderId + " and bag = "+BagNumber+" and checked = 1 and is_result = 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        checkedBy = rs.getInt(1);
        Assert.assertEquals("Checked Data With b_batch_bag Table",100,checkedBy);
    }
}
