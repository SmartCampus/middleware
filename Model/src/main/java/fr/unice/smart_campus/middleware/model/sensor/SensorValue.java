package fr.unice.smart_campus.middleware.model.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The value sent by the sensor to SmartCampus to be process and saved
 */
public class SensorValue implements Serializable{

    private static final long serialVersionUID = 7526472295622776148L;

    public String name;
    public long timestamp;
    public String value;

    /**
     * Default constructor
     */
    public SensorValue() {
    }

    /**
     *
     * @param name a sensor name
     * @param timestamp a sensor value timestamp
     * @param value a sensor value
     */
    public SensorValue(String name, long timestamp, String value) {
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     *
     * @return the sensor name
     */
    @JsonProperty("n")
    public String getName() {
        return name;
    }

    /**
     *
     * @return the sensor value timestamp
     */
    @JsonProperty("t")
    public long getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return the sensor value
     */
    @JsonProperty("v")
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
