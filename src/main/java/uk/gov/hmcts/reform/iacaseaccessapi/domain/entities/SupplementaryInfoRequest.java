package uk.gov.hmcts.reform.iacaseaccessapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SupplementaryInfoRequest {

    private List<String> ccdCaseNumbers;

    public void setCcdCaseNumbers(List<String> ccdCaseNumbers) {
        this.ccdCaseNumbers = ccdCaseNumbers;
    }
}
