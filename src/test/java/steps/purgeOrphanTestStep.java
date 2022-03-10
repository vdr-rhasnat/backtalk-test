package steps;
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

    @Then("table {string} should exists")
    public void tableShouldExists(String table) throws SQLException {
        boolean isTableExists = gen.tableIsExist(table);
        Assert.assertTrue(isTableExists);
    }

    @And("{string} table should not contain orphan data")
    public void statusUpdateTableShouldNotContainOrphanData(String arg0) throws SQLException {
        int count = gen.orphanDataIsNotExist();
        Assert.assertEquals("Number of records in StatusUpDate Table", 7, count);
    }
}
