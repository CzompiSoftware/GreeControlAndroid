package eu.czsoft.legacygreecontrol;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import eu.czsoft.greesdk.device.Device;
import eu.czsoft.greesdk.device.DeviceManager;
import eu.czsoft.greesdk.device.DeviceManagerEventListener;

public class DeviceActivity extends AppCompatActivity {
    private DeviceItem deviceItem;
    private TextView temperatureTextView;
    private Device device;
    private DeviceManagerEventListener deviceManagerEventListener;

    public static String EXTRA_FEATURE_HELP = "eu.czsoft.legacygreecontrol.FEATURE_HELP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        temperatureTextView = findViewById(R.id.temperatureTextView);

        Intent intent = getIntent();
        deviceItem = (DeviceItem) intent.getSerializableExtra(MainActivity.EXTRA_DEVICE_ITEM);

        setTitle(deviceItem.name);

        device = DeviceManager.getInstance().getDevice(deviceItem.id);

        deviceManagerEventListener = event -> {
            if (event == DeviceManagerEventListener.Event.DEVICE_STATUS_UPDATED)
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

        final Device.Mode mode = device.getMode();

        setImageButtonColorFilter(R.id.autoModeButton, mode == Device.Mode.AUTO ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.coolModeButton, mode == Device.Mode.COOL ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.dryModeButton, mode == Device.Mode.DRY ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.fanModeButton, mode == Device.Mode.FAN ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.heatModeButton, mode == Device.Mode.HEAT ? activeColor : inactiveColor);
        setImageButtonColorFilter(R.id.powerButton, device.isPoweredOn() ? activeColor : inactiveColor);

        setSwitchChecked(R.id.airSwitch, device.isAirModeEnabled());
        setSwitchChecked(R.id.healthSwitch, device.isHealthModeEnabled());
        setSwitchChecked(R.id.xfanSwitch, device.isXfanModeEnabled());
        setSwitchChecked(R.id.sleepSwitch, device.isSleepModeEnabled());
        setSwitchChecked(R.id.quietSwitch, device.isQuietModeEnabled());
        setSwitchChecked(R.id.turboSwitch, device.isTurboModeEnabled());
        setSwitchChecked(R.id.energySavingSwitch, device.isSavingModeEnabled());
        setSwitchChecked(R.id.lightSwitch, device.isLightEnabled());

        ((SeekBar) findViewById(R.id.fanSpeedSeekBar)).setProgress(device.getFanSpeed().ordinal());
    }

    public void onAirHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.AIR);
    }

    public void onHealthHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.HEALTH);
    }

    public void onDryHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.DRY);
    }

    public void onSleepHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.SLEEP);
    }

    public void onQuietHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.QUIET);
    }

    public void onTurboHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.TURBO);
    }

    public void onSavingHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.SAVING);
    }

    public void onLightHelpButtonClicked(View view) {
        startHelpActivity(DeviceHelpActivity.Feature.LIGHT);
    }

    public void onAutoModeButtonClicked(View view) {
        device.setMode(Device.Mode.AUTO);
    }

    public void onCoolModeButtonClicked(View view) {
        device.setMode(Device.Mode.COOL);
    }

    public void onDryModeButtonClicked(View view) {
        device.setMode(Device.Mode.DRY);
    }

    public void onFanModeButtonClicked(View view) {
        device.setMode(Device.Mode.FAN);
    }

    public void onHeatModeButtonClicked(View view) {
        device.setMode(Device.Mode.HEAT);
    }

    public void onPlusButtonClicked(View view) {
        device.setTemperature(device.getTemperature() + 1, Device.TemperatureUnit.CELSIUS);
    }

    public void onMinusButtonClicked(View view) {
        device.setTemperature(device.getTemperature() - 1, Device.TemperatureUnit.CELSIUS);
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
        device.setSleepModeEnabled(isSwitchChecked(R.id.sleepSwitch));
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

    private void startHelpActivity(DeviceHelpActivity.Feature feature) {
        Intent intent = new Intent(this, DeviceHelpActivity.class);
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
            final View usernamePasswordView = LayoutInflater.from(this).inflate(R.layout.set_wifi_details, null);
            new AlertDialog.Builder(this).setView(usernamePasswordView)
                    .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                        TextInputEditText name = usernamePasswordView.findViewById(R.id.name);
                        TextInputEditText password = usernamePasswordView.findViewById(R.id.password);
                        Log.d("Wi-Fi Settings",name.getText().toString() + " ***");
                        device.setWifiSsidPassword(name.getText().toString(), password.getText().toString());
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
                device.setFanSpeed(Device.FanSpeed.values()[seekBar.getProgress()]);
            }
        });
    }
}
