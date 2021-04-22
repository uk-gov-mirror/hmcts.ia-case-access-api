package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers;

import static org.springframework.http.ResponseEntity.*;

import io.swagger.annotations.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.CcdSupplementaryDetailsSearchService;

@Slf4j
@RestController
public class SupplementaryDetailsController {

    private CcdSupplementaryDetailsSearchService ccdSupplementaryDetailsSearchService;

    public SupplementaryDetailsController(CcdSupplementaryDetailsSearchService ccdSupplementaryDetailsSearchService) {
        this.ccdSupplementaryDetailsSearchService = ccdSupplementaryDetailsSearchService;
    }

    @ApiOperation(
        value = "Handles 'supplementary-details' calls from Pay Hub",
        response = String.class,
        authorizations =
            {
                @Authorization(value = "Authorization"),
                @Authorization(value = "ServiceAuthorization")
            }
    )
    @ApiResponses({
        @ApiResponse(
            code = 200,
            message = "Supplementary details completely retrieved.",
            response = String.class
        ),
        @ApiResponse(
            code = 206,
            message = "Supplementary details partially retrieved.",
            response = String.class
        ),
        @ApiResponse(
            code = 401,
            message = "Unauthorized - missing or invalid S2S token."
        ),
        @ApiResponse(
            code = 403,
            message = "Forbidden - system user does not have access to the resources."
        ),
        @ApiResponse(
            code = 404,
            message = "Supplementary details not found for all the case numbers given."
        ),
        @ApiResponse(
            code = 500,
            message = "Unexpected or Run time exception."
        )
    })
    @PostMapping(path = "/supplementary-details")
    public ResponseEntity<SupplementaryDetails> post(@RequestBody List<String>  ccdCaseNumberList) {

        if (ccdCaseNumberList == null
            || ccdCaseNumberList.isEmpty()) {
            return badRequest().build();
        }

        log.info("Request ccdNumberList:"
            + ccdCaseNumberList.stream().collect(Collectors.joining(",")));

        SupplementaryDetails supplementaryDetails = ccdSupplementaryDetailsSearchService.getCcdSupplementaryDetails(ccdCaseNumberList);

        if (supplementaryDetails == null) {
            return status(HttpStatus.UNAUTHORIZED).body(supplementaryDetails);

        } else if (supplementaryDetails.getSupplementaryInfo() == null) {
            return status(HttpStatus.FORBIDDEN).body(supplementaryDetails);

        } else if (supplementaryDetails.getSupplementaryInfo().isEmpty()) {
            return status(HttpStatus.NOT_FOUND).body(supplementaryDetails);

        } else if (supplementaryDetails.getSupplementaryInfo().size() < ccdCaseNumberList.size()) {
            return status(HttpStatus.PARTIAL_CONTENT).body(supplementaryDetails);

        } else if (supplementaryDetails.getSupplementaryInfo().size() == ccdCaseNumberList.size()) {
            return status(HttpStatus.OK).body(supplementaryDetails);

        } else {
            return status(HttpStatus.INTERNAL_SERVER_ERROR).body(supplementaryDetails);
        }
    }

}
