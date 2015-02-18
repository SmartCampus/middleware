import java.util.Random;

public class Main {
    public static void main(String[] args) {
        CEPEngine cepEngine = new CEPEngine();

        // We generate a few ticks...
        for (int i = 0; i < 15; i++) {
            GenerateRandomTick(cepEngine, i);
        }
    }

    private static Random generator = new Random();

    public static void GenerateRandomTick(CEPEngine cepRT, int i) {
        double price = (double) generator.nextInt(10);
        long timeStamp = System.currentTimeMillis();
        String symbol = "AAPL";
        ParkingSensorEvent tick = new ParkingSensorEvent("\tPark" + i + "\t", String.valueOf(12 + i), String.valueOf(timeStamp));
        //System.out.println("\nSending tick:" + tick);
        cepRT.sendEvent(tick);

        RoomSensorEvent tick2 = new RoomSensorEvent("\tRoom\"+i+\"\t", "12", "2" + String.valueOf(timeStamp));
        //System.out.println("\nSending tick 2:" + tick2);
        cepRT.sendEvent(tick2);
    }
}
