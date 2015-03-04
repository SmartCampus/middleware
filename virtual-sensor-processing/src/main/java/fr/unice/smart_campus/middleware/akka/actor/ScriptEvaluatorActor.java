package fr.unice.smart_campus.middleware.akka.actor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fr.unice.smart_campus.middleware.model.sensor.SensorValue;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValue;
import fr.unice.smart_campus.middleware.model.sensor.TypedSensorValueList;
import groovy.lang.GroovyShell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptEvaluatorActor extends UntypedActor {

    private static Map<String, GroovyShell> groovyShellMap;

    private LoggingAdapter loggingAdapter;

    public ScriptEvaluatorActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        this.groovyShellMap = new HashMap<String, GroovyShell>();
    }

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

    protected SensorValue evaluateScript(TypedSensorValueList typedSensorValueList) throws Exception {
        SensorValue res = new SensorValue();
        List<TypedSensorValue> sensors = typedSensorValueList.getSensorValues();

        res.name = typedSensorValueList.getVirtualSensorName();
        res.timestamp = getMaxTimestamp(sensors);

        String script = typedSensorValueList.getScript();

        loggingAdapter.error(script);

        GroovyShell shell = this.groovyShellMap.get(script);
        if(shell == null){
            shell = new GroovyShell();
            this.groovyShellMap.put(script, shell);
        }

        //Set variable for each sensor
        for (TypedSensorValue s : sensors) {
            Object o = getValue(s.getValue(), s.getType());
            loggingAdapter.error("ClassType : " + o.getClass());
            loggingAdapter.error("getValue result " + o);
            shell.setVariable(s.getName(), o);
        }

        loggingAdapter.error("Evaluate !");

        //evaluate script
        String resScript = shell.evaluate(script).toString();
        res.value = "" + resScript;
        loggingAdapter.error(res.toString());
        return res;
    }

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

    protected long getMaxTimestamp(List<TypedSensorValue> sensors) {
        long max = 0;
        for (TypedSensorValue v : sensors) {
            if (v.timestamp > max)
                max = v.timestamp;
        }
        return max;
    }

}
