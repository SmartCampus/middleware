import java.io.Serializable;

public class RoomSensorEvent extends CEPEvent implements Serializable {
    public final long serialVersionUID = 1234567891322321321L;
    private final String value;
    private final String name;
    private final String timeStamp;



    public RoomSensorEvent(String value, String name, String timeStamp) {
        this.value = value;
        this.name = name;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "RoomSensorEvent{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
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


}