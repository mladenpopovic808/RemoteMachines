Feature: Role Management

  Scenario: Get All Roles
    Given the user logs in with valid email "admin@raf.rs" and valid password "123"
    When admin requests all roles
    Then the response status should be 200
    And List size should be 11
