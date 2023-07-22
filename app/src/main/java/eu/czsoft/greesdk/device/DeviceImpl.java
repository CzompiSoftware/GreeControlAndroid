package eu.czsoft.greesdk.device;

import android.util.Log;

import java.util.Map;

import eu.czsoft.greesdk.packs.DatPack;
import eu.czsoft.greesdk.packs.DevicePack;
import eu.czsoft.greesdk.packs.ResultPack;
import eu.czsoft.greesdk.Utils;

class DeviceImpl implements Device {
    private final String deviceId;
    private final DeviceManager deviceManager;
    private final String logTag;

    public enum Parameter {
        POWER               ("Pow"),
        MODE                ("Mod"),
        TEMPERATURE         ("SetTem"),
        TEMPERATURE_UNIT    ("TemUn"),
        FAN_SPEED           ("WdSpd"),
        AIR_MODE            ("Air"),
        XFAN_MODE           ("Blo"),
        HEALTH_MODE         ("Health"),
        SLEEP_MODE          ("SwhSlp"),
        QUIET_MODE          ("Quiet"),
        TURBO_MODE          ("Tur"),
        SAVING_MODE         ("SvSt"),
        LIGHT               ("Lig"),
        HORIZONTAL_SWING    ("SwingLfRig"),
        VERTICAL_SWING      ("SwUpDn"),
        STHT_MODE           ("StHt"),
        HEAT_COOL_TYPE      ("HeatCoolType"),
        TEM_REC_MODE        ("TemRec")
        ;

        private final String param;

        Parameter(final String param) {
            this.param = param;
        }

        @Override
        public String toString() {
            return param;
        }
    }

    private String name = "";
    private Mode mode = Mode.AUTO;
    private FanSpeed fanSpeed = FanSpeed.AUTO;
    private int temperature = 0;
    private TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;
    private boolean poweredOn;
    private boolean lightEnabled;
    private boolean quietModeEnabled;
    private boolean turboModeEnabled;
    private boolean healthModeEnabled;
    private boolean airModeEnabled;
    private boolean xFanModeEnabled;
    private boolean savingModeEnabled;
    private boolean sleepModeEnabled;
    private VerticalSwingMode verticalSwingMode = VerticalSwingMode.DEFAULT;

    public DeviceImpl(String deviceId, DeviceManager deviceManager) {
        this.deviceId = deviceId;
        this.deviceManager = deviceManager;
        this.logTag = String.format("DeviceImpl(%s)", deviceId);

        Log.i(this.logTag, "Created");
    }

    public void updateWithDatPack(DatPack pack) {
        updateParameters(Utils.zip(pack.keys, pack.values));
    }

    public void updateWithResultPack(ResultPack pack) {
        updateParameters(Utils.zip(pack.keys, pack.values));
    }

    public void updateWithDevicePack(DevicePack pack) {
        Log.d(logTag, "Updating name: " + pack.name);
        name = pack.name;
    }

