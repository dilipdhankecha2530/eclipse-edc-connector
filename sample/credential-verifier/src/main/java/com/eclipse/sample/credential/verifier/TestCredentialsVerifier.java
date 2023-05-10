package com.eclipse.sample.credential.verifier;

import org.eclipse.edc.iam.did.spi.credentials.CredentialsVerifier;
import org.eclipse.edc.iam.did.spi.document.DidDocument;
import org.eclipse.edc.spi.result.Result;

import java.util.Map;

public class TestCredentialsVerifier implements CredentialsVerifier {
    @Override
    public Result<Map<String, Object>> getVerifiedCredentials(DidDocument participantDid) {
        return Result.success(Map.of("client_id","anonymous"));
    }
}

