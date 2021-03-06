package pl.mg.cfm.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.mg.cfm.R;
import pl.mg.cfm.account.UserAccountManager;


public class MainActivity extends Activity {


    private static final String TAG = MainActivity.class.getCanonicalName();


    private String id;
    private String password;

    private TextView welcomeTextView;
    private Button carListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.id = getIntent().getStringExtra(UserAccountManager.LOGIN_INTENT_ID);
        this.password = getIntent().getStringExtra(UserAccountManager.PASSWORD_INTENT_ID);

        initObjects();
    }

    private void initObjects() {
        this.welcomeTextView = (TextView) findViewById(R.id.main_menu_text_view);
        this.carListButton = (Button) findViewById(R.id.car_list_button);

        this.carListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarListActivity();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void openCarListActivity() {
        Intent carListActivityIntent = new Intent(this, CarListActivity.class);
        carListActivityIntent.putExtra(UserAccountManager.LOGIN_INTENT_ID, this.id);
        carListActivityIntent.putExtra(UserAccountManager.PASSWORD_INTENT_ID, this.password);
        startActivity(carListActivityIntent);
    }
}
