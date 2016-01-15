package fr.unice.smart_campus.middleware.model.config;

/*
 * #%L
 * model
 * %%
 * Copyright (C) 2015 - 2016 Université de Nice Sophia-Antipolis (UNS) - Centre National de la Recherche Scientifique (CNRS)
 * %%
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Université de Nice Sophia-Antipolis (UNS) -
 * Centre National de la Recherche Scientifique (CNRS)
 * Copyright © 2015 UNS, CNRS
 * 
 * 
 *   Authors: SmartCampus Team - http://smartcampus.github.io/sc-contacts/
 * 
 *   Architects: Sébastien Mosser – Laboratoire I3S – mosser@i3s.unice.fr
 *               Michel Riveill - Laboratoire I3S - riveill@i3s.unice.fr
 * #L%
 */

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
