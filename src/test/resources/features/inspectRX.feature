Feature: InspectRX and BacktalkDB Database Change Test
  Scenario: Reading data from ATP batch file
    Given ATP batch file "c:/Innovation/Input/Batch8069845.txt"
    Then batch file should contain ATP exported data

  Scenario: Check existence of b_batch_bag table with data in dispensercheck database
    Given sql server connection for database "dispensercheck"
    Then table "b_batch_bag" should exists
    And "b_batch_bag" table should contain b_batch_bag data with orderId and bagNumber

  Scenario: Check existence of StatusUpdate and PMSMessagelog tables in BacktalkDB database
    Given sql server connection for database "BacktalkDB"
    Then table "StatusUpdate" should exists
    And table "PMSMessageLog" should exists

  Scenario: Check status of XMLGenerated and Checked fields in StatusUpdate table
    When "StatusUpdate" table should contain UpdateStatusID, Checked, XMLGenerated and CVSAcknowlogement data  with orderId and BagNumber
    Then Checked field value should be 1
    And XMLGenerated field value should be 1

  Scenario: Check PMS status in PMSMessagelog table
    When "PMSMessageLog" table should contain PMS status with statusUpdateId
    Then CVSAcknowlogement field value should be 0 or 1
    And Status field value should be equals to CVSAcknowlogement field value

