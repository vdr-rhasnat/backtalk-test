package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class inspectRXStep {
    private ArrayList<String[]> statusDetails = new ArrayList<>();
    private int checked = 0;
    private int checkedBy = 0;
    private int XMLGenerated = 0;
    private int CVSAcknowlogement = 0;
    private Integer UpdateStatusID = 0;
    private String orderId = "";
    private Integer BagNumber = 0;
    private File file;

    @And("User should get data from {string} table with orderId and bagNumber")
    public void userShouldGetDataWithOrderIdAndBagNumber(String tableName) throws SQLException{
        orderId = statusDetails.get(0)[0].substring(1,8);
        BagNumber = Integer.parseInt(statusDetails.get(0)[20].substring(1,2));
        String sql = "select checked_by from "+tableName+" where batch = "+ orderId + " and bag = "+BagNumber+" and checked = 1 and is_result = 1";
        PreparedStatement stmt = purgeOrphanTestStep.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        checkedBy = rs.getInt(1);
        Assert.assertEquals("Checked Data With b_batch_bag Table",100,checkedBy);
    }

    @Given("ATP batch file {string}")
    public void atpBatchFile(String fileName) {
         file = new File(fileName);
        boolean exists = file.exists();
        Assert.assertTrue("The expected file does not exist", exists);
    }

    @Then("User should parse data from ATP batch file")
    public void userShouldParseDataFromAtpBatchFile() {
        boolean exists = false;
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
            if(i>0){
                exists = true;
            }else{
                exists = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue("There is no data in ATP batch file.",exists);

    }


    @And("User should get UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data from {string} table with orderId and BagNumber")
    public void userShouldGetUpdateStatusIDCheckedXMLGeneratedAndCVSAcknowlogementDataFromTableWithOrderIdAndBagNumber(String tableName)  throws SQLException {
        boolean status = false;
        String sql = "select UpdateStatusID, Checked, XMLGenerated, CVSAcknowlogement from "+tableName+" where OrderID = '"+ orderId+ "'and BagNumber ="+BagNumber+" and CheckedBy = "+checkedBy;
        PreparedStatement stmt = purgeOrphanTestStep.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            UpdateStatusID =Integer.parseInt(rs.getString(1));
            checked =Integer.parseInt(rs.getString(2));
            XMLGenerated = Integer.parseInt(rs.getString(3));
            CVSAcknowlogement = Integer.parseInt(rs.getString(4));
            status = true;
            break;
        }
        Assert.assertTrue("Checked Data With StatusUpDate Table",status);
    }

    @And("Checked should be {int}")
    public void checkedShouldBe(int Checked) {
        Assert.assertEquals(Checked,checked);
    }

    @And("XMLGenerated should be {int}")
    public void xmlgeneratedShouldBe(int xmlGenerated) {
        Assert.assertEquals(xmlGenerated,XMLGenerated);
    }

    @And("CVSAcknowlogement should be {int}")
    public void cvsacknowlogementShouldBe(int cvsAcknowlogement) {
        Assert.assertEquals(cvsAcknowlogement,CVSAcknowlogement);
    }

    @And("User should get data from {string} table with statusUpdateId and status true")
    public void userShouldGetDataFromTableWithStatusUpdateIdAndStatusTrue(String tableName) throws SQLException {
        String sql = "select count(*) from "+tableName+" where UpdateStatusID = "+ UpdateStatusID + " and Status = 'true'";
        PreparedStatement stmt = purgeOrphanTestStep.connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        Assert.assertEquals("Checked Data with PMSMessageLog Table",1, count);
    }
}
