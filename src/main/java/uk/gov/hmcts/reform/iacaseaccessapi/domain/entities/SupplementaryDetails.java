package uk.gov.hmcts.reform.iacaseaccessapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.iacaseaccessapi.domain.entities.SupplementaryInformation;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SupplementaryDetails {

    private List<SupplementaryInformation> supplementaryInfo;

    private MissingSupplementaryInfo missingSupplementaryInfo;

    public void setSupplementaryInfo(List<SupplementaryInformation> supplementaryInfo) {
        this.supplementaryInfo = supplementaryInfo;
    }

    public void setMissingSupplementaryInfo(MissingSupplementaryInfo missingSupplementaryInfo) {
        this.missingSupplementaryInfo = missingSupplementaryInfo;
    }
}
