package fr.unice.smart_campus.middleware.cep_engine;

import java.io.Serializable;

public class RoomSensorEvent extends CEPEvent implements Serializable {
    public final long serialVersionUID = 1234567891322321321L;


    public RoomSensorEvent(String value, String name, String timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }

}