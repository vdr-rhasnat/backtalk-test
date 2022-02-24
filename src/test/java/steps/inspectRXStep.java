package steps;

import Context.GlobalContext;
import GenericInfo.genericFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class inspectRXStep {
    private genericFactory gen = new genericFactory();

    @Given("ATP batch file {string}")
    public void atpBatchFile(String file) {
        GlobalContext.fileName = file;
        boolean checkFile =  gen.checkFile(GlobalContext.fileName);
        Assert.assertTrue("The expected file does not exist", checkFile);
    }

    @Then("user should parse data from ATP batch file")
    public void userShouldParseDataFromAtpBatchFile() {
        boolean checkData = false;
        GlobalContext.statusDetails = gen.readDataFromFile(GlobalContext.fileName);
        if(GlobalContext.statusDetails.size()>0){
            checkData = true;
        }
        Assert.assertTrue("There is no data in ATP batch file.",checkData);
    }

    @And("user should get data from {string} table with orderId and bagNumber")
    public void userShouldGetDataWithOrderIdAndBagNumber(String tableName) throws SQLException{
        GlobalContext.checkedBy = gen.getDataWithOrderIdAndBagNumber(tableName);
        Assert.assertEquals("Checked Data With b_batch_bag Table",100,GlobalContext.checkedBy);
    }

    @When("user get UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data from {string} table with orderId and BagNumber")
    public void userGetUpdateStatusIDCheckedXMLGeneratedAndCVSAcknowlogementDataFromTableWithOrderIdAndBagNumber(String tableName)  throws SQLException {
        boolean status = gen.getUpdateStatusIDCheckedXMLGeneratedAndCVSAcknowlogementDataFromTableWithOrderIdAndBagNumber(tableName);
        Assert.assertTrue("Checked Data With StatusUpDate Table",status);
    }

    @Then("Checked should be {int}")
    public void checkedShouldBe(int Checked) {
        Assert.assertEquals(Checked,GlobalContext.checked);
    }

    @And("XMLGenerated should be {int}")
    public void xmlgeneratedShouldBe(int xmlGenerated) {
        Assert.assertEquals(xmlGenerated,GlobalContext.XMLGenerated);
    }

    @And("CVSAcknowlogement should be {int}")
    public void cvsacknowlogementShouldBe(int cvsAcknowlogement) {
        Assert.assertEquals(cvsAcknowlogement,GlobalContext.CVSAcknowlogement);
    }

    @And("user should get data from {string} table with statusUpdateId and status true")
    public void userShouldGetDataFromTableWithStatusUpdateIdAndStatusTrue(String tableName) throws SQLException {
        int count = gen.getDataFromTableWithStatusUpdateIdAndStatusTrue(tableName);
        Assert.assertEquals("Checked Data with PMSMessageLog Table",1, count);
    }
}
