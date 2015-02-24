package fr.unice.smart_campus.middleware.cep_engine;

import java.io.Serializable;

public abstract class CEPEvent implements Serializable {
    public final long serialVersionUID = 1234567891922321327L;

    protected String value;
    protected String name;
    protected long timeStamp;

    public CEPEvent(){}

    public CEPEvent(String value, String name, long timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "{" +
                "\"v\":\"" + value + "\"" +
                ", \"n\":\"" + name + "\"" +
                ", \"t\":" + timeStamp + "" +
                '}';
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
