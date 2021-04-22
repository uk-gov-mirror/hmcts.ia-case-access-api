package uk.gov.hmcts.reform.iacaseaccessapi.domain.services;

import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;

import java.util.List;

public interface SupplementaryDetailsService {

    List<SupplementaryInfo> getSupplementaryDetails(List<String>  ccdCaseNumberList);
}
