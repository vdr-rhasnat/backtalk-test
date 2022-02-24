Feature: InspectRX Database Change Test
  Scenario: Mimic InspectRX DB Changes
    Given ATP batch file "c:/Innovation/Input/Batch8069845.txt"
    Then user should parse data from ATP batch file
    Given sql server connection for database "dispensercheck"
    When user get connection status "Success"
    Then table "b_batch_bag" should exists
    And user should get data from "b_batch_bag" table with orderId and bagNumber
    Given sql server connection for database "BacktalkDB"
    When user get connection status "Success"
    Then table "StatusUpdate" should exists
    And table "PMSMessageLog" should exists
    When user get UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data from "StatusUpdate" table with orderId and BagNumber
    Then Checked should be 1
    And XMLGenerated should be 1
    And CVSAcknowlogement should be 1
    And user should get data from "PMSMessageLog" table with statusUpdateId and status true