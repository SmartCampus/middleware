package fr.unice.smart_campus.middleware.model.sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the model of object between CEP and Virtual Message Computing
 */
public class TypedSensorValueList implements Serializable {
    private static final long serialVersionUID = 7526479295622776148L;
    private String script;
    private String virtualSensorName;
    private List<TypedSensorValue> sensorValues;

    /**
     * Constructor of TypedSensorValueList
     * Construct an empty object
     */
    public TypedSensorValueList() {
        this.sensorValues = new ArrayList<TypedSensorValue>();
    }

    /**
     * Constructor of TypedSensorValueList
     * Construct the object from the name and the script
     * @param script the script to create the value of the virtual sensor named virtualSensorName
     * @param virtualSensorName the name of the virtual sensor that have to be compute
     */
    public TypedSensorValueList(String script, String virtualSensorName) {
        this.script = script;
        this.virtualSensorName = virtualSensorName;
        this.sensorValues = new ArrayList<TypedSensorValue>();
    }

    /**
     * Constructor of TypedSensorValueList
     * Construct the object from the name and the script
     * @param script the script to create the value of the virtual sensor named virtualSensorName
     * @param virtualSensorName the name of the virtual sensor that have to be compute
     * @param sensorValues the list of the parents of the virtual sensor virtualSensorName.
     */
    public TypedSensorValueList(String script, String virtualSensorName, List<TypedSensorValue> sensorValues) {
        this.script = script;
        this.virtualSensorName = virtualSensorName;
        this.sensorValues = sensorValues;
    }

    public List<TypedSensorValue> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(List<TypedSensorValue> sensorValues) {
        this.sensorValues = sensorValues;
    }


    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getVirtualSensorName() {
        return virtualSensorName;
    }

    public void setVirtualSensorName(String virtualSensorName) {
        this.virtualSensorName = virtualSensorName;
    }

    @Override
    public String toString() {
        return "TypedSensorValueList{" +
                "script='" + script + '\'' +
                ", virtualSensorName='" + virtualSensorName + '\'' +
                ", sensorValues=" + sensorValues +
                '}';
    }
}
