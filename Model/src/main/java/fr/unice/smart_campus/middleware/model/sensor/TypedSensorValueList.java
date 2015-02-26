package fr.unice.smart_campus.middleware.model.sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypedSensorValueList implements Serializable {
    private static final long serialVersionUID = 7526479295622776148L;

    private List<TypedSensorValue> sensorValues;

    public TypedSensorValueList() {
        this.sensorValues = new ArrayList<TypedSensorValue>();
    }

    public List<TypedSensorValue> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(List<TypedSensorValue> sensorValues) {
        this.sensorValues = sensorValues;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
