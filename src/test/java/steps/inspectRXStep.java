package steps;
import Context.GlobalContext;
import GenericInfo.genericFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.sql.*;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertThat;

public class inspectRXStep {
    private genericFactory gen = new genericFactory();

    @Given("ATP batch file {string}")
    public void atpBatchFile(String file) {
        GlobalContext.fileName = file;
        boolean checkFile =  gen.checkFile(GlobalContext.fileName);
        Assert.assertTrue("The expected file does not exist", checkFile);
    }

    @Then("batch file should contain ATP exported data")
    public void batchFileShouldContainATPExportedData() {
        boolean checkData = false;
        GlobalContext.statusDetails = gen.readDataFromFile(GlobalContext.fileName);
        if(GlobalContext.statusDetails.size()>0){
            checkData = true;
        }
        Assert.assertTrue("There is no data in ATP batch file.",checkData);
    }

    @And("{string} table should contain b_batch_bag data with orderId and bagNumber")
    public void tableShouldContainb_batch_bagDataWithOrderIdAndBagNumber(String tableName) throws SQLException{
        GlobalContext.checkedBy = gen.getDataWithOrderIdAndBagNumber(tableName);
        Assert.assertEquals("Checked Data With b_batch_bag Table",100,GlobalContext.checkedBy);
    }

    @When("Checked, XMLGenerated fields data get updated in {string} table with orderId and BagNumber")
    public void checkedAndXMLGeneratedFieldsDataUpdatedInTableWithOrderIdAndBagNumber(String tableName)  throws SQLException {
        boolean status = gen.getUpdatedataFromTableWithOrderIdAndBagNumber(tableName);
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

    @And("status fields data get updated in {string} table with statusUpdateId")
    public void pmsMessageLogTableShouldContainPMSStatusWithStatusUpdateId(String tableName) throws SQLException {
        int count =  gen.getDataFromTableWithStatusUpdateId(tableName);
        Assert.assertEquals(1,count);
    }

    @And("Status field value should be {int} or {int}")
    public void checkStatusFieldValueShouldZeroOrOne(int firstStatus, int secondStatus) {
        int status = 0;
        if(GlobalContext.statusCheck){
            status = 1;
        }
        assertThat(status, isOneOf(firstStatus, secondStatus));
    }

//    @And("CVSAcknowlogement field value should be equals to Status field value")
//    public void checkCVSAcknowlogementFieldValueShouldBeEqualToStatusFieldValue() {
//        int PmsStatus =0;
//        if(GlobalContext.statusCheck){
//            PmsStatus = 1;
//        }
//        Assert.assertEquals(PmsStatus, GlobalContext.CVSAcknowlogement);
//    }
//
//    @And("CVSAcknowlogement fields data get updated in {string} table with statusUpdateId")
//    public boolean cvsacknowlogementFieldsDataGetUpdatedInTableWithStatusUpdateId(String tableName) throws SQLException{
//        boolean status = false;
//        String sql = "select CVSAcknowlogement from "+tableName+" where UpdateStatusID = '"+ GlobalContext.UpdateStatusID;
//        PreparedStatement stmt = GlobalContext.connection.prepareStatement(sql);
//        ResultSet response = stmt.executeQuery();
//        while (response.next()) {
//            GlobalContext.CVSAcknowlogement = Integer.parseInt(response.getString(1));
//            status = true;
//            break;
//        }
//        return status;
//    }
}
