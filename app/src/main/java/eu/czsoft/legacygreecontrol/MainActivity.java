package eu.czsoft.legacygreecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import eu.czsoft.greesdk.device.DeviceManager;

public class MainActivity extends AppCompatActivity
    implements DeviceItemFragment.OnListFragmentInteractionListener {

    public static String EXTRA_DEVICE_ITEM = "eu.czsoft.greecontrol.DEVICEITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DeviceManager.getInstance().discoverDevices();
            Snackbar.make(view, getString(R.string.device_scan_start_notification), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });

        DeviceManager.getInstance().discoverDevices();
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

    @Override
    public void onListFragmentInteraction(DeviceItem item) {
        Intent intent = new Intent(this, DeviceActivity.class);
        intent.putExtra(EXTRA_DEVICE_ITEM, item);
        startActivity(intent);
    }
}
