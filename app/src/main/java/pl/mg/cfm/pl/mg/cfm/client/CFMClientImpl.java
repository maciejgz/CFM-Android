package pl.mg.cfm.pl.mg.cfm.client;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import pl.mg.cfm.message.CFMJsonSimpleMessage;
import pl.mg.cfm.network.CFMUtilsDictionary;
import pl.mg.cfm.network.HttpClientFactory;
import pl.mg.cfm.pojo.CarPojo;
import pl.mg.cfm.pojo.EmployeePojo;
import pl.mg.cfm.serializer.CFMMessageSerializer;
import pl.mg.cfm.serializer.CarSerializer;

/**
 * Created by m on 2014-11-25.
 */
public class CFMClientImpl implements CFMClient {

    private HttpClient httpClient;

    @Override
    public EmployeePojo login(String id, String password) {

        if (this.httpClient == null) {
            httpClient = HttpClientFactory.getHttpClient();
        }
        return null;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            DefaultHttpClient client = null;
            try {
                client = (DefaultHttpClient) HttpClientFactory.getHttpClient();
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials("9996", "testPass");
                HttpGet get = new HttpGet(CFMUtilsDictionary.SERVER_URL + "cfm-web/car/");
                get.setHeader(BasicScheme.authenticate(creds, "UTF-8", false));
                try {
                    org.apache.http.HttpResponse httpResponse = client.execute(get);
                    System.out.println(httpResponse.getStatusLine().getStatusCode() + ";"
                            + httpResponse.getStatusLine().getReasonPhrase());

                    HttpEntity entity = httpResponse.getEntity();
                    String entityContents = EntityUtils.toString(entity);
                    System.out.println(entityContents);

                    CFMMessageSerializer serializer = new CFMMessageSerializer();
                    CFMJsonSimpleMessage message = serializer.deserialize(entityContents);
                    System.out.println(message.toString());


                    List<CarPojo> list = (new CarSerializer()).deserializeList(message.getData());
                    Iterator<CarPojo> it = list.iterator();
                    while (it.hasNext()) {
                        System.out.println(it.next().toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }
}
