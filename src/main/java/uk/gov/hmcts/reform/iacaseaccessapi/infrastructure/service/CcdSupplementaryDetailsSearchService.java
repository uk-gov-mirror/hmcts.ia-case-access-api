package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInfo;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.services.SupplementaryDetailsService;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.clients.CoreCaseDataApi;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.clients.model.ccd.CaseDetails;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.clients.model.ccd.SearchResult;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.security.SystemTokenGenerator;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.security.oauth2.IdentityManagerResponseException;

@Service
@Slf4j
public class CcdSupplementaryDetailsSearchService implements SupplementaryDetailsService {
    private static final String CASE_TYPE_ID = "Asylum";
    private static final String APPELLANT_FAMILY_NAME = "appellantFamilyName";

    private final CoreCaseDataApi coreCaseDataApi;
    private final SystemTokenGenerator systemTokenGenerator;
    private final AuthTokenGenerator s2sAuthTokenGenerator;


    public CcdSupplementaryDetailsSearchService(SystemTokenGenerator systemTokenGenerator,
                                                CoreCaseDataApi coreCaseDataApi,
                                                AuthTokenGenerator s2sAuthTokenGenerator) {
        this.systemTokenGenerator = systemTokenGenerator;
        this.coreCaseDataApi = coreCaseDataApi;
        this.s2sAuthTokenGenerator = s2sAuthTokenGenerator;
    }

    @Override
    public List<SupplementaryInfo> getSupplementaryDetails(List<String> ccdCaseNumberList) {

        log.info(
            "CcdSupplementaryDetailsSearchService : getSupplementaryDetails fetch data for the case ids  {} ",
            ccdCaseNumberList.toString()
        );

        String userToken;
        String s2sToken;

        try {
            userToken = "Bearer " + systemTokenGenerator.generate();
            log.info(
                "CcdSupplementaryDetailsSearchService : getSupplementaryDetails System user token has been generated."
            );

            // returned token is already with Bearer prefix
            s2sToken = s2sAuthTokenGenerator.generate();
            log.info("S2S token has been generated.");

        } catch (Exception e) {
            throw new IdentityManagerResponseException(e.getMessage(), e);
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery("reference", ccdCaseNumberList);
        searchSourceBuilder.size(100);
        searchSourceBuilder.from(0);
        searchSourceBuilder.sort("created_date", SortOrder.DESC);
        searchSourceBuilder.query(termQueryBuilder);

        return search(userToken, s2sToken, searchSourceBuilder.toString());
    }

    private List<SupplementaryInfo> search(String userAuthorisation, String serviceAuthToken, String query) {

        SearchResult searchResult = coreCaseDataApi.searchCases(
            userAuthorisation,
            serviceAuthToken,
            CASE_TYPE_ID,
            query
        );

        if (searchResult.getCases() == null) {
            return Collections.emptyList();
        }
        return searchResult.getCases()
            .stream()
            .filter(p -> p.getCaseData() != null && p.getId() != null)
            .map(this::extractSupplementaryInfo)
            .collect(Collectors.toList());
    }

    private SupplementaryInfo extractSupplementaryInfo(CaseDetails caseDetails) {
        return new SupplementaryInfo(
            String.valueOf(caseDetails.getId()),
            new SupplementaryDetails(String.valueOf(caseDetails.getCaseData().get(APPELLANT_FAMILY_NAME)))
        );
    }
}
