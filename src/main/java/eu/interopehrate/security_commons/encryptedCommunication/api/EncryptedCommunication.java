package eu.interopehrate.security_commons.encryptedCommunication.api;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Created by smenesid on 15/7/2020.
 */
public interface EncryptedCommunication {

    /**
     *
     * Responsible for decrypted the scanned data
     *
     */
    public String decrypt(String encryptedPayload, String symKey) throws Exception;

    String decryptb(byte[] encryptedPayload, String symKey) throws Exception;

    /**
     *
     * Responsible for decrypted the scanned data
     *
     */
    public String encrypt(String payload, String symKey) throws Exception;

    byte[] encryptb(String payload, String symKey) throws Exception;

    /**
     *
     * Responsible for random AES 256bit key generation the scanned data
     *
     */
    String generateSymmtericKey() throws NoSuchAlgorithmException;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Alice creates her own DH key pair
     *
     */
    public KeyPair aliceInitKeyPair() throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Alice creates and initializes her DH KeyAgreement object
     *
     */
    public KeyAgreement aliceKeyAgreement(KeyPair aliceKpair) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Alice encodes her public key, and sends it over to Bob.
     *
     */
    public byte[] alicePubKeyEnc(KeyPair aliceKpair) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Bob has received Alice's public key in encoded format.
     * He instantiates a DH public key from the encoded key material.
     * Bob gets the DH parameters associated with Alice's public key.
     * He must use the same parameters when he generates his own key pair.
     * Bob creates his own DH key pair
     */
    public KeyPair bobInitKeyPair(byte[] alicePubKeyEnc) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Bob creates and initializes his DH KeyAgreement object
     */
    public KeyAgreement bobKeyAgreement(KeyPair bobKpair) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Bob encodes his public key, and sends it over to Alice.
     */
    public byte[] bobPubKeyEnc(KeyPair bobKpair) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Alice uses Bob's public key of her version of the DH
     * Before she can do so, she has to instantiate a DH public key
     * from Bob's encoded key material.
     * Alice generate the (same) shared secret.
     */
    public KeyAgreement aliceKeyAgreementFin(byte[] bobPubKeyEnc, KeyAgreement aliceKeyAgree) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Bob generate the (same) shared secret.
     *
     */
    public KeyAgreement bobKeyAgreementFin(byte[] alicePubKeyEnc, KeyAgreement bobKeyAgree) throws Exception;

    /**
     *
     * Responsible for Diffie-Hellman Key Exchange
     * Create an AES SecretKey object using the shared secret
     *
     */
    SecretKeySpec generateSymmtericKey(byte[] sharedSecret, int size);
}
