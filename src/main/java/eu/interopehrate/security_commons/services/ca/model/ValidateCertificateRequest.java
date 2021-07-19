package eu.interopehrate.security_commons.services.ca.model;

public class ValidateCertificateRequest {
    private byte[] certificate;

    public ValidateCertificateRequest(byte[] certificate) {
        this.certificate = certificate;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }
}
