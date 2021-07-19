package eu.interopehrate.security_commons.services.ca.model;

public class GetUserCertificateResponse {
    private String certificate;
    private String keyStore;
    private int type;
    private byte[] certificateData;

    public GetUserCertificateResponse() {
    }

    public GetUserCertificateResponse(String certificate, String keyStore, int type, byte[] certificateData) {
        this.certificate = certificate;
        this.keyStore = keyStore;
        this.type = type;
        this.certificateData = certificateData;
    }

    public byte[] getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(byte[] certificateData) {
        this.certificateData = certificateData;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
