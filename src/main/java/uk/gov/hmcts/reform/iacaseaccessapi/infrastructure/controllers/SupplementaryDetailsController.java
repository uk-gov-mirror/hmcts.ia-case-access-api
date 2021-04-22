package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers;

import static java.util.Collections.emptyList;
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
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.SupplementaryDetailsService;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers.model.MissingSupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers.model.SupplementaryDetailsRequest;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers.model.SupplementaryDetailsResponse;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.service.CcdSupplementaryDetailsSearchService;

@Slf4j
@RestController
public class SupplementaryDetailsController {

    private SupplementaryDetailsService supplementaryDetailsService;

    public SupplementaryDetailsController(SupplementaryDetailsService supplementaryDetailsService) {
        this.supplementaryDetailsService = supplementaryDetailsService;
    }

    @ApiOperation(
        value = "Handles 'supplementary-details' calls from Pay Hub",
        response = String.class,
        authorizations =
            {
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
    public ResponseEntity<SupplementaryDetailsResponse> post(@RequestBody SupplementaryDetailsRequest supplementaryDetailsRequest) {

        if (supplementaryDetailsRequest == null
            || supplementaryDetailsRequest.getCcdCaseNumbers() == null) {
            return badRequest().build();
        }

        List<String> ccdCaseNumberList = supplementaryDetailsRequest.getCcdCaseNumbers();
        log.info("Request ccdNumberList:"
            + ccdCaseNumberList.stream().collect(Collectors.joining(",")));

        try{

            List<SupplementaryInfo> supplementaryInfo = supplementaryDetailsService
                .getSupplementaryDetails(ccdCaseNumberList);
            SupplementaryDetailsResponse supplementaryDetailsResponse = new SupplementaryDetailsResponse(
                supplementaryInfo, missingSupplementaryDetailsInfo(ccdCaseNumberList, supplementaryInfo));

            if (supplementaryDetailsResponse.getSupplementaryInfo() == null) {
                return status(HttpStatus.FORBIDDEN).body(supplementaryDetailsResponse);

            } else if (supplementaryDetailsResponse.getSupplementaryInfo().isEmpty()) {
                return status(HttpStatus.NOT_FOUND).body(supplementaryDetailsResponse);

            } else if (supplementaryDetailsResponse.getSupplementaryInfo().size() < ccdCaseNumberList.size()) {
                return status(HttpStatus.PARTIAL_CONTENT).body(supplementaryDetailsResponse);

            } else if (supplementaryDetailsResponse.getSupplementaryInfo().size() == ccdCaseNumberList.size()) {
                return status(HttpStatus.OK).body(supplementaryDetailsResponse);

            } else {
                return status(HttpStatus.INTERNAL_SERVER_ERROR).body(supplementaryDetailsResponse);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private MissingSupplementaryInfo missingSupplementaryDetailsInfo(List<String> ccdCaseNumberList, List<SupplementaryInfo> supplementaryInfo) {

        // TODO implement correct solution
        return new MissingSupplementaryInfo(emptyList());
    }

}
