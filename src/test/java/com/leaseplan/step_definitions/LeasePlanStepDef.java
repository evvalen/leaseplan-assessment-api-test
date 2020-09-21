package com.leaseplan.step_definitions;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pojo.pets.Pet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// FIXME in general hard coded urls used
public class LeasePlanStepDef {
    private ResponseEntity<?> responseEntity;

    @Given("Headers accepts content type as {string}")
    public void headers_accepts_content_type_as(String string) {
        // TODO check if really nothing needs to be done here
    }

    @When("User sends POST request to {string}")
    public void user_sends_POST_request_to(String string, io.cucumber.datatable.DataTable dataTable) throws URISyntaxException {
        Map<String, String> petData = dataTable.asMap(String.class, String.class);

        // TODO this Pet POJO could come from a library or juist use pure JSON instead
        Pet pet = new Pet();
        pet.setId(petData.get("id"));
        pet.setName(petData.get("name"));
        pet.setStatus(petData.get("status"));

        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI("http://localhost:8080/api/pet");
        Object request = pet;
        Class<?> responseType = Pet.class;
        responseEntity = restTemplate.postForEntity(url, request, responseType);
    }

    @Then("User verifies that response status code is {int}")
    public void user_verifies_that_response_status_code_is(Integer int1) {
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(int1);
    }

    @Then("User verifies that response content type is {string}")
    public void user_verifies_that_response_content_type_is(String string) {
        assertThat(responseEntity.getHeaders().getContentType().toString()).isEqualTo(string);
    }

    @When("User sends GET request to \"pet\\/\"{int}")
    public void user_sends_GET_request_to_pet(Integer int1) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI("http://localhost:8080/api/pet/" + int1);
        Class<?> responseType = Pet.class;
        responseEntity = restTemplate.getForEntity(url, responseType);
    }

    @Given("User sends GET request to {string}")
    public void user_sends_GET_request_to(String string) {
        // TODO check if really nothing needs to be done here
    }

    @When("User selects pets with {string}")
    public void user_selects_pets_with(String string) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/pet/findByStatus")
                .queryParam("status", string);
        URI url = new URI(builder.toUriString());
        Class<?> responseType = Pet[].class;
        responseEntity = restTemplate.getForEntity(url, responseType);

        // Verify the content's structure
        Pet[] pets = (Pet[]) responseEntity.getBody();

        for(Pet pet : pets) {
            assertThat(pet).isNotNull();
        }
    }

    @Given("User sends DELETE request to \"pet\\/\"{int}")
    public void user_sends_DELETE_request_to_pet(Integer int1) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI("http://localhost:8080/api/pet/" + int1);
        Class<?> responseType = Pet.class;
        HttpMethod method = HttpMethod.DELETE;
        HttpEntity<?> requestEntity = new HttpEntity<>(null);
        responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
    }

    @When("Select {string} for the authorization filters as a API key")
    public void select_for_the_authorization_filters_as_a_API_key(String string) {
        // TODO check if really nothing needs to be done here
        // TODO there is no authorization at this moment
    }
}
