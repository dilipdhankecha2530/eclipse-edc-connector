package org.eclipse.edc.iam.did.spi.document;

public class RSAPublicKey  implements JwkPublicKey{
    private String kty;
    private String n;
    private String e;
    private String alg;
    private String x5u;

    public RSAPublicKey() {
        // needed for JSON Deserialization
    }

    public RSAPublicKey(String kty, String n, String e, String alg, String x5u) {
        this.kty = kty;
        this.n = n;
        this.e = e;
        this.alg = alg;
        this.x5u = x5u;
    }

    @Override
    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getX5u() {
        return x5u;
    }

    public void setX5u(String x5u) {
        this.x5u = x5u;
    }
}
