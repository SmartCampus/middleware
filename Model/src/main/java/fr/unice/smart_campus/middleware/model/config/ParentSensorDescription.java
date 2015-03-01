package fr.unice.smart_campus.middleware.model.config;

import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;

public class ParentSensorDescription {
    private String name;
    private SensorValueType valueType;

    public ParentSensorDescription() {
    }

    public ParentSensorDescription(String name, SensorValueType valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorValueType getValueType() {
        return valueType;
    }

    public void setValueType(SensorValueType valueType) {
        this.valueType = valueType;
    }
}
