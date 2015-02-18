import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CEPInterfaceActor extends UntypedActor {
    private LoggingAdapter loggingAdapter;

    public CEPInterfaceActor() {
        this.loggingAdapter = Logging.getLogger(this.context().system(), this);
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String){
            this.loggingAdapter.error(message.toString() + "___________  cep interface actor __________");
        }
    }
}
