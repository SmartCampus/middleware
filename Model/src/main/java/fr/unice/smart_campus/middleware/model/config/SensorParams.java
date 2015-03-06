package fr.unice.smart_campus.middleware.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 * This class represents a model to properly defined a sensor parameters
 */

public class SensorParams {

    public static final String NAME_COLUMN="name";
    public static final String KIND_COLUMN="kind";
    public static final String VALUETYPE_COLUMN="valueType";
    public static final String FREQUENCY_COLUMN="frequency";
    public static final String SENSORTYPE_COLUMN="sensorType";
    public static final String SCRIPT_COLUMN="script";
    public static final String PARENTS_COLUMN="parents";


    private String name, kind, script;
    private SensorType sensorType;
    private SensorValueType valueType;
    private double frequency;
    private List<ParentSensor> parentSensors;


    /**
     * Default constructor
     */
    public SensorParams() {
    }

    /**
     * Constructor for deserialize from MongoDb document
     * @param o a MongoDB object
     */
    public SensorParams(DBObject o){
        this.name= (String) o.get(NAME_COLUMN);
        this.kind= (String) o.get(KIND_COLUMN);
        this.frequency=(Double) o.get(FREQUENCY_COLUMN);
        this.sensorType =SensorType.valueOf(((String)o.get(SENSORTYPE_COLUMN)).toUpperCase());
        this.valueType =SensorValueType.valueOf(((String) o.get(VALUETYPE_COLUMN)).toUpperCase());
        this.script=(String) o.get(SCRIPT_COLUMN);
        BasicDBList list=(BasicDBList) o.get(PARENTS_COLUMN);
        this.parentSensors=new ArrayList<ParentSensor>();
        if(list!=null) {
            for (Object obj : list) {
                parentSensors.add(new ParentSensor((DBObject) obj));
            }
        }
    }


    /**
     *
     * @param name a sensor name
     * @param kind a sensor kind (temperature, humidity, ...)
     * @param script a sensor script to compute the value from parents
     * @param sensorType a sensor type
     * @param valueType a sensor value type
     * @param frequency a frequency
     * @param parentSensors the parents sensors
     */
    public SensorParams(String name, String kind, String script, SensorType sensorType, SensorValueType valueType, double frequency, List<ParentSensor> parentSensors) {

        this.name = name;
        this.kind = kind;
        this.script = script;
        this.sensorType = sensorType;
        this.valueType = valueType;
        this.frequency = frequency;
        this.parentSensors = parentSensors;
    }

    /**
     *
     * @return the sensor name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the sensor kind
     */
    @JsonProperty
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     *
     * @return the sensor script
     */
    @JsonProperty
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    /**
     *
     * @return the sensor type
     */
    @JsonProperty
    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    /**
     *
     * @return the sensor frequency
     */
    @JsonProperty
    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    /**
     *
     * @return the parents sensors
     */
    @JsonProperty("parents")
    @JsonDeserialize(as=List.class, contentAs=ParentSensor.class)
    public List<ParentSensor> getParentSensors() {
        return parentSensors;
    }

    public void setParentSensors(List<ParentSensor> parentSensors) {
        this.parentSensors = parentSensors;
    }

    /**
     *
     * @return the sensor value type
     */
    @JsonProperty
    public SensorValueType getValueType() {
        return valueType;
    }

    public void setValueType(SensorValueType valueType) {
        this.valueType = valueType;
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

    /**
     *
     * @return the MongoDB object to serialize into document
     */
    public BasicDBObject toDoc(){
        BasicDBList parents=new BasicDBList();
        if(parentSensors!=null){

            for(ParentSensor sensor: parentSensors){
                parents.add(sensor.toDoc());

            }
        }
        BasicDBObject doc = new BasicDBObject(SensorParams.NAME_COLUMN,name)
                .append(SensorParams.KIND_COLUMN, kind)
                .append(SensorParams.FREQUENCY_COLUMN, frequency)
                .append(SensorParams.SCRIPT_COLUMN, script)
                .append(SensorParams.SENSORTYPE_COLUMN, sensorType.name().toLowerCase())
                .append(SensorParams.VALUETYPE_COLUMN, valueType.name().toLowerCase())
                .append(SensorParams.PARENTS_COLUMN, parents);
        return doc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorParams that = (SensorParams) o;

        if (!name.equals(that.name)) return false;

        return true;
    }
}
