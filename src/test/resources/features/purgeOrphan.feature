Feature: Purged Orphan Data Test
  Scenario: "StatusUpdate" table should not contain data before 14 days from current date
    Given Sql server connection
    Then I should get connection status "Success"
    And Table "StatusUpdate" should exists
    And I should not get orphan data

  Scenario: Data Read From Text file And Check With InspectRxDB and BacktalkDB
    Given I should read data from "c:/Innovation/Input/Batch8069845.txt" batch file
    Given Sql server connection with inspectRxDB
    Then I should get connection status "Success"
    And Table "b_batch_bag" should exists
    Then I should get data "b_batch_bag" table with orderId and bagNumber
    Given Sql server connection
    Then I should get connection status "Success"
    And Table "StatusUpdate" should exists
    And I should check data with "StatusUpdate" table
    And Table "PMSMessageLog" should exists
    And I should check "PMSMessageLog" table data



