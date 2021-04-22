package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.SupplementaryDetailsService;

@Service
@Slf4j
public class CcdSupplementaryDetailsSearchService implements SupplementaryDetailsService {

    // inject CCD API

    @Override
    public List<SupplementaryInfo> getSupplementaryDetails(List<String>  ccdCaseNumberList) {

        List<SupplementaryInfo> supplementaryInfo = new ArrayList<>();

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails("John");

        ccdCaseNumberList.forEach((ccdCaseNumber) -> {
            // mock
            if(!"3354323678765432".equals(ccdCaseNumber)) {
                SupplementaryInfo supplementaryInformation = new SupplementaryInfo(ccdCaseNumber, supplementaryDetails);
                supplementaryInfo.add(supplementaryInformation);
            }
        });

        return supplementaryInfo;
    }
}
