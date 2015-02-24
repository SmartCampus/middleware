package fr.unice.smart_campus.middleware.cep_engine;

import akka.actor.ActorRef;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.bean.BeanEventBean;
import sensor.SensorValueType;
import sensor.TypedSensorValue;

import java.util.Map;

/**
 * Listener of the event from the CEP engine.
 * When it receive a message from the CEP engine, it transfers the message to a remote Akka actor
 */
public abstract class CEPListener implements UpdateListener {
    private ActorRef actorRef;
    protected SensorValueType type;

    public CEPListener(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public void setType(SensorValueType type) {
        this.type = type;
    }

    public void update(final EventBean[] newData, EventBean[] oldData) {
        // TODO Recuperer N message
        // TODO Creer objetAkka a envoyer
//        System.out.println("Type : " + type);
//        System.out.println("SizeNew : " + newData.length);
//        System.out.println(newData[0].getUnderlying().toString());


        Map map = (Map) newData[0].getUnderlying();
        for(int i =0; i < map.size(); i ++){
            CEPEvent event = (CEPEvent) ((EventBean)map.get("a"+i)).getUnderlying();
            System.out.println(event);
        }

//        for(Object o : map.keySet()){
//            System.out.println("keyClass : "  + o.getClass());
//            System.out.println("keyContent : "  + o.toString());
//            System.out.println("valueClass : " + map.get(o).getClass());
//            System.out.println("valueContent : " + map.get(o).toString());
//
//            System.out.println("lol " + ((BeanEventBean) map.get(o)).getUnderlying());
//        }

        System.out.println("Send message to Akka actor : ");
// TODO       SensorValue sensorValueToSend = new SensorValue(message.getName(), Long.parseLong(message.getTimeStamp()),message.getValue());
// TODO      this.actorRef.tell(sensorValueToSend, ActorRef.noSender());
    }
}
