package uk.gov.hmcts.reform.iacaseaccessapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SupplementaryInformation {

    private String ccdCcaseNumber;
    private SupplementaryInformationSurname informationSurname;

    public void setCcdCcaseNumber(String ccdCcaseNumber) {
        this.ccdCcaseNumber = ccdCcaseNumber;
    }

    public void setInformationSurname(SupplementaryInformationSurname informationSurname) {
        this.informationSurname = informationSurname;
    }

}
