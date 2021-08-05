package eu.interopehrate.security_commons.services.ca;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.interopehrate.security_commons.services.SecurityHttpClient;
import eu.interopehrate.security_commons.services.ca.api.CAService;
import eu.interopehrate.security_commons.services.ca.model.GetUserCertificateRequest;
import eu.interopehrate.security_commons.services.ca.model.GetUserCertificateResponse;
import eu.interopehrate.security_commons.services.ca.model.ValidateCertificateRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
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
        return result.contains("CN=");
    }

    @Override
    public X509Certificate toX509Certificate(byte[] certificateData) throws CertificateException {
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        //ISO-8859-1
        String base64string = new String(certificateData, StandardCharsets.ISO_8859_1);
        byte[] decoded = Base64.getMimeDecoder().decode(base64string);
        X509Certificate certificate = (X509Certificate) f.generateCertificate(new ByteArrayInputStream(decoded));
        return certificate;
    }
}
