Feature: Purged Orphan Data Test
  Scenario: "StatusUpdate" table should not contain data before 14 days from current date
    Given sql server connection for database "BacktalkDB"
    When user get connection status "Success"
    Then table "StatusUpdate" should exists
    And user should get orphan data





