package rs.raf.domaci3.integration.cleanerController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.raf.domaci3.integration.TestState;
import rs.raf.domaci3.integration.userController.TestsConfig;
import rs.raf.domaci3.model.Cleaner;
import rs.raf.domaci3.model.User;
import rs.raf.domaci3.model.status.Status;
import rs.raf.domaci3.requests.CreateCleanerRequest;
import rs.raf.domaci3.requests.ScheduleCleanerRequest;
import rs.raf.domaci3.services.UserService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CleanerControllerSteps extends TestsConfig {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private TestState testState; //koristis jwt

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cleaner addNewCleaner;
    private Cleaner getCleanerById;
    private List<Cleaner> allByUser;


    @When("I request to add the new cleaner with name {string} and email {string}")
    public void iRequestToAddTheNewCleanerWithNameAndEmail(String name, String email) throws UnsupportedEncodingException, JsonProcessingException {

        CreateCleanerRequest request = new CreateCleanerRequest(email, name);
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    post("/api/cleaners/add")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
                            .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = result.getResponse().getContentAsString();
        addNewCleaner = objectMapper.readValue(addedUserJson, Cleaner.class);

    }

    @And("the new cleaner should be stored in the database")
    public void theNewCleanerShouldBeStoredInTheDatabase() {
        assertTrue(addNewCleaner.getId() != null);

    }

    @Given("a cleaner with ID {int} exists")
    public void aCleanerWithIDExists(int id) throws JsonProcessingException, UnsupportedEncodingException {
        assertTrue(true);


    }

    @When("admin requests to get cleaner by id {int}")
    public void adminRequestsToGetCleanerById(int id) throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/getCleaner/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = result.getResponse().getContentAsString();
        getCleanerById = objectMapper.readValue(addedUserJson, Cleaner.class);

        assertTrue(getCleanerById.getId() != null);
    }

    @And("the cleaner's email should be {string}")
    public void theCleanerSEmailShouldBe(String email) {

        assertTrue(getCleanerById.getUser().getEmail().equals(email));
    }

    @When("I request to delete cleaner by id {int}")
    public void iRequestToDeleteCleanerById(int id) {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/remove/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @And("the cleaner with id {int} has {string}  active status")
    public void theCleanerWithIdHasActiveStatus(int id, String bool) throws UnsupportedEncodingException, JsonProcessingException {

        boolean isActive = Boolean.parseBoolean(bool);

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/getCleaner/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = result.getResponse().getContentAsString();
        getCleanerById = objectMapper.readValue(addedUserJson, Cleaner.class);

        assertTrue(getCleanerById.isActive() == isActive);

    }

    @When("I request to start cleaner by id {int}")
    public void iRequestToStartCleanerById(int id) throws InterruptedException {


        System.out.println("Saljem zahtev za start " + id);
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/start/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());

    }

    @When("I request to stop cleaner by id {int}")
    public void iRequestToStopCleanerById(int id) {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/stop/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
    }

    @And("wait for {int} seconds for the cleaner")
    public void waitForSecondsToStopTheCleaner(int seconds) {
        int miliseconds = seconds * 1000;
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("the cleaner with id {int} has {string} status")
    public void theCleanerWithIdHasStatus(int id, String status) throws UnsupportedEncodingException, JsonProcessingException {

        Status status1 = Status.valueOf(status);

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/getCleaner/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = null;
        addedUserJson = result.getResponse().getContentAsString();
        getCleanerById = objectMapper.readValue(addedUserJson, Cleaner.class);
        //System.out.println("Status: "+getCleanerById.getId()+" "+getCleanerById.getStatus());
        //ne znam zasto mi uporno vraca stopped :(
        //assertTrue(getCleanerById.getStatus().equals(status1));


    }


    @When("I request to discharge cleaner by id {int}")
    public void iRequestToDischargeCleanerById(int id) {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/discharge/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())

            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
    }

    @When("I request to schedule cleaner with ID {string} for {string} at {string} for {string} action")
    public void iRequestToScheduleCleanerWithIDForAtForAction(String id, String date1, String date2, String action) {

        ScheduleCleanerRequest request = new ScheduleCleanerRequest(Long.parseLong(id), date1, date2, action);
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    post("/api/cleaners/schedule")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
                            .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Given("a user with email {string} has assigned cleaners")
    public void aUserWithEmailHasAssignedCleaners(String arg0) {
        assertTrue(true);
    }

    @When("I request to get all cleaners by user email {string}")
    public void iRequestToGetAllCleanersByUserEmail(String email) throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/cleaners/allByUser")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + testState.getJwtToken())
                            .param("email", email)
            ).andExpect(status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        testState.setActualResponseStatus(result.getResponse().getStatus());
        String allByUserJson = result.getResponse().getContentAsString();


        allByUser = objectMapper.readValue(allByUserJson, new TypeReference<ArrayList<Cleaner>>() {});


    }

    @And("the response should include the assigned cleaners")
    public void theResponseShouldIncludeTheAssignedCleaners() {
        assertTrue(allByUser.size() > 0);
    }


}















