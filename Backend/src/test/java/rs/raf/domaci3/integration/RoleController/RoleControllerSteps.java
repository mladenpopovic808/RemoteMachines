package rs.raf.domaci3.integration.RoleController;

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
import rs.raf.domaci3.model.Role;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerSteps extends TestsConfig {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestState userControllerState; //koristis jwt

    @Autowired
    private ObjectMapper objectMapper;

    private List<Role>roles;



    @When("admin requests all roles")
    public void adminRequestsAllRoles() throws UnsupportedEncodingException, JsonProcessingException {

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(
                    get("/api/roles/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + userControllerState.getJwtToken())
            ).andExpect(status().isOk());
        }catch (Exception e){
            e.printStackTrace();
        }

        MvcResult result = resultActions.andReturn();
        userControllerState.setActualResponseStatus(result.getResponse().getStatus());
        String addedUserJson = result.getResponse().getContentAsString();
        roles = objectMapper.readValue(addedUserJson, new TypeReference<ArrayList<Role>>(){});

    }

    @And("List size should be {int}")
    public void listSizeShouldBe(int arg0) {
        assertTrue(roles.size()==arg0);
    }



}
