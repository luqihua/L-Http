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
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by lqh on 2016/11/8.
 */
public class HttpsFactory {

    public static X509TrustManager sTrustManager;

    public static SSLSocketFactory getSSLSocketFactory(InputStream... inputStreams) {
        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");
            CertificateFactory factory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;

            for (InputStream stream : inputStreams) {
                if (stream == null) continue;
                String name = Integer.toString(index++);
                keyStore.setCertificateEntry(name, factory.generateCertificate(stream));
                stream.close();
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext.init(null, getWrappedTrustManagers(trustManagerFactory.getTrustManagers()), new SecureRandom());

            return sslContext.getSocketFactory();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }


    /**
     * 安卓的 SSLContext 自带的TrustManager无法让本文示例中提到的自签名证书通过验证.
     * 解决的办法是自己定义一个 TrustManager 类. 然后用这个类去验证自签名证书.
     *
     * @param trustManagers
     * @return
     */
    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        sTrustManager = new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {

                return originalTrustManager.getAcceptedIssuers();

            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {

                try {

                    originalTrustManager.checkClientTrusted(certs, authType);

                } catch (CertificateException e) {

                    e.printStackTrace();

                }

            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {

                try {

                    originalTrustManager.checkServerTrusted(certs, authType);

                } catch (CertificateException e) {

                    e.printStackTrace();

                }

            }

        };
        return new TrustManager[]{
                sTrustManager
        };

    }
}
