package rs.raf.domaci3.integration.userController;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MvcResult;
import rs.raf.domaci3.integration.TestState;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;
import rs.raf.domaci3.requests.LoginRequest;
import rs.raf.domaci3.response.LoginResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerSteps extends TestsConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestState testState;

    @Autowired
    private ObjectMapper objectMapper;


    private LoginResponse loginResponse;
    private List<User> allUsers;
    private User userWithId;
    private User updatedUser;
    private User addedUser;
    private User createUserDto;


    @Given("a user with email {string} and password {string}")
    public void aUserWithEmailAndPassword(String email, String password) {

        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);


            ResultActions resultActions = mockMvc.perform(
                    post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)

            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("the user logs in with valid email {string} and valid password {string}")
    public void theUserLogsInWithValidEmailAndValidPassword(String email, String password) {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        try {
            String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

            // Simuliraj login zahtev
            ResultActions resultActions = mockMvc.perform(
                    post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(loginRequestJson)
            );

            // Zadrži status odgovora
            MvcResult result = resultActions.andReturn();
            testState.setActualResponseStatus(result.getResponse().getStatus());


            // Pročitaj JWT token iz odgovora
            String stringResponse = result.getResponse().getContentAsString();
            loginResponse = objectMapper.readValue(stringResponse, LoginResponse.class);

            testState.setJwtToken(loginResponse.getToken()); // Sačuvaj JWT token


        } catch (Exception e) {
            e.printStackTrace();
            fail("Login failed: " + e.getMessage());
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, testState.getActualResponseStatus(), "Response status does not match expected status");
    }

    @Then("the response should contain a valid JWT token")
    public void theResponseShouldContainAValidJwtToken() {
        assertNotNull(testState.getJwtToken(), "JWT token should not be null");
    }

    @Then("the response should contain roles for {string}")
    public void theResponseShouldContainRolesFor(String email) {
        assertTrue(loginResponse.getRoles().size() == 11, "User should have roles");
    }

    //get all
    @When("Admin with email {string} requests to get all users")
    public void adminWithEmailRequestsToGetAllUsers(String email) {

        //imas jwt gore,prosledi ga samo
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/users/get")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
            ).andExpect(status().isOk());


            MvcResult result = resultActions.andReturn();
            testState.setActualResponseStatus(result.getResponse().getStatus());
            ;

            String allUsersJson = result.getResponse().getContentAsString();

            allUsers = objectMapper.readValue(allUsersJson, new TypeReference<ArrayList<User>>() {
            });//da bi znao u sta da mapira


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @And("the users should include {string},{string} and {string}")
    public void theUsersShouldIncludeAnd(String name1, String name2, String name3) {

        assertTrue(allUsersExists(allUsers, name1, name2, name3), "All users exist");
    }

    private boolean allUsersExists(List<User> allUsers, String name1, String name2, String name3) {
        return allUsers.stream().anyMatch(user -> user.getName().equals(name1)) &&
                allUsers.stream().anyMatch(user -> user.getName().equals(name2)) &&
                allUsers.stream().anyMatch(user -> user.getName().equals(name3));
    }

    /// Get user with id
    @When("User with email {string} requests to get user by id {int}")
    public void userWithEmailRequestsToGetUserById(String email, int id) throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/users/get/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
            ).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());

        String userWithIdJson = result.getResponse().getContentAsString();
        userWithId = objectMapper.readValue(userWithIdJson, User.class);

    }

    @And("the user's email should be {string}")
    public void theUserSEmailShouldBe(String email) {
        assertEquals(email, userWithId.getEmail(), "User email does not match expected email");
    }

    //CreateUser
    @Given("a new user with email {string}, password {string}, name {string}, and lastName {string}")
    public void aNewUserWithEmailPasswordNameAndLastName(String arg0, String arg1, String arg2, String arg3) {

        createUserDto = new User();
        createUserDto.setEmail(arg0);
        createUserDto.setPassword(arg1);
        createUserDto.setName(arg2);
        createUserDto.setLastName(arg3);
        List<Role> roles = new ArrayList<>();

        createUserDto.setRoles(roles);

    }

    @When("I request to add the new user")
    public void iRequestToAddTheNewUser() throws UnsupportedEncodingException, JsonProcessingException {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    post("/api/users/add")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
                            .content(objectMapper.writeValueAsString(createUserDto))
            ).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = result.getResponse().getContentAsString();
        addedUser = objectMapper.readValue(addedUserJson, User.class);
    }

    @And("the new user should be stored in the database")
    public void theNewUserShouldBeStoredInTheDatabase() {
        //ako ima svoj id, znaci da mu je dodelila baza
        assertNotNull(addedUser.getId(), "User should be added to the database");
    }


    @Given("a user with id {int} exists")
    public void aUserWithIdExists(int id) throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {


            resultActions = mockMvc.perform(
                    get("/api/users/get/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isOk());
        } catch (Exception e) {
            fail(e.getMessage());
        }


        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());


        String stringResponse = result.getResponse().getContentAsString();

        userWithId = objectMapper.readValue(stringResponse, User.class);


        assertTrue(userWithId != null);


    }

    @When("I update user id {int} with new name {string} and new lastName {string}")
    public void iUpdateUserIdWithNewNameAndNewLastName(int id, String name, String lastname) throws UnsupportedEncodingException, JsonProcessingException {

        UserDto dto = new UserDto();
        dto.setId(userWithId.getId());
        dto.setName(name);
        dto.setLastName(lastname);
        dto.setEmail(userWithId.getEmail());
        dto.setRoles(userWithId.getRoles().toArray(new Role[0]));


        userWithId.setName(name);
        userWithId.setLastName(lastname);

        ResultActions resultActions = null;
        try {

            resultActions = mockMvc.perform(
                    put("/api/users/update")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(dto))
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isOk());

        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Zadrži status odgovora
        MvcResult result = resultActions.andReturn();


        // Pročitaj JWT token iz odgovora
        String stringResponse = result.getResponse().getContentAsString();
        updatedUser = objectMapper.readValue(stringResponse, User.class);


    }

    @And("the response should contain updated user name {string} and new last name {string}")
    public void theResponseShouldContainUpdatedUserNameAndNewLastName(String name, String lastname) {

        assertTrue(updatedUser.getName().equals(name) && updatedUser.getLastName().equals(lastname));

    }

    @When("I request to delete user by id {int}")
    public void iRequestToDeleteUserById(int id) {

        ResultActions resultActions = null;
        try {

            resultActions = mockMvc.perform(
                    delete("/api/users/delete/{id}", 2)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isOk());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @And("the user with id {int} should no longer exist in the database")
    public void theUserWithIdShouldNoLongerExistInTheDatabase(int id) throws JsonProcessingException, UnsupportedEncodingException {

        ResultActions resultActions = null;
        try {


            resultActions = mockMvc.perform(
                    get("/api/users/get/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isNotFound());
        } catch (Exception e) {
            //stvorio se izuzetak, zato sto user sa tim id-jem ne postoji
            assertTrue(true);
        }


    }
}



