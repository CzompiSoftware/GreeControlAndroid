package eu.czsoft.greecontrolandroid;

import java.io.Serializable;

import eu.czsoft.greesdk.appliances.AirConditionerAppliance;
import eu.czsoft.greesdk.appliances.Appliance;
import eu.czsoft.greesdk.appliances.ApplianceType;
import eu.czsoft.greesdk.appliances.airconditioner.Mode;

public class DeviceItem implements Serializable {

    public String id = "ID";
    public String name = "Name";
    public Mode mode = Mode.AUTO;
    public int temperature = 0;
    public ApplianceType type = ApplianceType.UNKNOWN;
    public RoomType roomType = RoomType.NONE;

    public enum RoomType {
        NONE,
        LIVING_ROOM,
        BEDROOM,
        KITCHEN,
        DINING_ROOM,
        BATHROOM,
        OFFICE
    }

    public DeviceItem() {}

    public DeviceItem(Appliance device) {
        updateWithDevice((AirConditionerAppliance)device);
    }

    public void updateWithDevice(AirConditionerAppliance device) {
        id = device.getId();
        name = device.getName();
        type = device.getType();
        mode = device.getMode();
        temperature = device.getTemperature();
    }
}
