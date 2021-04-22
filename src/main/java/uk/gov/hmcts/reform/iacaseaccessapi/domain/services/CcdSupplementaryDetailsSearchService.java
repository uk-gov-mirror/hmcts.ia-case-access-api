package uk.gov.hmcts.reform.iacaseaccessapi.domain.services;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInformation;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInformationSurname;

@Service
@Slf4j
public class CcdSupplementaryDetailsSearchService {

    public SupplementaryDetails getCcdSupplementaryDetails(List<String>  ccdCaseNumberList) {

        List<SupplementaryInformation> supplementaryInfo = new ArrayList<SupplementaryInformation>();

        SupplementaryInformationSurname supplementaryInformationSurname = new SupplementaryInformationSurname("John");

        ccdCaseNumberList.forEach((ccdCaseNumber) -> {
            SupplementaryInformation supplementaryInformation = new SupplementaryInformation();
            supplementaryInformation.setCcdCcaseNumber(ccdCaseNumber);
            supplementaryInformation.setInformationSurname(supplementaryInformationSurname);
            supplementaryInfo.add(supplementaryInformation);
        });

        SupplementaryDetails supplementaryDetails = new SupplementaryDetails();
        supplementaryDetails.setSupplementaryInfo(supplementaryInfo);

        return supplementaryDetails;
    }
}
