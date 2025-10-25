package br.com.lrbarros.assinador.util;

import br.com.lrbarros.assinador.config.CryptoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Component
public class KeyStoreUtils {

    @Autowired
    private CryptoProperties prop;

    @Value("${crypto.keystore-path}")
    private Resource certificado;

    public KeyStore loadKeyStore() {
        try (InputStream inputStream = certificado.getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, prop.getKeystorePassword().toCharArray());
            return keyStore;
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao carregar o keystore: " + prop.getKeystorePath(), e);
        }
    }

    public PrivateKey getPrivateKey(KeyStore ks) throws Exception {
        Enumeration<String> aliases = ks.aliases();
        while (aliases.hasMoreElements()) {
            return (PrivateKey) ks.getKey(aliases.nextElement(), prop.getKeyPassword().toCharArray());
        }
        throw new IllegalStateException("Erro ao carregar o keystore: " + prop.getKeystorePath());
    }

    public X509Certificate getCertificate(KeyStore ks) throws Exception {
        return (X509Certificate) ks.getCertificate(prop.getKeyAlias());
    }
}
