package pl.mg.cfm.network;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by m on 2014-11-23.
 */
public class NullHostNameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        Log.i("RestUtilImpl", "Approving certificate for " + hostname);
        return true;
    }
}
