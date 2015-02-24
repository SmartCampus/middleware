package fr.unice.smart_campus.middleware.cep_engine;

public class Main {
    public static void main(String[] args) throws Exception {
        CEPEngine cepEngine = new CEPEngine();

        CEPEvent cepEvent= (CEPEvent)Class.forName("TEMP_443").newInstance();
        cepEvent.setName("TEMP_443");
        cepEvent.setTimeStamp(12233456789L);
        cepEvent.setValue("12.2");

        CEPEvent cepEvent2= (CEPEvent)Class.forName("TEMP_444").newInstance();
        cepEvent2.setName("TEMP_444");
        cepEvent2.setTimeStamp(12233456789L);
        cepEvent2.setValue("12.2");

        cepEngine.sendEvent(cepEvent);
        cepEngine.sendEvent(cepEvent2);
    }
}
