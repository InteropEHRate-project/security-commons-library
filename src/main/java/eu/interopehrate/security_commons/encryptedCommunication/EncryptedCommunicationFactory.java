package eu.interopehrate.security_commons.encryptedCommunication;

import eu.interopehrate.security_commons.encryptedCommunication.api.EncryptedCommunication;

/**
 * Created by smenesid on 15/7/2020.
 */
public class EncryptedCommunicationFactory {
    private EncryptedCommunicationFactory() {}

    /**
     * Factory method for creating an instance of EncryptedCommunication
     *
     * @return
     */
    public static EncryptedCommunication create() {
        return new EncryptedCommunicationImpl();
    }
}
