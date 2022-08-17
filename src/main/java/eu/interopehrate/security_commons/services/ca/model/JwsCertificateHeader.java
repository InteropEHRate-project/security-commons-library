package eu.interopehrate.security_commons.services.ca.model;

public class JwsCertificateHeader {
    private String alg;
    private byte[] certificate;

    public JwsCertificateHeader() {
    }

    public JwsCertificateHeader(String alg, byte[] certificate) {
        this.alg = alg;
        this.certificate = certificate;
    }


    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }
}
