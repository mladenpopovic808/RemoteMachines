package rs.raf.domaci3.integration.userController;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.MvcResult;
import rs.raf.domaci3.model.Role;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.dto.UserDto;
import rs.raf.domaci3.requests.LoginRequest;
import rs.raf.domaci3.response.LoginResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerSteps extends UserControllerTestsConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserControllerState userControllerState;

    @Autowired
    private ObjectMapper objectMapper;


    private int actualResponseStatus; // Za čuvanje statusa odgovora
    private LoginResponse loginResponse;
    private List<User>allUsers;
    private User userWithId;
    private User addedUser;
    private User createUserDto;


    @Given("a user with email {string} and password {string}")
    public void aUserWithEmailAndPassword(String email, String password) {

        try{
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
            actualResponseStatus = result.getResponse().getStatus();

            // Pročitaj JWT token iz odgovora
            String stringResponse = result.getResponse().getContentAsString();
            loginResponse = objectMapper.readValue(stringResponse, LoginResponse.class);

            userControllerState.setJwtToken(loginResponse.getToken()); // Sačuvaj JWT token



        } catch (Exception e) {
            e.printStackTrace();
        fail("Login failed: "+e.getMessage());
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, actualResponseStatus, "Response status does not match expected status");
    }

    @Then("the response should contain a valid JWT token")
    public void theResponseShouldContainAValidJwtToken() {
        assertNotNull(userControllerState.getJwtToken(), "JWT token should not be null");
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
                            .header("Authorization", "Bearer " + userControllerState.getJwtToken())
            ).andExpect(status().isOk());


            MvcResult result = resultActions.andReturn();
            actualResponseStatus = result.getResponse().getStatus();
            System.out.println("STATUS JE " + actualResponseStatus);
            String allUsersJson = result.getResponse().getContentAsString();

            allUsers = objectMapper.readValue(allUsersJson, new TypeReference<ArrayList<User>>() {});//da bi znao u sta da mapira


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

    ///Get user with id
    @When("User with email {string} requests to get user by id {int}")
    public void userWithEmailRequestsToGetUserById(String email, int id) throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {
             resultActions = mockMvc.perform(
                    get("/api/users/get/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerState.getJwtToken())
            ).andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        actualResponseStatus = result.getResponse().getStatus();

        String userWithIdJson = result.getResponse().getContentAsString();
        userWithId = objectMapper.readValue(userWithIdJson, User.class);

    }

    @And("the user's email should be {string}")
    public void theUserSEmailShouldBe(String email) {
        assertEquals(email, userWithId.getEmail(), "User email does not match expected email");
    }

    //CreateUser
    @Given("a new user with email {string}, password {string}, name {string}, and lastName {string}")
    public void aNewUserWithEmailPasswordNameAndLastName(String arg0, String arg1, String arg2, String arg3)  {

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
                            .header("Authorization", "Bearer " + userControllerState.getJwtToken())
                            .content(objectMapper.writeValueAsString(createUserDto))
            ).andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        actualResponseStatus = result.getResponse().getStatus();
        String addedUserJson = result.getResponse().getContentAsString();
        addedUser = objectMapper.readValue(addedUserJson, User.class);
    }

    @And("the new user should be stored in the database")
    public void theNewUserShouldBeStoredInTheDatabase() {
        assertNotNull(addedUser, "User should be added to the database");
    }

















}


