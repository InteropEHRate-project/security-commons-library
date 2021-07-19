package eu.interopehrate.security_commons.services.ca.model;

public class GetUserCertificateRequest {
    private String username;

    public GetUserCertificateRequest(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
