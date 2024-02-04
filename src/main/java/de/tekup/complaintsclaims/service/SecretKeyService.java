package de.tekup.complaintsclaims.service;

import de.tekup.complaintsclaims.exception.SecretKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class SecretKeyService {

    public KeyPair generateSecretKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);

            return generator.generateKeyPair();
        } catch (Exception exception) {
            log.error("SecretKeyService::generateSecretKeys => " + exception.getMessage());
            throw new SecretKeyException(exception.getMessage());
        }
    }

    public String encrypt(String dataToEncrypt, String publicKeyString) {
        try {
            byte[] dataToBytes = dataToEncrypt.getBytes();
            byte[] publicKeyBytes = decode(publicKeyString);
            // Create a PublicKey object from the bytes
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            byte[] encryptedBytes = getBytesOutOfCipherInstance(dataToBytes, null, publicKey);

            return encode(encryptedBytes);
        } catch (Exception exception) {
            log.error("SecretKeyService::encrypt => " + exception.getMessage());
            throw new SecretKeyException(exception.getMessage());
        }
    }

    public String decrypt(String dataToDecrypt, String privateKeyString) {
        try {
            byte[] privateKeyBytes = decode(privateKeyString);

            // Create a PrivateKey object from the bytes
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // Continue with the decryption using the PrivateKey
            byte[] encryptedBytes = decode(dataToDecrypt);
            byte[] decryptedBytes = getBytesOutOfCipherInstance(encryptedBytes, privateKey, null);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            log.error("SecretKeyService::encrypt => " + exception.getMessage());
            throw new SecretKeyException(exception.getMessage());
        }
    }

    private byte[] getBytesOutOfCipherInstance(byte[] dataBytes, PrivateKey privateKey, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            if (publicKey != null) {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
            }

            return cipher.doFinal(dataBytes);
        } catch (Exception exception) {
            log.error("SecretKeyService::getCipherInstance => " + exception.getMessage());
            throw new SecretKeyException(exception.getMessage());
        }
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
}
