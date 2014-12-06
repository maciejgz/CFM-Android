package pl.mg.cfm.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.mg.cfm.R;
import pl.mg.cfm.account.UserAccountManager;
import pl.mg.cfm.message.CFMJsonSimpleMessage;
import pl.mg.cfm.network.CFMUtilsDictionary;
import pl.mg.cfm.network.HttpClientFactory;
import pl.mg.cfm.pojo.CarPojo;
import pl.mg.cfm.serializer.CFMMessageSerializer;
import pl.mg.cfm.serializer.CarSerializer;

public class CarListActivity extends Activity {

    private String id;
    private String password;

    private List<CarPojo> cars;

    private TextView carListTitle;
    private ListView list;

    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);


        mProgressView = findViewById(R.id.login_progress);

        this.id = getIntent().getStringExtra(UserAccountManager.LOGIN_INTENT_ID);
        this.password = getIntent().getStringExtra(UserAccountManager.PASSWORD_INTENT_ID);


        initElements();
    }

    private void initElements() {
        this.list = (ListView) findViewById(R.id.car_list_view);
        this.carListTitle = (TextView) findViewById(R.id.car_list_title_text_view);


        this.list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GetCarListTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmployeeId;
        private final String mPassword;

        GetCarListTask(String email, String password) {
            mEmployeeId = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            DefaultHttpClient client = null;
            try {
                client = (DefaultHttpClient) HttpClientFactory.getHttpClient();
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(mEmployeeId, mPassword);
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


                    cars = (new CarSerializer()).deserializeList(message.getData());
                    Iterator<CarPojo> it = cars.iterator();
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

            if (success) {
            } else {
            }
        }

        @Override
        protected void onCancelled() {

        }
    }


    public class CarListAdapter extends SimpleAdapter {


        public CarListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            CarHolder carHolder = new CarHolder();
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.car_list_element, null);
                TextView firstLine = (TextView) findViewById(R.id.firstLine);

                carHolder.carId = firstLine;
                v.setTag(carHolder);
            } else {
                carHolder = (CarHolder) v.getTag();
            }

            CarPojo car = cars.get(position);
            carHolder.carId.setText(car.getCarId());
            return v;
        }
    }


    private class CarHolder {
        public TextView carId;
    }
}
