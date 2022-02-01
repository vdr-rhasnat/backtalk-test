Feature: Purged Orphan Data Test
  Scenario: "StatusUpdate" table should not contain data before 14 days from current date
    Given Sql server connection
    Then I should get connection status "Success"
    And Table "StatusUpdate" should exists
    And I should not get orphan data