    @Override
    public String getId() {
        return deviceId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void setMode(Mode mode) {
        setParameter(Parameter.MODE, mode.ordinal());
    }

    @Override
    public FanSpeed getFanSpeed() {
        return fanSpeed;
    }

    @Override
    public void setFanSpeed(FanSpeed fanSpeed) {
        setParameter(Parameter.FAN_SPEED, fanSpeed.ordinal());
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(int value, TemperatureUnit unit) {
        setParameters(
                new Parameter[] { Parameter.TEMPERATURE, Parameter.TEMPERATURE_UNIT },
                new Integer[] { value, unit.ordinal() }
        );
    }

    @Override
    public boolean isPoweredOn() {
        return poweredOn;
    }

    @Override
    public void setPoweredOn(boolean poweredOn) {
        setParameter(Parameter.POWER, poweredOn ? 1 : 0);
    }

    @Override
    public boolean isLightEnabled() {
        return lightEnabled;
    }

    @Override
    public void setLightEnabled(boolean enabled) {
        setParameter(Parameter.LIGHT, enabled ? 1 : 0);
    }

    @Override
    public boolean isQuietModeEnabled() {
        return quietModeEnabled;
    }

    @Override
    public void setQuietModeEnabled(boolean enabled) {
        setParameter(Parameter.QUIET_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isTurboModeEnabled() {
        return turboModeEnabled;
    }

    @Override
    public void setTurboModeEnabled(boolean enabled) {
        setParameter(Parameter.TURBO_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isHealthModeEnabled() {
        return healthModeEnabled;
    }

    @Override
    public void setHealthModeEnabled(boolean enabled) {
        setParameter(Parameter.HEALTH_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isAirModeEnabled() {
        return airModeEnabled;
    }

    @Override
    public void setAirModeEnabled(boolean enabled) {
        setParameter(Parameter.AIR_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isXfanModeEnabled() {
        return xFanModeEnabled;
    }

    @Override
    public void setXfanModeEnabled(boolean enabled) {
        setParameter(Parameter.XFAN_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isSavingModeEnabled() {
        return savingModeEnabled;
    }

    @Override
    public void setSavingModeEnabled(boolean enabled) {
        setParameter(Parameter.SAVING_MODE, enabled ? 1 : 0);
    }

    @Override
    public boolean isSleepModeEnabled() {
        return sleepModeEnabled;
    }

    @Override
    public void setSleepModeEnabled(boolean enabled) {
        setParameter(Parameter.SLEEP_MODE, enabled ? 1 : 0);
    }

    @Override
    public VerticalSwingMode getVerticalSwingMode() {
        return verticalSwingMode;
    }

    @Override
    public void setVerticalSwingMode(VerticalSwingMode mode) {
        setParameter(Parameter.VERTICAL_SWING, mode.ordinal());
    }

    @Override
    public int getParameter(String name) {
        return 0;
    }

    @Override
    public void setParameter(String name, int value) {
        deviceManager.setParameter(this, name, value);
    }

    @Override
    public void setWifiSsidPassword(String ssid, String psw) {
        deviceManager.setWifi(ssid, psw);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceImpl device = (DeviceImpl) o;

        return deviceId.equals(device.deviceId);
    }

    @Override
    public int hashCode() {
        return deviceId.hashCode();
    }

    private void setParameter(Parameter parameter, int value) {
        setParameter(parameter.toString(), value);
    }

    private void setParameters(Parameter[] parameters, Integer[] values) {
        String[] names = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            names[i] = parameters[i].toString();
        }

        deviceManager.setParameters(this, Utils.zip(names, values));
    }

    private void updateParameters(Map<String, Integer> p) {
        Log.d(logTag, "Updating parameters: " + p);

        mode = getEnuparameter(p, Parameter.MODE, Mode.values(), mode);
        fanSpeed = getEnuparameter(p, Parameter.FAN_SPEED, FanSpeed.values(), fanSpeed);
        temperature = getOrdinalParameter(p, Parameter.TEMPERATURE, temperature);
        temperatureUnit = getEnuparameter(p, Parameter.TEMPERATURE_UNIT, TemperatureUnit.values(), temperatureUnit);
        poweredOn = getBooleanParameter(p, Parameter.POWER, poweredOn);
        lightEnabled = getBooleanParameter(p, Parameter.LIGHT, lightEnabled);
        quietModeEnabled = getBooleanParameter(p, Parameter.QUIET_MODE, quietModeEnabled);
        turboModeEnabled = getBooleanParameter(p, Parameter.TURBO_MODE, turboModeEnabled);
        healthModeEnabled = getBooleanParameter(p, Parameter.HEALTH_MODE, healthModeEnabled);
        airModeEnabled = getBooleanParameter(p, Parameter.AIR_MODE, airModeEnabled);
        xFanModeEnabled = getBooleanParameter(p, Parameter.XFAN_MODE, xFanModeEnabled);
        savingModeEnabled = getBooleanParameter(p, Parameter.SAVING_MODE, savingModeEnabled);
        sleepModeEnabled = getBooleanParameter(p, Parameter.SLEEP_MODE, sleepModeEnabled);
        verticalSwingMode = getEnuparameter(p, Parameter.VERTICAL_SWING, VerticalSwingMode.values(), verticalSwingMode);
    }

    private static <E> E getEnuparameter(Map<String, Integer> m, Parameter p, E[] values, E def) {
        if (m.containsKey(p.toString())) {
            int ordinal = m.get(p.toString());
            if (ordinal >= 0 && ordinal < values.length) {
                return values[ordinal];
            }
        }

        return def;
    }

    private static int getOrdinalParameter(Map<String, Integer> m, Parameter p, int def) {
        if (m.containsKey(p.toString()))
            return m.get(p.toString());
        return def;
    }

    private static boolean getBooleanParameter(Map<String, Integer> m, Parameter p, boolean def) {
        return getOrdinalParameter(m, p, def ? 1 : 0) == 1;
    }

    @Override
    public String toString() {
        return "DeviceImpl{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceManager=" + deviceManager +
                ", logTag='" + logTag + '\'' +
                ", name='" + name + '\'' +
                ", mode=" + mode +
                ", fanSpeed=" + fanSpeed +
                ", temperature=" + temperature +
                ", temperatureUnit=" + temperatureUnit +
                ", poweredOn=" + poweredOn +
                ", lightEnabled=" + lightEnabled +
                ", quietModeEnabled=" + quietModeEnabled +
                ", turboModeEnabled=" + turboModeEnabled +
                ", healthModeEnabled=" + healthModeEnabled +
                ", airModeEnabled=" + airModeEnabled +
                ", xFanModeEnabled=" + xFanModeEnabled +
                ", savingModeEnabled=" + savingModeEnabled +
                ", sleepModeEnabled=" + sleepModeEnabled +
                ", verticalSwingMode=" + verticalSwingMode +
                '}';
    }
}
