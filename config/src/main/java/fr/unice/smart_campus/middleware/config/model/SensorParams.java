package fr.unice.smart_campus.middleware.config.model;

import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 */
public class SensorParams {

    private String name, kind, script;
    private SensorType sensorType;
    private int frequency;
    private List<String> parentSensors;

    public SensorParams() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<String> getParentSensors() {
        return parentSensors;
    }

    public void setParentSensors(List<String> parentSensors) {
        this.parentSensors = parentSensors;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\" :'" + name + '\'' +
                ", \"kind\" :'" + kind + '\'' +
                ", \"script\" :'" + script + '\'' +
                ", \"sensorType\" :" + sensorType +
                ", \"frequency\" :" + frequency +
                ", \"parentSensors\" :" + parentSensors +
                '}';
    }
}
