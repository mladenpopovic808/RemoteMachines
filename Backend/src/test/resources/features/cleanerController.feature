Feature: Cleaner Controller management

  Scenario: Admin Login and Create Cleaner
    Given the user logs in with valid email "admin@raf.rs" and valid password "123"
    When I request to add the new cleaner with name "Cleaner5" and email "admin@raf.rs"
    Then the response status should be 200
    And the new cleaner should be stored in the database

  Scenario: Get Cleaner by ID
    Given a cleaner with ID 1 exists
    When admin requests to get cleaner by id 1
    Then the response status should be 200
    And the cleaner's email should be "admin@raf.rs"

  Scenario: Delete Cleaner
    Given a cleaner with ID 1 exists
    When I request to delete cleaner by id 1
    Then the response status should be 200
    And the cleaner with id 1 has "false"  active status

  Scenario: Start Cleaner
    Given a cleaner with ID 3 exists
    When I request to start cleaner by id 3
    Then the response status should be 202
    And wait for 5 seconds for the cleaner
    And the cleaner with id 3 has "RUNNING" status

  Scenario: Stop Cleaner
    Given the cleaner with id 3 has "RUNNING" status
    When I request to stop cleaner by id 3
    Then the response status should be 202
    And wait for 5 seconds for the cleaner
    And the cleaner with id 3 has "STOPPED" status

  Scenario: Discharge Cleaner
    Given a cleaner with ID 3 exists
    When I request to discharge cleaner by id 3
    Then the response status should be 202

  Scenario: Schedule Cleaner
    Given a cleaner with ID 2 exists
    When I request to schedule cleaner with ID "2" for "2024-10-31" at "10:00 AM" for "start" action
    Then the response status should be 202

  Scenario: Get All Cleaners by User
    Given a user with email "admin@raf.rs" has assigned cleaners
    When I request to get all cleaners by user email "admin@raf.rs"
    Then the response status should be 200
    And the response should include the assigned cleaners


