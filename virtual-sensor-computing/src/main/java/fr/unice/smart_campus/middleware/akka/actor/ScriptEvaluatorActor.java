package fr.unice.smart_campus.middleware.akka.actor;

/*
 * #%L
 * virtual-sensor-computing
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
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValue;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValueList;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Akka actor to compute the virtual sensor value
 */
public class ScriptEvaluatorActor extends UntypedActor {

    private static Map<String, Script> groovyScriptMap;

    private LoggingAdapter loggingAdapter;

    /**
     * Default Constructor
     */
    public ScriptEvaluatorActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        this.groovyScriptMap = new HashMap<String, Script>();
    }

    /**
     * Method to handle new message
     * @param message a message
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TypedSensorValueList) {
            TypedSensorValueList sensors = (TypedSensorValueList) message;

            this.loggingAdapter.info(message.toString());

            SensorValue response = evaluateScript(sensors);

            ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://MessageProcessingActorSystem@172.31.39.206:2554/user/ActorReceiverFromCollector");
            actorSelection.tell(response, this.sender());
        }
    }

    /**
     * Method to evaluate a virtual sensor value
     * @param typedSensorValueList
     * @return a sensor value
     * @throws Exception
     */
    protected SensorValue evaluateScript(TypedSensorValueList typedSensorValueList) throws Exception {
        SensorValue res = new SensorValue();
        List<TypedSensorValue> sensors = typedSensorValueList.getSensorValues();

        res.name = typedSensorValueList.getVirtualSensorName();
        res.timestamp = getMaxTimestamp(sensors);

        String script = typedSensorValueList.getScript();

        loggingAdapter.debug(script);

        Script groovyScript = this.groovyScriptMap.get(script);
        Binding binding = new Binding();

        if (groovyScript == null) {
            GroovyShell shell = new GroovyShell();
            groovyScript = shell.parse(script);
            this.groovyScriptMap.put(script, groovyScript);
        }

//        GroovyShell shell = new GroovyShell();

        //Set variable for each sensor
        for (TypedSensorValue s : sensors) {
            Object o = getValue(s.getValue(), s.getType());
            loggingAdapter.debug("ClassType : " + o.getClass());
            loggingAdapter.debug("getValue result " + o);
            binding.setVariable(s.getName(), o);
//            shell.setVariable(s.getName(), o);
        }

        loggingAdapter.debug("Evaluate !");

//        evaluate script
        groovyScript.setBinding(binding);
        String resScript = groovyScript.run().toString();
//        String resScript = shell.evaluate(script).toString();
        res.value = "" + resScript;
        loggingAdapter.debug(res.toString());
        return res;
    }

    /**
     * Method to get the right instance Java class form a SensorValueType
     * @param value a string value to put in the good class
     * @param type a sensor value type
     * @return an object with the good instance of class and the value
     * @throws Exception
     */
    protected Object getValue(String value, SensorValueType type) throws Exception {
        try {
            Method m = type.getClassType().getDeclaredMethod("valueOf", String.class);

            return m.invoke(null, value);
        } catch (NoSuchMethodException e) {
            this.loggingAdapter.error("No such method exception. The object " + type.getClassType().getName() + " must have a static method valueOf(String) with public access");
            this.loggingAdapter.error(e.getMessage());
            throw new Exception(e);
        } catch (InvocationTargetException e) {
            this.loggingAdapter.error("Invocation target exception. The method valueOf threw an exception");
            this.loggingAdapter.error(e.getMessage());
            throw new Exception(e);
        } catch (IllegalAccessException e) {
            this.loggingAdapter.error("Illegal access exception. The object " + type.getClassType().getName() + " must have a static method valueOf(String) with public access");
            this.loggingAdapter.error(e.getMessage());
            throw new Exception(e);
        }
    }

    /**
     * Method to get the max frequency of a typed sensor value list
     * @param sensors
     * @return
     */
    protected long getMaxTimestamp(List<TypedSensorValue> sensors) {
        long max = 0;
        for (TypedSensorValue v : sensors) {
            if (v.timestamp > max)
                max = v.timestamp;
        }
        return max;
    }

}
