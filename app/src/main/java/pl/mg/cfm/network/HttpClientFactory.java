package pl.mg.cfm.network;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by m on 2014-11-23.
 */
public class HttpClientFactory {


    public static final String TAG = HttpClientFactory.class.getCanonicalName();

    public static void loadCert(Context context) {
        Log.d(TAG, "loadCert()");
        InputStream is = null;
        InputStream caInput = null;
        InputStream in = null;
        StringWriter writer = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            AssetManager assManager = context.getAssets();

            try {
                is = assManager.open("cert/server.cer");
            } catch (IOException e) {
                e.printStackTrace();
            }
            caInput = new BufferedInputStream(is);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }


            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
//            sslContext.init(null, new X509TrustManager[]{new TrustAllManager()}, new SecureRandom());

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    HostnameVerifier hv =
                            HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify("192.168.1.104", session);
                }
            };

// Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL("https://192.168.1.104:8444");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setHostnameVerifier(hostnameVerifier);


            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());


            in = urlConnection.getInputStream();
            writer = new StringWriter();
            IOUtils.copy(in, writer, "UTF-8");
            String theString = writer.toString();
            Log.d(TAG, theString);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static HttpClient getHttpClient(Context context) {
       /* HttpClient client = null;


        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http",PlainSocketFactory.getSocketFactory(), 9080));

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

       *//* TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        };*/

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new TrustAllSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
            registry.register(new Scheme("https", sf, 8444));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }

       /* return client;*/
    }
}
