package sensor;

import java.io.Serializable;

/**
 * The value sent by the sensor to SmartCampus to be process and saved
 */
public class SensorValue implements Serializable{

    private static final long serialVersionUID = 7526472295622776148L;

    public String name;
    public long timestamp;
    public String value;

    public SensorValue() {
    }

    public SensorValue(String name, long timestamp, String value) {
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SensorValue{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", value='" + value + '\'' +
                '}';
    }
}
