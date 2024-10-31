Feature: User Management

  Scenario: User Login
    Given a user with email "admin@raf.rs" and password "123"
    When the user logs in with valid email "admin@raf.rs" and valid password "123"
    Then the response status should be 200
    And the response should contain a valid JWT token
    And the response should contain roles for "admin@raf.rs"

  Scenario: Get All Users
    When Admin with email "admin@raf.rs" requests to get all users
    Then the response status should be 200
    And the users should include "Admin","User1" and "User2"

  Scenario: Get User by ID
    When User with email "admin@raf.rs" requests to get user by id 2
    Then the response status should be 200
    And the user's email should be "user1@raf.rs"

  Scenario: Create User
    Given a new user with email "newUser@raf.rs", password "newpassword", name "New", and lastName "User"
    When I request to add the new user
    Then the response status should be 200
    And the new user should be stored in the database

  Scenario: Update User
    Given a user with id 1 exists
    When I update user id 1 with new name "Updated User" and new lastName "Updated"
    Then the response status should be 200
    And the response should contain updated user details
    And the user's name should be "Updated User"

  Scenario: Delete User
    Given a user with id 1 exists
    When I request to delete user by id 1
    Then the response status should be 200
    And the user with id 1 should no longer exist in the database
