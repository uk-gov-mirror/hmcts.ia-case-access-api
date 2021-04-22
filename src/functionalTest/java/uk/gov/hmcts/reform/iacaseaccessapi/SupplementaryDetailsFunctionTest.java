package uk.gov.hmcts.reform.iacaseaccessapi;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.iacaseaccessapi.testutils.FunctionalTest;

public class SupplementaryDetailsFunctionTest extends FunctionalTest {

    @Test
    public void should_allow_unauthorized_requests_and_return_401_response_code() {

        Response response = given(requestSpecification)
            .when()
            .get("/supplementary-details")
            .then()
            .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(401);
    }

}
