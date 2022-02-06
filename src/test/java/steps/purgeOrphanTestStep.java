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

    @Given("Sql server connection")
    public void sqlServerConnection() {
        String connectionUrl = "jdbc:sqlserver://localhost;databaseName=BacktalkDB;user=shawon;password=sa1234";
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
        String sql = " select count(*) from [BacktalkDB].[dbo].[StatusUpdate] where DATEDIFF(DAY, CheckedDateTime, GETDATE()) > 14";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        Assert.assertEquals("Number of records", 0, count);
    }

    @And("I should read data from {string} text file")
    public void iShouldReadDataFromTextFile(String fileName) {
//        String content = null;
//        File file = new File(fileName); // For example, foo.txt
//        FileReader reader = null;
//        try {
//            reader = new FileReader(file);
//            char[] chars = new char[(int) file.length()];
//            reader.read(chars);
//
//            content = new String(chars);
//            reader.close();
        File file = new File(fileName);
        ArrayList<String[]> arrayOfPeople = new ArrayList<>();

        try {
            Scanner input = new Scanner(file);
            int i=0;
            while (input.hasNext()){
                input.nextLine();                         //do this to skip the first line (header)while (input.hasNext()) {
                String num = input.nextLine();
                String[] personData = num.split(","); //returns the array of strings computed by splitting this string around matches of the given delimiter
                arrayOfPeople.add(personData);

                for(int j=0; j<arrayOfPeople.get(i).length; j++){
                    System.out.print("Column :"+j+"= " +arrayOfPeople.get(i)[j]+ " ");
                }

              System.out.print("\n===============\n");
                i++;
            }
//            for(int i=0; i<arrayOfPeople.size(); i++){
//                System.out.println(arrayOfPeople.get(i));
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
