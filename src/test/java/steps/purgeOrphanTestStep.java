package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import javax.xml.transform.Result;
import java.sql.*;

public class purgeOrphanTestStep {
    private Connection connection;
    private String connectionStatus;

    @Given("Sql server connection")
    public void sqlServerConnection() {
        String connectionUrl = "jdbc:sqlserver://localhost;databaseName=BacktalkDB;user=sa;password=sa123";
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
}
