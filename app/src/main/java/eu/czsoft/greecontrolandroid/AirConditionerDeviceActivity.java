package eu.czsoft.greecontrolandroid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.textfield.TextInputEditText;

import eu.czsoft.greesdk.DeviceManager;
import eu.czsoft.greesdk.DeviceManagerEvent;
import eu.czsoft.greesdk.DeviceManagerEventListener;
import eu.czsoft.greesdk.appliances.AirConditionerAppliance;
import eu.czsoft.greesdk.appliances.airconditioner.FanSpeed;
import eu.czsoft.greesdk.appliances.airconditioner.Mode;
import eu.czsoft.greesdk.appliances.airconditioner.TemperatureUnit;

public class AirConditionerDeviceActivity extends AppCompatActivity {
    private DeviceItem deviceItem;
    private TextView temperatureTextView;
    private AirConditionerAppliance device;
    private DeviceManagerEventListener deviceManagerEventListener;

    public static String EXTRA_FEATURE_HELP = "eu.czsoft.greecontrolandroid.FEATURE_HELP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aircon_device);

        temperatureTextView = findViewById(R.id.temperatureTextView);

        Intent intent = getIntent();
        deviceItem = (DeviceItem) intent.getSerializableExtra(MainActivity.EXTRA_DEVICE_ITEM);

        setTitle(deviceItem.name);

        device = (AirConditionerAppliance) DeviceManager.getInstance().getDevice(deviceItem.id);

        deviceManagerEventListener = event -> {
            if (event == DeviceManagerEvent.DEVICE_STATUS_UPDATED)
                update();
        };

        DeviceManager.getInstance().registerEventListener(deviceManagerEventListener);

        update();
        setupFanSpeedSeekBarChangeListener();
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
        temperatureTextView.setText(String.format("%d", device.getTemperature()));

        int activeColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null);
        int inactiveColor = MaterialColors.getColor(temperatureTextView, R.attr.colorSecondaryTextVariant, Color.BLACK);

        final Mode mode = device.getMode();

        setImageButtonColorFilter(R.id.autoModeButton, mode == Mode.AUTO ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.coolModeButton, mode == Mode.COOL ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.dryModeButton, mode == Mode.DRY ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.fanModeButton, mode == Mode.FAN ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.heatModeButton, mode == Mode.HEAT ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.powerButton, device.isPoweredOn() ? activeColor : inactiveColor);

        setSwitchChecked(R.id.airSwitch, device.isAirModeEnabled());
        setSwitchChecked(R.id.healthSwitch, device.isHealthModeEnabled());
        setSwitchChecked(R.id.xfanSwitch, device.isXfanModeEnabled());
        setSwitchChecked(R.id.sleepSwitch, device.isSleepEnabled());
        setSwitchChecked(R.id.quietSwitch, device.isQuietModeEnabled());
        setSwitchChecked(R.id.turboSwitch, device.isTurboModeEnabled());
        setSwitchChecked(R.id.energySavingSwitch, device.isSavingModeEnabled());
        setSwitchChecked(R.id.lightSwitch, device.isLightEnabled());

        ((SeekBar) findViewById(R.id.fanSpeedSeekBar)).setProgress(device.getFanSpeed().ordinal());
    }

    public void onAirHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.AIR);
    }

    public void onHealthHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.HEALTH);
    }

    public void onDryHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.DRY);
    }

    public void onSleepHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.SLEEP);
    }

    public void onQuietHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.QUIET);
    }

    public void onTurboHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.TURBO);
    }

    public void onSavingHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.SAVING);
    }

    public void onLightHelpButtonClicked(View view) {
        startHelpActivity(AirConditionerDeviceHelpActivity.Feature.LIGHT);
    }

    public void onAutoModeButtonClicked(View view) {
        device.setMode(Mode.AUTO);
    }

    public void onCoolModeButtonClicked(View view) {
        device.setMode(Mode.COOL);
    }

    public void onDryModeButtonClicked(View view) {
        device.setMode(Mode.DRY);
    }

    public void onFanModeButtonClicked(View view) {
        device.setMode(Mode.FAN);
    }

    public void onHeatModeButtonClicked(View view) {
        device.setMode(Mode.HEAT);
    }

    public void onPlusButtonClicked(View view) {
        device.setTemperature(device.getTemperature() + 1, TemperatureUnit.CELSIUS);
    }

    public void onMinusButtonClicked(View view) {
        device.setTemperature(device.getTemperature() - 1, TemperatureUnit.CELSIUS);
    }

    public void onPowerButtonClicked(View view) {
        device.setPoweredOn(!device.isPoweredOn());
    }

    public void onAirSwitchClicked(View view) {
        device.setAirModeEnabled(isSwitchChecked(R.id.airSwitch));
    }

    public void onHealthSwitchClicked(View view) {
        device.setHealthModeEnabled(isSwitchChecked(R.id.healthSwitch));
    }

    public void onXfanSwitchClicked(View view) {
        device.setXfanModeEnabled(isSwitchChecked(R.id.xfanSwitch));
    }

    public void onSleepSwitchClicked(View view) {
        device.setSleepEnabled(isSwitchChecked(R.id.sleepSwitch));
    }

    public void onQuietSwitchClicked(View view) {
        device.setQuietModeEnabled(isSwitchChecked(R.id.quietSwitch));
    }

    public void onTurboSwitchClicked(View view) {
        device.setTurboModeEnabled(isSwitchChecked(R.id.turboSwitch));
    }

    public void onSavingSwitchClicked(View view) {
        device.setSavingModeEnabled(isSwitchChecked(R.id.energySavingSwitch));
    }

    public void onLightSwitchClicked(View view) {
        device.setLightEnabled(isSwitchChecked(R.id.lightSwitch));
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

    private void setupFanSpeedSeekBarChangeListener() {
        ((SeekBar) findViewById(R.id.fanSpeedSeekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // FIXME: int might be used for fan speed instead of enum
                device.setFanSpeed(FanSpeed.values()[seekBar.getProgress()]);
            }
        });
    }
}
