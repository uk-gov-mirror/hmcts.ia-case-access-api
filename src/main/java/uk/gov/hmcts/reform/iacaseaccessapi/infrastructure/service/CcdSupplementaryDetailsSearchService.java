package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.SupplementaryDetailsService;

@Service
@Slf4j
public class CcdSupplementaryDetailsSearchService implements SupplementaryDetailsService {

    // inject CCD API

    @Override
    public List<SupplementaryInfo> getSupplementaryDetails(List<String>  ccdCaseNumberList) {

        List<SupplementaryInfo> supplementaryInfoList = new ArrayList<>();

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails("Johnson");

        ccdCaseNumberList.forEach((ccdCaseNumber) -> {
            // mock
            if (!"99999999999999".equals(ccdCaseNumber)) {
                SupplementaryInfo supplementaryInformation = new SupplementaryInfo(ccdCaseNumber, supplementaryDetails);
                supplementaryInfoList.add(supplementaryInformation);
            }
        });

        return supplementaryInfoList;
    }
}
