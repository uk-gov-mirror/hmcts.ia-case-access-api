package uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.clients;


import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.clients.model.idam.Token;
import uk.gov.hmcts.reform.iacaseaccessapi.infrastructure.config.FeignConfiguration;

@FeignClient(
    name = "idam-api",
    url = "${idam.baseUrl}",
    configuration = FeignConfiguration.class
)
public interface IdamApi {

    @PostMapping(value = "/o/token", produces = "application/json", consumes = "application/x-www-form-urlencoded")
    Token token(@RequestBody Map<String, ?> form);

}
