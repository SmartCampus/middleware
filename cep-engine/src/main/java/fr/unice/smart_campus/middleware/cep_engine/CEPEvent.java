package fr.unice.smart_campus.middleware.cep_engine;

/*
 * #%L
 * cep-engine
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

public abstract class CEPEvent implements Serializable {
    public final long serialVersionUID = 1234567891922321327L;

    protected String value;
    protected String name;
    protected long timeStamp;

    public CEPEvent(){}

    public CEPEvent(String value, String name, long timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "{" +
                "\"v\":\"" + value + "\"" +
                ", \"n\":\"" + name + "\"" +
                ", \"t\":" + timeStamp + "" +
                '}';
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
