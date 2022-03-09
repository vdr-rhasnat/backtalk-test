Feature: InspectRX Database Change Test
  Scenario: Reading data from ATP batch file
    Given ATP batch file "c:/Innovation/Input/Batch8069845.txt"
    And batch file should contain data

  Scenario: Check existence of b_batch_bag table with data in dispensercheck database
    Given sql server connection for database "dispensercheck"
    Then table "b_batch_bag" should exists
    And user should get data from "b_batch_bag" table with orderId and bagNumber

  Scenario: Check existence of StatusUpdate and PMSMessagelog tables in BacktalkDB database
    Given sql server connection for database "BacktalkDB"
    Then table "StatusUpdate" should exists
    And table "PMSMessageLog" should exists

  Scenario: Check status of XMLGenerated and Checked fields in StatusUpdate table
    When user get UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data from "StatusUpdate" table with orderId and BagNumber
    Then Checked field value should be 1
    And XMLGenerated field value should be 1

  Scenario: Check PMS status in PMSMessagelog table
    When user get PMS status from "PMSMessageLog" table with statusUpdateId
    Then CVSAcknowlogement field value is 0
    And Status field value shoudld be 0

