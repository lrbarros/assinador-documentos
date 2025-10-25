package br.com.lrbarros.assinador.service;


import br.com.lrbarros.assinador.exception.CryptoException;
import br.com.lrbarros.assinador.model.Documento;
import br.com.lrbarros.assinador.util.KeyStoreUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Base64;

@Service
@Getter
public class AssinadorService {

    private final KeyStoreUtils keyStoreUtils;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String algorithm;

    public AssinadorService(KeyStoreUtils keyStoreUtils) {
        this.keyStoreUtils = keyStoreUtils;
    }

    @PostConstruct
    public void init() {
        try {
            KeyStore ks = keyStoreUtils.loadKeyStore();
            this.privateKey = keyStoreUtils.getPrivateKey(ks);
            X509Certificate cert = keyStoreUtils.getCertificate(ks);
            this.publicKey = cert.getPublicKey();
            this.algorithm = resolveAlgorithm(privateKey);
        } catch (Exception e) {
            throw new CryptoException("Erro ao inicializar chaves de assinatura", e);
        }
    }

    private String resolveAlgorithm(PrivateKey key) {
        if (key instanceof RSAPrivateKey) {
            int keySize = ((RSAPrivateKey) key).getModulus().bitLength();
            if (keySize >= 3072) {
                return "RSASSA-PSS";
            }
            return "SHA256withRSA";
        } else if (key instanceof ECPrivateKey ecKey) {
            int size = ecKey.getParams().getCurve().getField().getFieldSize();
            if (size <= 256) return "SHA256withECDSA";
            else return "SHA384withECDSA";
        } else if (key.getAlgorithm().equalsIgnoreCase("Ed25519")) {
            return "Ed25519";
        }
        throw new IllegalArgumentException("Unsupported key type: " + key.getAlgorithm());
    }

    public Documento assinar(Documento doc) {
        try {
            Signature signature = getSignatureInstance();
            signature.initSign(privateKey);
            signature.update(doc.conteudo().getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return new Documento(doc.conteudo(), Base64.getEncoder().encodeToString(signed));
        } catch (Exception e) {
            throw new CryptoException("Erro ao assinar dados", e);
        }
    }

    public boolean verify(String data, String signatureBase64) {
        try {
            Signature signature = getSignatureInstance();
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signatureBase64));
        } catch (Exception e) {
            throw new CryptoException("Erro ao verificar assinatura", e);
        }
    }

    private Signature getSignatureInstance() throws Exception {
        if ("RSASSA-PSS".equals(algorithm)) {
            // RSASSA-PSS usa parâmetros diferentes de RSA PKCS#1
            Signature sig = Signature.getInstance("RSASSA-PSS");
            sig.setParameter(new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1));
            return sig;
        }
        return Signature.getInstance(algorithm);
    }

}
