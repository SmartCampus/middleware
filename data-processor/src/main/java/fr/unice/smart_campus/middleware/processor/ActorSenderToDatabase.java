package fr.unice.smart_campus.middleware.processor;

/*
 * #%L
 * data-processor
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

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.database.SensorsDataOutputDataAccess;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

/**
 * Actor to send sensor value to database
 */
public class ActorSenderToDatabase extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private SensorsDataOutputDataAccess sensorsDataOutputDataAccess;

    public ActorSenderToDatabase() throws Exception {
        this.sensorsDataOutputDataAccess = new SensorsDataOutputDataAccess();
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.sendEventToDatabase((SensorValue) message);
        }
    }

    /**
     * Creation of the actor that has to send an event
     * to the CEP Engine to allowed the process the data.
     *
     * @param messageToSend
     */
    private void sendEventToDatabase(SensorValue messageToSend) {
        this.loggingAdapter.info("Save message in database : " + messageToSend);
        // Save sensor data into the database
        sensorsDataOutputDataAccess.saveSensorData(messageToSend.getName(), String.valueOf(messageToSend.getTimestamp()), messageToSend.getValue());
    }
}
