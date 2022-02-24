package steps;

import Context.DbConnection;
import Context.GlobalContext;
import GenericInfo.genericFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.sql.*;

public class purgeOrphanTestStep {
    private genericFactory gen = new genericFactory();

    @Given("sql server connection for database {string}")
    public void sqlServerConnectionForDatabase(String databaseName) {
      boolean isActive = gen.checkServerConnection(databaseName);
      Assert.assertTrue("Sql Server Connection Failed", isActive);
    }

    @When("user get connection status {string}")
    public void userGetConnectionStatus(String status) {
        Assert.assertEquals(status, GlobalContext.connectionStatus);
    }

    @Then("table {string} should exists")
    public void tableShouldExists(String table) throws SQLException {
        boolean isTableExists = gen.isExistTable(table);
        Assert.assertTrue(isTableExists);
    }

    @And("user should get orphan data")
    public void userShouldGetOrphanData() throws SQLException {
        int count = gen.GetOrphanData();
        Assert.assertEquals("Number of records in StatusUpDate Table", 7, count);
    }
}
