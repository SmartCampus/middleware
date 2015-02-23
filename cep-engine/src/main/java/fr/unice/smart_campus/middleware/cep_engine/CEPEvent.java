package fr.unice.smart_campus.middleware.cep_engine;

public abstract class CEPEvent {
    protected String value;
    protected String name;
    protected String timeStamp;

    public CEPEvent() {
        this.value = "";
        this.name = "";
        this.timeStamp = "";
    }
    public CEPEvent(String value, String name, String timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "{" +
                "\"v\":\"" + value + "\"" +
                ", \"n\":\"" + name + "\"" +
                ", \"t\":\"" + timeStamp + "\"" +
                '}';
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
