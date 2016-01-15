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

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;

/**
 * Actor to receive sensor value from collector
 */
public class ActorReceiverFromCollector extends UntypedActor {
    private LoggingAdapter loggingAdapter;
    private ActorSelection cepSender;
    private ActorSelection databaseSender;

    public ActorReceiverFromCollector() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        cepSender = this.getContext().actorSelection("akka://MessageProcessingActorSystem/user/ActorSenderToCEP");
        databaseSender = this.getContext().actorSelection("akka://MessageProcessingActorSystem/user/ActorSenderToDatabase");
    }

    /**
     * Method to handle new message
     * @param message a message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SensorValue) {
            databaseSender.tell(message, this.sender());
            cepSender.tell(message, this.sender());
        }
    }
}
