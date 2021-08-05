package eu.interopehrate.security_commons.services.ca.api;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;

public interface CAService {

    byte[] getUserCertificate(String userAlias) throws IOException, ExecutionException, InterruptedException;

    Boolean validateUserCertificate(byte[] certificateData) throws IOException, ExecutionException, InterruptedException;

    X509Certificate toX509Certificate(byte[] certificateData) throws CertificateException;
}
