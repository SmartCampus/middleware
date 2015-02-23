package config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 */

public class SensorParams {

    public static final String NAME_COLUMN="name";
    public static final String KIND_COLUMN="kind";
    public static final String FREQUENCY_COLUMN="frequency";
    public   static final String SENSORTYPE_COLUMN="sensorType";
    public static final String SCRIPT_COLUMN="script";
    public static final String PARENTS_COLUMN="parents";


    private String name, kind, script;
    private SensorType sensorType;
    private int frequency;
    private List<String> parentSensors;

    public SensorParams() {
    }

    public SensorParams(DBObject o){
        this.name= (String) o.get(NAME_COLUMN);
        this.kind= (String) o.get(KIND_COLUMN);
        this.frequency=(Integer) o.get(FREQUENCY_COLUMN);
        this.sensorType =SensorType.valueOf((String)o.get(SENSORTYPE_COLUMN));
        this.script=(String) o.get(SCRIPT_COLUMN);
        //this.parentSensors=(List<String>) o.get(PARENTS_COLUMN);

    }
    public SensorParams(String name, String kind, String script, SensorType sensorType, int frequency, List<String> parentSensors) {
        this.name = name;
        this.kind = kind;
        this.script = script;
        this.sensorType = sensorType;
        this.frequency = frequency;
        this.parentSensors = parentSensors;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonProperty
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @JsonProperty
    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    @JsonProperty
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @JsonProperty
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

    public BasicDBObject toDoc(){
        BasicDBObject doc = new BasicDBObject(SensorParams.NAME_COLUMN,name)
                .append(SensorParams.KIND_COLUMN, kind)
                .append(SensorParams.FREQUENCY_COLUMN, frequency)
                .append(SensorParams.SCRIPT_COLUMN, script)
                .append(SensorParams.SENSORTYPE_COLUMN, sensorType.name())
                .append(SensorParams.PARENTS_COLUMN, parentSensors);
        return doc;
    }

}
