package mitest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Date;

// import com.azure.core.credential.AccessToken;
// import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;

// import reactor.core.publisher.Mono;

@RestController
public class TestController {

    // private String accountName;
    // private String containerName;

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public String root() {

        long startTimeMillis = System.currentTimeMillis();
        
        ManagedIdentityCredential managedIdentityCredential = new ManagedIdentityCredentialBuilder()
            .maxRetry(1)
            .retryTimeout(duration -> Duration.ofMinutes(1))
            .build();

        String clientId = managedIdentityCredential.getClientId();
        long endTimeMillis = System.currentTimeMillis();
        long duration = endTimeMillis - startTimeMillis;

        return "Time to get token: " + duration + " ms. Client ID is: " + clientId;
    }
    
}