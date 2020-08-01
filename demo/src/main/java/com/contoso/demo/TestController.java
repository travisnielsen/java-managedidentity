package com.contoso.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Locale;

import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@RestController
public class TestController {

    private BlobContainerClient blobContainerClient;

    @Value("${azure.storage.account:local}")
    private String accountName;

    @GetMapping("/container/{name}")
    @ResponseStatus(code = HttpStatus.OK)
    public String getContainer(@PathVariable String name) {

        String msiEndpoint = System.getenv("MSI_ENDPOINT");
        
        long startTimeMillis = System.currentTimeMillis();
        
        ManagedIdentityCredential credential = new ManagedIdentityCredentialBuilder()
            .maxRetry(1)
            .retryTimeout(duration -> Duration.ofMinutes(1))
            .build();

        String clientId = credential.getClientId();
        long endTimeMillis = System.currentTimeMillis();
        long duration = endTimeMillis - startTimeMillis;

        String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName);
        BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();
        blobContainerClient = storageClient.getBlobContainerClient(name);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }

        return "Time to get token: " + duration + " ms. Endpoint is: [" + msiEndpoint + "] Client ID is: [" + clientId + "]";
    }
    
}