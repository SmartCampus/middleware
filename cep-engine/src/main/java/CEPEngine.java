import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.espertech.esper.client.*;
import com.typesafe.config.ConfigFactory;

public class CEPEngine {

    private EPRuntime cepRT;

    public CEPEngine() {
        this.init();
    }

    private void init() {
        //The Configuration is meant only as an initialization-time object.
        Configuration cepConfig = new Configuration();

        /** registerEventType **/
        cepConfig.addEventType("StockParkingSensor", ParkingSensorEvent.class.getName());
        cepConfig.addEventType("StockRoomSensor", RoomSensorEvent.class.getName());
        EPServiceProvider cep = EPServiceProviderManager.getProvider("myCEPEngine", cepConfig);

        cepRT = cep.getEPRuntime();
        EPAdministrator cepAdm = cep.getEPAdministrator();

        /** Deploy Statement **/
        EPStatement cepStatement1 = cepAdm.createEPL("" +
                "select a, b \n" +
                "  from StockRoomSensor.win:time(30 minutes) a , StockParkingSensor.win:time(30 minutes) b" +
                " where a.name = b.name");
        // TODO : event a la volée !!
        // TODO : join dans les requetes !!
        EPStatement cepStatementbis = cepAdm.createEPL("select * from StockRoomSensor");
        EPStatement cepStatement2 = cepAdm.createEPL("select * from StockParkingSensor");

        /** Creation of the system **/
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        ActorRef actorRef = system.actorOf(FromConfig.getInstance().props(Props.create(Actor.class)), "remotePool");
        system.actorOf(Props.create(CEPInterfaceActor.class), "CEPInterfaceActor");

        CEPListener cepListener = new CEPListener(actorRef);

        cepStatement1.addListener(cepListener);
        cepStatementbis.addListener(cepListener);
        cepStatement2.addListener(cepListener);
    }

    public void sendEvent(CEPEvent event){
        cepRT.sendEvent(event);
    }
}
