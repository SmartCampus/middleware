package sensor;

public class TypedSensorValue extends SensorValue {
    private  TypedSensorValue type;

    public TypedSensorValue(String name, long timestamp, String value, TypedSensorValue type) {
        super(name, timestamp, value);
        this.type = type;
    }

    public TypedSensorValue getType() {
        return type;
    }

    public void setType(TypedSensorValue type) {
        this.type = type;
    }
}
