package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import uk.gov.hmcts.reform.iacaseaccessapi.testutils.SpringBootIntegrationTest;

public class SupplementaryDetailsIntegrationTest extends SpringBootIntegrationTest {

    @Test
    public void supplementaryDetailsEndpoint() throws Exception {
        MvcResult postResponse = mockMvc
            .perform(
                post("/supplementary-details")
                    .content(getSupplementaryInfoRequest())
                    .contentType("application/json")
            )
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(getResponseData(), postResponse.getResponse().getContentAsString());
    }

    private String getSupplementaryInfoRequest() {
        return ("[\"22876543212322\", \"12345678765432\", \"3354323678765432\"]");
    }

    private String getResponseData() {
        return
            "{\"supplementary_info\":["
            + "{\"ccd_ccase_number\":\"22876543212322\",\"information_surname\":{\"surname\":\"John\"}},"
            + "{\"ccd_ccase_number\":\"12345678765432\",\"information_surname\":{\"surname\":\"John\"}},"
            + "{\"ccd_ccase_number\":\"3354323678765432\",\"information_surname\":{\"surname\":\"John\"}}],"
            + "\"missing_supplementary_info\":null}";
    }

}
