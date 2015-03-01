package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import fr.unice.smart_campus.middleware.model.config.ParentSensorDescription;
import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
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
    private ActorRef actorRef;
    protected Map<String, SensorValueType> parentValueTypeMap;
    protected String script;
    protected String virtualSensorName;

    public CEPListener(ActorRef actorRef) {
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

        System.out.println("Send message to Akka actor : " + akkaMessage);
        this.actorRef.tell(akkaMessage, ActorRef.noSender());
    }
}
