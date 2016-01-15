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

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

public class CEPInterfaceActor extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private CEPEngine cepRT;

    public CEPInterfaceActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }

    public CEPInterfaceActor(CEPEngine cepRT) {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        this.cepRT = cepRT;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            this.loggingAdapter.info(message.toString());
            SensorValue sensorValue = (SensorValue) message;

            String name = sensorValue.getName();
            String value = sensorValue.getValue();
            long timestamp = sensorValue.getTimestamp();

            Class<?> cepEventClass = Class.forName(name);

            CEPEvent event = (CEPEvent) cepEventClass.newInstance();

            event.setName(name);
            event.setValue(value);
            event.setTimeStamp(timestamp);

            cepRT.sendEvent(event);
        }
    }
}
