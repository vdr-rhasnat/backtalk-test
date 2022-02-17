Feature: InspectRX Database Test
  Scenario: Data Read From Text file And Check With InspectRXDB
    Given ATP batch file "c:/Innovation/Input/Batch8069845.txt"
    Then User should parse data from ATP batch file
    Given Sql server connection for database "dispensercheck"
    Then User should get connection status "Success"
    And Table "b_batch_bag" should exists
    And User should get data from "b_batch_bag" table with orderId and bagNumber
    Given Sql server connection for database "BacktalkDB"
    Then User should get connection status "Success"
    And Table "StatusUpdate" should exists
    And Table "PMSMessageLog" should exists
    And User should get UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data from "StatusUpdate" table with orderId and BagNumber
    And Checked should be 1
    And XMLGenerated should be 1
    And CVSAcknowlogement should be 1
    And User should get data from "PMSMessageLog" table with statusUpdateId and status true