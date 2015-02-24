package sensor;

public class TypedSensorValue extends SensorValue {

    private  SensorValueType type;

    public TypedSensorValue(String name, long timestamp, String value, SensorValueType type) {
        super(name, timestamp, value);
        this.type = type;
    }

    public SensorValueType getType() {
        return type;
    }

    public void setType(SensorValueType type) {
        this.type = type;
    }
}
