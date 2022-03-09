package steps;

import Context.GlobalContext;
import GenericInfo.genericFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.Assume;

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

    @And("batch file should contain data")
    public void batchFileShouldContainData() {
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

    @Then("Checked field value should be {int}")
    public void checkedFieldValueShouldBe(int Checked) {
        Assert.assertEquals(Checked,GlobalContext.checked);
    }

    @And("XMLGenerated field value should be {int}")
    public void xmlgeneratedFieldValueShouldBe(int xmlGenerated) {
        Assert.assertEquals(xmlGenerated,GlobalContext.XMLGenerated);
    }

    @And("user get PMS status from {string} table with statusUpdateId")
    public void userShouldGetDataFromTableWithStatusUpdateId(String tableName) throws SQLException {
        int count =  gen.getDataFromTableWithStatusUpdateId(tableName);
        Assert.assertEquals(2,count);
    }

    @And("CVSAcknowlogement field value is {int}")
    public void checkCvsacknowlogementFieldValue(int cvsAcknowledgementValue) {
//        int cvsAcknowlogement = 0;
//        if(GlobalContext.statusCheck){
//            cvsAcknowlogement = 1;
//        }
        Assert.assertEquals(cvsAcknowledgementValue, GlobalContext.CVSAcknowlogement);
    }
    @And("Status field value shoudld be {int}")
    public void checkStatusFieldValue(int PmsStatus) {
//        int cvsAcknowlogement = 0;
        if(GlobalContext.statusCheck){
            PmsStatus = 1;
        }
        Assert.assertEquals(PmsStatus, GlobalContext.CVSAcknowlogement);
    }
}
