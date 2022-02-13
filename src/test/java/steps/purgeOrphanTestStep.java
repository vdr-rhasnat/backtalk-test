package steps;

import DbContext.DbConnection;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.sql.*;

public class purgeOrphanTestStep {
    public static Connection connection;
    public static String connectionStatus;

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

    @Given("Sql server connection for database {string}")
    public void sqlServerConnectionForDatabase(String databaseName) {
        String connectionUrl = DbConnection.connectionUrl +databaseName;
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
}
