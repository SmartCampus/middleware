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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * The value sent by the sensor to SmartCampus to be process and saved
 */
public class SensorValue implements Serializable{

    private static final long serialVersionUID = 7526472295622776148L;

    public String name;
    public long timestamp;
    public String value;

    /**
     * Default constructor
     */
    public SensorValue() {
    }

    /**
     *
     * @param name a sensor name
     * @param timestamp a sensor value timestamp
     * @param value a sensor value
     */
    public SensorValue(String name, long timestamp, String value) {
        this.name = name;
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     *
     * @return the sensor name
     */
    @JsonProperty("n")
    public String getName() {
        return name;
    }

    /**
     *
     * @return the sensor value timestamp
     */
    @JsonProperty("t")
    public long getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return the sensor value
     */
    @JsonProperty("v")
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SensorValue{" +
                "name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", value='" + value + '\'' +
                '}';
    }

}
