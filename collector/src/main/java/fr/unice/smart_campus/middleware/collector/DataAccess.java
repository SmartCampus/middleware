package fr.unice.smart_campus.middleware.collector;

/*
 * #%L
 * collector
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
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigFactory;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

import java.io.IOException;


/**
 * DataAccess class
 * Interface to post sensor values into the message queue
 */
public class DataAccess {
    private static DataAccess instance;
    private ActorRef senderToMessageProcessing;

    /**
     * Get the class instance
     */
    public static synchronized DataAccess getInstance() {
        if (instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }

    /**
     * Private constructor
     */
    private DataAccess() {
        ActorSystem actorSystem = ActorSystem.create("CollectorActorSystem", ConfigFactory.load());
        this.senderToMessageProcessing = actorSystem.actorOf(Props.create(ActorSenderToMessageProcessing.class), "ActorSenderToMessageProcessing");
    }


    /**
     * Post a sensor message to MessageProcessing
     *
     * @param jsonString The JSON string that contains sensor data
     */
    public void postMessage(String jsonString) throws IOException {
        // Extract sensor information
        ObjectMapper mapper = new ObjectMapper();

        SensorValue sensorValue = mapper.readValue(jsonString, SensorValue.class);

        this.senderToMessageProcessing.tell(sensorValue, ActorRef.noSender());
    }
}
