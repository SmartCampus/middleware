package fr.unice.smart_campus.middleware.model.sensor;

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
