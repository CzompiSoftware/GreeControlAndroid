package eu.czsoft.legacygreecontrol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.color.MaterialColors;
import eu.czsoft.legacygreecontrol.Gree.Device.Device;
import eu.czsoft.legacygreecontrol.Gree.Device.DeviceManager;
import eu.czsoft.legacygreecontrol.Gree.Device.DeviceManagerEventListener;

/*
 * This file is part of GreeRemoteAndroid.
 *
 * GreeRemoteAndroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GreeRemoteAndroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GreeRemoteAndroid. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by tomikaa87 <https://github.com/tomikaa87> on 2017-10-23.
 */

public class DeviceActivity extends AppCompatActivity {
    private DeviceItem deviceItem;
    private TextView temperatureTextView;
    private Device device;
    private DeviceManagerEventListener deviceManagerEventListener;

    public static String EXTRA_FEATURE_HELP = "eu.czsoft.greecontrol.FEATURE_HELP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);

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
