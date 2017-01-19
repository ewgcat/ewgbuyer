package com.ewgvip.buyer.android.net.httpsfactroy;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by Administrator on 2016/9/29.
 */

public class HttpsFactroy {
    public static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }
        SSLSocketFactory sslsocketfactory = null;
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            sslsocketfactory = sslContext.getSocketFactory();
            return sslsocketfactory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslsocketfactory;
    }

    public static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {

        HostnameVerifier hostnameverifier = new HostnameVerifier() {

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

        return hostnameverifier;
    }

}