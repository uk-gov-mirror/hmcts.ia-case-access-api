package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.reform.iacaseaccessapi.testutils.SpringBootIntegrationTest;

public class SupplementaryDetailsResponseIntegrationTest extends SpringBootIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void supplementaryDetailsEndpoint() throws Exception {
        MvcResult postResponse = mockMvc
            .perform(
                post("/supplementary-details")
                    .content(getSupplementaryDetailsRequest())
                    .contentType("application/json")
            )
            .andExpect(status().isPartialContent())
            .andReturn();

        assertEquals(getResponseData(), postResponse.getResponse().getContentAsString());
    }

    private String getSupplementaryDetailsRequest() {

        return
            "{\"ccd_case_numbers\":["
            + "\"11111111111111\","
            + "\"22222222222222\","
            + "\"99999999999999\"]}";
    }

    private String getResponseData() {
        return
            "{\"supplementary_info\":["
            + "{\"ccd_case_number\":\"11111111111111\",\"information_surname\":{\"surname\":\"Johnson\"}},"
            + "{\"ccd_case_number\":\"22222222222222\",\"information_surname\":{\"surname\":\"Johnson\"}}],"
            + "\"missing_supplementary_info\":{\"ccd_case_numbers\":[\"99999999999999\"]}}";
    }

}
