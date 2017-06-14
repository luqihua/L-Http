package com.lu.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Author: luqihua
 * Time: 2017/6/13
 * Description: SSLFactory
 */

public class SSLFactory {

    public static SSLSocketFactory getSocketFactory(InputStream... inputStreams) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;

            for (InputStream stream : inputStreams) {
                String name = Integer.toString(index++);
                keyStore.setCertificateEntry(name, factory.generateCertificate(stream));
                if (stream != null) {
                    stream.close();
                }
            }


            SSLContext context = SSLContext.getInstance("TLS");

            TrustManagerFactory managerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            managerFactory.init(keyStore);

            context.init(null,managerFactory.getTrustManagers(),new SecureRandom());

            return context.getSocketFactory();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

}
