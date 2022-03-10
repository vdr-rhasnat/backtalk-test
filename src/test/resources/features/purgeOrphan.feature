Feature: Purged Orphan Data Test
  Scenario: "StatusUpdate" table should not contain data before 14 days from current date
    Given sql server connection for database "BacktalkDB"
    Then table "StatusUpdate" should exists
    And "StatusUpdate" table should not contain orphan data





