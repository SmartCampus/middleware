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

/**
 * The value sent by the sensor to SmartCampus to be process and saved with the value type
 */
public class TypedSensorValue extends SensorValue {

    private  SensorValueType type;

    public TypedSensorValue(String name, long timestamp, String value, SensorValueType type) {
        super(name, timestamp, value);
        this.type = type;
    }

    /**
     *
     * @return the sensor value type
     */
    public SensorValueType getType() {
        return type;
    }

    public void setType(SensorValueType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TypedSensorValue{" +
                super.toString() +
                "type=" + type +
                '}';
    }
}
