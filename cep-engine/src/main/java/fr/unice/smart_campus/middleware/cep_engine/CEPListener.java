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

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
import fr.unice.smart_campus.middleware.model.config.ParentSensor;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValue;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValueList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Listener of the event from the CEP engine.
 * When it receive a message from the CEP engine, it transfers the message to a remote Akka actor
 */
public abstract class CEPListener implements UpdateListener {
    private ActorSelection actorRef;

    protected Map<String, SensorValueType> parentValueTypeMap;
    protected String script;
    protected String virtualSensorName;

    public CEPListener(ActorSelection actorRef) {
        this.actorRef = actorRef;
    }

    public void setParentValueTypeMap(Map<String, SensorValueType> parentValueTypeMap) {
        this.parentValueTypeMap = parentValueTypeMap;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public void setVirtualSensorName(String virtualSensorName) {
        this.virtualSensorName = virtualSensorName;
    }

    public void update(final EventBean[] newData, EventBean[] oldData) {
        TypedSensorValueList akkaMessage = new TypedSensorValueList();

        List<TypedSensorValue> typedSensorValues = new ArrayList<TypedSensorValue>();
        Map map = (Map) newData[0].getUnderlying();
        for (int i = 0; i < map.size(); i++) {
            CEPEvent event = (CEPEvent) ((EventBean) map.get("a" + i)).getUnderlying();
            System.out.println(event);
            String name = event.getName();
            SensorValueType type = parentValueTypeMap.get(name);
            TypedSensorValue typedSensorValue = new TypedSensorValue(name, event.getTimeStamp(), event.getValue(), type);

            typedSensorValues.add(typedSensorValue);
        }

        akkaMessage.setVirtualSensorName(virtualSensorName);
        akkaMessage.setSensorValues(typedSensorValues);
        akkaMessage.setScript(script);

        System.out.println("Send message to Akka actor : " + akkaMessage);
        this.actorRef.tell(akkaMessage, ActorRef.noSender());
    }
}
