package fr.unice.smart_campus.middleware.akka.actor;

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

        loggingAdapter.error(script);

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
            loggingAdapter.error("ClassType : " + o.getClass());
            loggingAdapter.error("getValue result " + o);
            binding.setVariable(s.getName(), o);
//            shell.setVariable(s.getName(), o);
        }

        loggingAdapter.error("Evaluate !");

//        evaluate script
        groovyScript.setBinding(binding);
        String resScript = groovyScript.run().toString();
//        String resScript = shell.evaluate(script).toString();
        res.value = "" + resScript;
        loggingAdapter.error(res.toString());
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
