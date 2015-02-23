package sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorValues implements Serializable {
    private static final long serialVersionUID = 7526479295622776148L;

    private List<SensorValue> sensorValues;

    public SensorValues() {
        this.sensorValues = new ArrayList<SensorValue>();
    }

    public List<SensorValue> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(List<SensorValue> sensorValues) {
        this.sensorValues = sensorValues;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
