package com.lu.rxhttp.util;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by lqh on 2016/11/8.
 */
public class HttpsFactory {

    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager x509TrustManager;

    public HttpsFactory(InputStream... inputStreams) {
        getSSLSocketFactory(inputStreams);
    }

    public void getSSLSocketFactory(InputStream... inputStreams) {
        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            int count = inputStreams.length;
            for (int i = 0; i < count; i++) {
                InputStream stream = inputStreams[i];
                if (stream == null) continue;
                String name = String.valueOf(i);
                keyStore.setCertificateEntry(name, factory.generateCertificate(stream));
                stream.close();
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            //使用holder返回SSLSocketFactory和X509TrustManager
            x509TrustManager = (X509TrustManager) trustManagers[0];
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }
}
