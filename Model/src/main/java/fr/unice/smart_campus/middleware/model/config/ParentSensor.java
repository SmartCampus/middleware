package fr.unice.smart_campus.middleware.model.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;

/**
 *
 * Created by clement0210 on 28/02/15.
 * This class represents a model to properly defined the sensors hierarchy
 */
public class ParentSensor {

    private static final String NAME_COLUMN = "name";
    private static final String VALUETYPE_COLUMN = "valueType";
    private SensorValueType valueType;
    private String name;

    /**
     * Default constructor
     */
    public ParentSensor() {
    }

    /**
     *
     * @param name parent sensor name
     * @param valueType parent sensor value type
     */
    public ParentSensor(String name, SensorValueType valueType) {
        this.valueType = valueType;
        this.name = name;
    }

    /**
     * Constructor for deserialize from MongoDb document
     * @param o a MongoDB object
     */
    public ParentSensor(DBObject o){
        this.name= (String) o.get(NAME_COLUMN);
        this.valueType =SensorValueType.valueOf(((String) o.get(VALUETYPE_COLUMN)).toUpperCase());
    }

    /**
     *
     * @return parent sensor value type
     */
    @JsonProperty
    public SensorValueType getValueType() {
        return valueType;
    }

    public void setValueType(SensorValueType valueType) {
        this.valueType = valueType;
    }

    /**
     *
     * @return parent sensor name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ParentSensor{" +
                "valueType=" + valueType +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     *
     * @return the MongoDB object to serialize into document
     */
    public DBObject toDoc(){
        BasicDBObject doc = new BasicDBObject(NAME_COLUMN,name)
                .append(VALUETYPE_COLUMN,valueType.name().toLowerCase());
        return doc;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParentSensor that = (ParentSensor) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
