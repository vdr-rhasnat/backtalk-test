Feature: Purge Orphan Data Test
  Scenario: Backtalk Test With Purge Orphan Data
    Given Sql server connection
    Then I should get connection status "Success"
    And Table "StatusUpdate" should exists
    And I should get purge orphan data

  Scenario: Backtalk Test Without Purge Orphan Data
    Given Sql server connection
    Then I should get connection status "Success"
    And Table "StatusUpdate" should exists
    And I shouldn't get purge orphan data

