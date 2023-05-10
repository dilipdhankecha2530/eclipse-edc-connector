/*
 *  Copyright (c) 2022 Daimler TSS GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Daimler TSS GmbH - Initial implementation
 *
 */

package org.eclipse.edc.iam.did.crypto.key;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.util.Base64URL;
import org.eclipse.edc.iam.did.spi.document.DidDocument;
import org.eclipse.edc.iam.did.spi.document.EllipticCurvePublicKey;
import org.eclipse.edc.iam.did.spi.document.JwkPublicKey;
import org.eclipse.edc.iam.did.spi.document.RSAPublicKey;
import org.eclipse.edc.iam.did.spi.key.PublicKeyWrapper;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Set;

import static java.lang.String.format;

public class KeyConverter {

    /**
     * Converts an {@link EllipticCurvePublicKey} into an {@link ECKey} from the Nimbus library.
     *
     * @param jwk A (valid) elliptic curve public key
     * @param id An arbitrary string that will serve as "kid" property of the key.
     * @return an {@link ECKey}
     * @throws IllegalArgumentException if any of the public key's properties are not valid. Check {@link ECKey} for details.
     */
    public static ECKey toEcKey(EllipticCurvePublicKey jwk, String id) {
        return new ECKey(Curve.parse(jwk.getCrv()),
                Base64URL.from(jwk.getX()),
                Base64URL.from(jwk.getY()),
                KeyUse.SIGNATURE,
                Set.of(KeyOperation.VERIFY),
                null,
                id,
                null, null, null, null, null
        );
    }

    //RSAKey(Base64URL n, Base64URL e, Base64URL d, Base64URL p, Base64URL q, Base64URL dp, Base64URL dq, Base64URL qi,
    // List<OtherPrimesInfo> oth, PrivateKey prv, KeyUse use, Set<KeyOperation> ops,
    // Algorithm alg, String kid, URI x5u, Base64URL x5t, Base64URL x5t256, List<Base64> x5c, Date exp, Date nbf, Date iat, KeyStore ks)
    public static RSAKey toRsaKey(RSAPublicKey jwk, String id) {
        return new RSAKey(Base64URL.from(jwk.getN()), Base64URL.from(jwk.getE()),null,null,null,null,null,null,
                null,null,null,null,
                Algorithm.parse(jwk.getAlg()),null, URI.create(jwk.getX5u()),null,null,null,null,null,null,null);
    }

    /**
     * Converts a {@link JwkPublicKey}, that is coming from one of the {@link DidDocument#getVerificationMethod()}s and converts it into a {@link PublicKeyWrapper}
     * <em>Note that currently only Elliptic-Curve public Keys are supported! An exception will be thrown if {@link JwkPublicKey#getKty()} is anything other than "EC" or "ec"!</em>
     *
     * @param publicKey The instance of the {@code JwkPublicKey}
     * @param id An arbitrary ID that serves as 'kid' property
     * @return A {@link PublicKeyWrapper}
     * @throws IllegalArgumentException if an invalid public key (something other than "EC") is passed or if the runtime-type is not {@link EllipticCurvePublicKey}
     */
    public static @NotNull PublicKeyWrapper toPublicKeyWrapper(JwkPublicKey publicKey, String id) {
        switch (publicKey.getKty()) {
            case "EC":
            case "ec":
                if (!(publicKey instanceof EllipticCurvePublicKey)) {
                    throw new IllegalArgumentException(format("Public key has 'kty' = '%s' but its Java type was %s!", publicKey.getKty(), publicKey.getClass()));
                }
                return new EcPublicKeyWrapper(toEcKey((EllipticCurvePublicKey) publicKey, id));
            case "rsa":
            case "RSA":
                if (!(publicKey instanceof RSAPublicKey)) {
                    throw new IllegalArgumentException(format("Public key has 'kty' = '%s' but its Java type was %s!", publicKey.getKty(), publicKey.getClass()));
                }
                return new RsaPublicKeyWrapper(toRsaKey((RSAPublicKey) publicKey, id));
            default:
                throw new IllegalArgumentException(format("Only public-key-JWK of type 'EC' can be used at the moment, but '%s' was specified!", publicKey.getKty()));
        }
    }

}
