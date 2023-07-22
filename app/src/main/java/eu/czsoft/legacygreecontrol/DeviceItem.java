package eu.czsoft.legacygreecontrol;

import java.io.Serializable;

import eu.czsoft.greesdk.device.Device;

public class DeviceItem implements Serializable {

    public String id = "ID";
    public String name = "Name";
    public Device.Mode mode = Device.Mode.AUTO;
    public int temperature = 0;
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

    public DeviceItem(Device device) {
        updateWithDevice(device);
    }

    public void updateWithDevice(Device device) {
        id = device.getId();
        name = device.getName();
        mode = device.getMode();
        temperature = device.getTemperature();
    }
}
