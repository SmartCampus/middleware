package fr.unice.smart_campus.middleware.akka.actor;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import config.SensorParams;
import fr.unice.smart_campus.middleware.config.SensorsConfigInputDataAccess;
import groovy.lang.GroovyShell;
import sensor.SensorValue;
import sensor.SensorValueType;
import sensor.TypedSensorValue;
import sensor.TypedSensorValueList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class ScriptEvaluatorActor extends UntypedActor {

    private LoggingAdapter loggingAdapter;
    private SensorsConfigInputDataAccess sensorConfigAdapter;

    public ScriptEvaluatorActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
        this.sensorConfigAdapter = new SensorsConfigInputDataAccess();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof TypedSensorValueList) {
            TypedSensorValueList sensors = (TypedSensorValueList) message;

            this.loggingAdapter.info(message.toString());

            SensorValue response = evaluateScript(sensors.getSensorValues());

            ActorSelection databaseAccess = this.getContext().actorSelection("akka://ActorSystemFactory/user/DatabaseAccessActor");
            databaseAccess.tell(response, this.self());

            ActorSelection actorSelection = this.getContext().actorSelection("akka.tcp://Simulation@localhost:2553/user/CEPInterfaceActor");
            actorSelection.tell(response, this.sender());
        }
    }

    protected SensorValue evaluateScript(List<TypedSensorValue> sensors) throws Exception {
        SensorValue res = new SensorValue();

        //get sensors names
        List<String> sensorNames = new LinkedList<String>();
        for (SensorValue s : sensors)
            sensorNames.add(s.getName());

        SensorParams sensor = sensorConfigAdapter.getSensorFormParentsSensors(sensorNames);
        res.name = sensor.getName();
        res.timestamp = getMaxTimestamp(sensors);

        String script = sensor.getScript();

        loggingAdapter.error(script);

        GroovyShell shell = new GroovyShell();
        //Set variable for each sensor
        for (TypedSensorValue s : sensors) {
            shell.setVariable(s.getName(), getValue(s.getValue(), s.getType()));
        }

        loggingAdapter.error("Evaluate !");

        //evaluate script
        String resScript = shell.evaluate(script).toString();
        res.value = "" + resScript;
        loggingAdapter.error(res.toString());
        return res;
    }

    protected Object getValue(String value, SensorValueType type) throws Exception{
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
