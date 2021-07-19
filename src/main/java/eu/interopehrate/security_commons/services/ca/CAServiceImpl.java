package eu.interopehrate.security_commons.services.ca;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.interopehrate.security_commons.services.SecurityHttpClient;
import eu.interopehrate.security_commons.services.ca.api.CAService;
import eu.interopehrate.security_commons.services.ca.model.GetUserCertificateRequest;
import eu.interopehrate.security_commons.services.ca.model.GetUserCertificateResponse;
import eu.interopehrate.security_commons.services.ca.model.ValidateCertificateRequest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class CAServiceImpl implements CAService {

    private final String caUrl;
    private final String validateCertificateEndpoint;
    private final String getCertififateEndpoint;

    public CAServiceImpl(String caUrl) {
        this.caUrl = caUrl;
        this.getCertififateEndpoint = "/getUserCertificateInfo";
        this.validateCertificateEndpoint = "/validatecertificate";
    }

    @Override
    public byte[] getUserCertificate(String userAlias) throws IOException, ExecutionException, InterruptedException {
        String uri = caUrl + getCertififateEndpoint;
        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(new GetUserCertificateRequest(userAlias));
        String res = SecurityHttpClient.createPost(uri, req);
        GetUserCertificateResponse[] response = mapper.readValue(res, GetUserCertificateResponse[].class);
        return response[0].getCertificateData();
    }

    @Override
    public Boolean validateUserCertificate(byte[] certificateData) throws IOException, ExecutionException, InterruptedException {
        String uri = caUrl + validateCertificateEndpoint;
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new ValidateCertificateRequest(certificateData));
        String result = SecurityHttpClient.createPost(uri, json);
        return result.startsWith("CN=");
    }
}
