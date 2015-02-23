package fr.unice.smart_campus.middleware.cep_engine;

import java.io.Serializable;

public class ParkingSensorEvent  extends CEPEvent implements Serializable{
    public final long serialVersionUID = 1234567891922321327L;

    public ParkingSensorEvent(String value, String name, String timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }
}