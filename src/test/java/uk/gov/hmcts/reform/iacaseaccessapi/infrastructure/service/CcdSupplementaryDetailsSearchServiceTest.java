package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;

class CcdSupplementaryDetailsSearchServiceTest {

    private CcdSupplementaryDetailsSearchService ccdSupplementaryDetailsSearchService;
    private ArrayList<String> ccdCaseNumberList = new ArrayList<String>();

    @BeforeEach
    public void setUp() throws Exception {
        ccdSupplementaryDetailsSearchService = new CcdSupplementaryDetailsSearchService();
    }

    @Test
    void should_get_supplementary_details() {

        ccdCaseNumberList.add("11111111111111");
        ccdCaseNumberList.add("22222222222222");
        ccdCaseNumberList.add("99999999999999");

        List<SupplementaryInfo> supplementaryInfoList =
            ccdSupplementaryDetailsSearchService.getSupplementaryDetails(ccdCaseNumberList);

        assertEquals(2, supplementaryInfoList.size());
        assertEquals("11111111111111", supplementaryInfoList.get(0).getCcdCaseNumber());
        assertEquals("Johnson", supplementaryInfoList.get(0).getSupplementaryDetails().getSurname());
        assertEquals("22222222222222", supplementaryInfoList.get(1).getCcdCaseNumber());
        assertEquals("Johnson", supplementaryInfoList.get(1).getSupplementaryDetails().getSurname());
    }
}
