/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.iam.did.crypto.key;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.eclipse.edc.iam.did.spi.key.PublicKeyWrapper;

public class RsaPublicKeyWrapper implements PublicKeyWrapper {
    private final RSAKey publicKey;

    public RsaPublicKeyWrapper(RSAKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public JWEEncrypter encrypter() throws JOSEException {
        return new RSAEncrypter(publicKey);
    }

    @Override
    public JWSVerifier verifier() throws JOSEException {
        return new RSASSAVerifier(publicKey);
    }

    @Override
    public JWEAlgorithm jweAlgorithm() {
        return JWEAlgorithm.RSA_OAEP_256;
    }
}
