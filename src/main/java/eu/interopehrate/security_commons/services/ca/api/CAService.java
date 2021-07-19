package eu.interopehrate.security_commons.services.ca.api;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface CAService {

    byte[] getUserCertificate(String userAlias) throws IOException, ExecutionException, InterruptedException;

    Boolean validateUserCertificate(byte[] certificateData) throws IOException, ExecutionException, InterruptedException;
}
