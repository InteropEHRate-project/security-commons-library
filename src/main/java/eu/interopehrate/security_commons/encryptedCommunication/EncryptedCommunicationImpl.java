package eu.interopehrate.security_commons.encryptedCommunication;

import eu.interopehrate.security_commons.encryptedCommunication.api.EncryptedCommunication;
import org.apache.commons.text.StringEscapeUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64; //TODO: Base64 for android??

/**
 * Created by smenesid on 15/7/2020.
 */
public class EncryptedCommunicationImpl implements EncryptedCommunication {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) throws Exception
    {
        MessageDigest sha = null;
        key = myKey.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }

    @Override
    public String decrypt(String encryptedPayload, String symKey) throws Exception {
        symKey = StringEscapeUtils.unescapeJava(symKey);
        setKey(symKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return new String(cipher.doFinal(Base64.getMimeDecoder().decode(encryptedPayload)));
    }

    @Override
    public String decryptb(byte[] encryptedPayload, String symKey) throws Exception {
        setKey(symKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(encryptedPayload));
    }

    @Override
    public String encrypt(String payload, String symKey) throws Exception {
        setKey(symKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(payload.getBytes("UTF-8")));
        return encryptedString;

        //return Base64.getEncoder().encodeToString(cipher.doFinal(payload.getBytes("UTF-8")));
    }

    @Override
    public byte[] encryptb(String payload, String symKey) throws Exception {
        setKey(symKey);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] ciphertext = cipher.doFinal(payload.getBytes());
        return ciphertext;
    }

    @Override
    public String generateSymmtericKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        SecretKey key = keygen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    @Override
    public KeyPair aliceInitKeyPair() throws Exception {
        /*
         * Alice creates her own DH key pair with 2048-bit key size
         */
        System.out.println("ALICE: Generate DH keypair ...");
        KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
        aliceKpairGen.initialize(512);//2048
        KeyPair aliceKpair = aliceKpairGen.generateKeyPair();
        return aliceKpair;
    }

    @Override
    public KeyAgreement aliceKeyAgreement(KeyPair aliceKpair) throws Exception {
        // Alice creates and initializes her DH KeyAgreement object
        System.out.println("ALICE: Initialization ...");
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKpair.getPrivate());
        return aliceKeyAgree;
    }

    @Override
    public byte[] alicePubKeyEnc(KeyPair aliceKpair) throws Exception {
        // Alice encodes her public key, and sends it over to Bob.
        byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();
        String salicePubKeyEnc = new String(alicePubKeyEnc);
        return alicePubKeyEnc;
    }

    @Override
    public KeyPair bobInitKeyPair(byte[] alicePubKeyEnc) throws Exception {
        /*
         * Let's turn over to Bob. Bob has received Alice's public key
         * in encoded format.
         * He instantiates a DH public key from the encoded key material.
         */
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(alicePubKeyEnc);

        PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);

        /*
         * Bob gets the DH parameters associated with Alice's public key.
         * He must use the same parameters when he generates his own key
         * pair.
         */
        DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey)alicePubKey).getParams();

        // Bob creates his own DH key pair
        System.out.println("BOB: Generate DH keypair ...");
        KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
        bobKpairGen.initialize(dhParamFromAlicePubKey);
        KeyPair bobKpair = bobKpairGen.generateKeyPair();
        return bobKpair;
    }

    @Override
    public KeyAgreement bobKeyAgreement(KeyPair bobKpair) throws Exception {
        // Bob creates and initializes his DH KeyAgreement object
        System.out.println("BOB: Initialization ...");
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKpair.getPrivate());
        return bobKeyAgree;
    }

    @Override
    public byte[] bobPubKeyEnc(KeyPair bobKpair) throws Exception {
        // Bob encodes his public key, and sends it over to Alice.
        byte[] bobPubKeyEnc = bobKpair.getPublic().getEncoded();
        return bobPubKeyEnc;
    }

    @Override
    public KeyAgreement aliceKeyAgreementFin(byte[] bobPubKeyEnc, KeyAgreement aliceKeyAgree) throws Exception {
        /*
         * Alice uses Bob's public key for the first (and only) phase
         * of her version of the DH
         * protocol.
         * Before she can do so, she has to instantiate a DH public key
         * from Bob's encoded key material.
         */
        KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
        PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
        System.out.println("ALICE: Execute PHASE1 ...");
        aliceKeyAgree.doPhase(bobPubKey, true);
        return aliceKeyAgree;
    }

    @Override
    public KeyAgreement bobKeyAgreementFin(PublicKey alicePubKey, KeyAgreement bobKeyAgree) throws Exception {
        /*
         * Bob uses Alice's public key for the first (and only) phase
         * of his version of the DH
         * protocol.
         */
        System.out.println("BOB: Execute PHASE1 ...");
        bobKeyAgree.doPhase(alicePubKey, true);
        return bobKeyAgree;
    }

    @Override
    public SecretKeySpec generateSymmtericKey(byte[] sharedSecret, int size) {
        SecretKeySpec aesKey = new SecretKeySpec(sharedSecret, 0, size, "AES");
        return aesKey;
    }
}
