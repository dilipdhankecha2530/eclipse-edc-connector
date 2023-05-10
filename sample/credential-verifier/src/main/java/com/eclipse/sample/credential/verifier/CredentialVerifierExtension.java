package com.eclipse.sample.credential.verifier;

import org.eclipse.edc.iam.did.spi.credentials.CredentialsVerifier;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;

@Extension(value = "TestCredentialVerifier")
public class CredentialVerifierExtension implements ServiceExtension {

    @Provider
    public CredentialsVerifier credentialsVerifier(ServiceExtensionContext context) {
        return new TestCredentialsVerifier();
    }
}
