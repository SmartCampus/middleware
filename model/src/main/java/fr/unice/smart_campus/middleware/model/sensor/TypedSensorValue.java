package fr.unice.smart_campus.middleware.model.sensor;

/**
 * The value sent by the sensor to SmartCampus to be process and saved with the value type
 */
public class TypedSensorValue extends SensorValue {

    private  SensorValueType type;

    public TypedSensorValue(String name, long timestamp, String value, SensorValueType type) {
        super(name, timestamp, value);
        this.type = type;
    }

    /**
     *
     * @return the sensor value type
     */
    public SensorValueType getType() {
        return type;
    }

    public void setType(SensorValueType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypedSensorValue{" +
                super.toString() +
                "type=" + type +
                '}';
    }
}
