package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.MissingSupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInformation;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInformationSurname;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.CcdSupplementaryDetailsSearchService;

@ExtendWith(MockitoExtension.class)
class SupplementaryDetailsControllerTest {

    @Mock
    private CcdSupplementaryDetailsSearchService ccdSupplementaryDetailsSearchService;

    private SupplementaryDetailsController supplementaryDetailsController;
    private ArrayList<String> ccdCaseNumberList = new ArrayList<String>();

    @BeforeEach
    public void setUp() {

        supplementaryDetailsController
            = new SupplementaryDetailsController(ccdSupplementaryDetailsSearchService);

        ccdCaseNumberList.add("123456789");
        ccdCaseNumberList.add("987654321");
        ccdCaseNumberList.add("1239874576");
    }

    @Test
    void should_return_supplementary_details_complete_on_request() {

        List<SupplementaryInformation> supplementaryInfo = new ArrayList<SupplementaryInformation>();
        SupplementaryInformation supplementaryInformation = new SupplementaryInformation();
        SupplementaryInformationSurname supplementaryInformationSurname = new SupplementaryInformationSurname("John");

        ccdCaseNumberList.forEach((ccdCaseNumber) -> {
            supplementaryInformation.setCcdCcaseNumber(ccdCaseNumber);
            supplementaryInformation.setInformationSurname(supplementaryInformationSurname);
            supplementaryInfo.add(supplementaryInformation);
        });

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails();
        supplementaryDetails.setSupplementaryInfo(supplementaryInfo);

        when(ccdSupplementaryDetailsSearchService.getCcdSupplementaryDetails(ccdCaseNumberList)).thenReturn(supplementaryDetails);

        ResponseEntity<SupplementaryDetails> response = supplementaryDetailsController.post(ccdCaseNumberList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ccdCaseNumberList.size(), response.getBody().getSupplementaryInfo().size());
        assertEquals("1239874576", response.getBody().getSupplementaryInfo().get(0).getCcdCcaseNumber());
        assertEquals("John", response.getBody().getSupplementaryInfo().get(0).getInformationSurname().getSurname());
        assertNull(response.getBody().getMissingSupplementaryInfo());
    }

    @Test
    void should_return_supplementary_details_partial_on_request() {

        List<SupplementaryInformation> supplementaryInfo = new ArrayList<SupplementaryInformation>();
        SupplementaryInformation supplementaryInformation = new SupplementaryInformation();
        SupplementaryInformationSurname supplementaryInformationSurname = new SupplementaryInformationSurname("John");

        List<String>  missingCcdCaseNumberList = new ArrayList<String>();
        MissingSupplementaryInfo missingSupplementaryInfo = new MissingSupplementaryInfo();

        int count = 0;
        while (count < ccdCaseNumberList.size()) {
            if (count == 0) {
                supplementaryInformation.setCcdCcaseNumber(ccdCaseNumberList.get(count));
                supplementaryInformation.setInformationSurname(supplementaryInformationSurname);
                supplementaryInfo.add(supplementaryInformation);
            } else {
                missingCcdCaseNumberList.add(ccdCaseNumberList.get(count));
            }
            missingSupplementaryInfo.setCcdCaseNumbers(missingCcdCaseNumberList);
            count++;
        }

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails();
        supplementaryDetails.setSupplementaryInfo(supplementaryInfo);
        supplementaryDetails.setMissingSupplementaryInfo(missingSupplementaryInfo);


        when(ccdSupplementaryDetailsSearchService.getCcdSupplementaryDetails(ccdCaseNumberList)).thenReturn(supplementaryDetails);

        ResponseEntity<SupplementaryDetails> response = supplementaryDetailsController.post(ccdCaseNumberList);

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
        assertEquals(1, response.getBody().getSupplementaryInfo().size());
        assertEquals("123456789", response.getBody().getSupplementaryInfo().get(0).getCcdCcaseNumber());
        assertEquals("John", response.getBody().getSupplementaryInfo().get(0).getInformationSurname().getSurname());
        assertEquals(2, response.getBody().getMissingSupplementaryInfo().getCcdCaseNumbers().size());
    }

    @Test
    void should_return_no_supplementary_details_on_request() {

        List<SupplementaryInformation> supplementaryInfo = new ArrayList<SupplementaryInformation>();

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails();
        supplementaryDetails.setSupplementaryInfo(supplementaryInfo);

        when(ccdSupplementaryDetailsSearchService.getCcdSupplementaryDetails(ccdCaseNumberList)).thenReturn(supplementaryDetails);

        ResponseEntity<SupplementaryDetails> response = supplementaryDetailsController.post(ccdCaseNumberList);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, response.getBody().getSupplementaryInfo().size());
    }

    @Test
    void should_return_forbidden_on_request() {

        List<SupplementaryInformation> supplementaryInfo = null;

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails();
        supplementaryDetails.setSupplementaryInfo(supplementaryInfo);

        when(ccdSupplementaryDetailsSearchService.getCcdSupplementaryDetails(ccdCaseNumberList)).thenReturn(supplementaryDetails);

        ResponseEntity<SupplementaryDetails> response = supplementaryDetailsController.post(ccdCaseNumberList);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void should_return_unauthorised_on_request() {

        ResponseEntity<SupplementaryDetails> response = supplementaryDetailsController.post(ccdCaseNumberList);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
