package eu.czsoft.greecontrolandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import eu.czsoft.greesdk.DeviceManager;
import eu.czsoft.greesdk.DeviceManagerEvent;
import eu.czsoft.greesdk.DeviceManagerEventListener;
import eu.czsoft.greesdk.appliances.Appliance;

public class DeviceActivity extends AppCompatActivity {
    private DeviceItem deviceItem;

    //private TextView registerComponentsHere;
    private Appliance device;
    private DeviceManagerEventListener deviceManagerEventListener;

    public static String EXTRA_FEATURE_HELP = "eu.czsoft.greecontrolandroid.FEATURE_HELP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        //registerComponentsHere = findViewById(R.id.component_id);

        Intent intent = getIntent();
        deviceItem = (DeviceItem) intent.getSerializableExtra(MainActivity.EXTRA_DEVICE_ITEM);

        setTitle(deviceItem.name);

        device = DeviceManager.getInstance().getDevice(deviceItem.id);

        deviceManagerEventListener = event -> {
            if (event == DeviceManagerEvent.DEVICE_STATUS_UPDATED)
                update();
        };

        DeviceManager.getInstance().registerEventListener(deviceManagerEventListener);

        update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceManager.getInstance().unregisterEventListener(deviceManagerEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
        return true;
    }

    public void update() {
        //temperatureTextView.setText(String.format("%d", device.getTemperature()));

        // int activeColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);
        // int inactiveColor = MaterialColors.getColor(temperatureTextView, R.attr.colorSecondaryTextVariant, Color.BLACK);

        // final Mode mode = device.getMode();

        // setImageButtonColorFilter(R.id.autoModeButton, mode == Mode.AUTO ? activeColor : inactiveColor);

        //setSwitchChecked(R.id.airSwitch, device.isAirModeEnabled());
    }

    private void startHelpActivity(AirConditionerDeviceHelpActivity.Feature feature) {
        Intent intent = new Intent(this, AirConditionerDeviceHelpActivity.class);
        intent.putExtra(EXTRA_FEATURE_HELP, feature);
        startActivity(intent);
    }

    private void setImageButtonColorFilter(int id, int color) {
        View view = findViewById(id);
        if (view instanceof ImageButton) {
            ((ImageButton) view).setColorFilter(color);
        }
    }

    private void setSwitchChecked(int id, boolean checked) {
        View view = findViewById(id);
        if (view instanceof Switch) {
            ((Switch) view).setChecked(checked);
        }
    }

    private boolean isSwitchChecked(int id) {
        View view = findViewById(id);
        if (view instanceof Switch) {
            return ((Switch) view).isChecked();
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.wifi_settings){
            final View setWiFiDetailsView = LayoutInflater.from(this).inflate(R.layout.set_wifi_details, null);
            new AlertDialog.Builder(this).setView(setWiFiDetailsView)
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        TextInputEditText name = setWiFiDetailsView.findViewById(R.id.name);
                        TextInputEditText password = setWiFiDetailsView.findViewById(R.id.password);
                        Log.d("Wi-Fi setup","SSID: '" + name.getText().toString() + "' Password: ***");
                        device.setWiFiDetails(name.getText().toString(), password.getText().toString());
                    }).create().show();
        }
        return true;
    }

}
