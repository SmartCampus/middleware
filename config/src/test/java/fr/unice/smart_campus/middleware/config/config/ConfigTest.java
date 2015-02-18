package fr.unice.smart_campus.middleware.config.config;

import fr.unice.smart_campus.middleware.config.OutputDataAccess;
import fr.unice.smart_campus.middleware.config.model.SensorParams;
import fr.unice.smart_campus.middleware.config.model.SensorType;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clement0210 on 15-02-18.
 */
public class ConfigTest extends TestCase {

    @Test
    public void testCheckGoodPhysical() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
       assertEquals(outputDataAccess.saveSensorParams(sensorParams), true);
    }
    @Test
    public void testCheckPhysicalWithScript() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }
    @Test
    public void testCheckPhysicalWithTooManySensors() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }

    @Test
    public void testCheckGoodFilter() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), true);
    }
    @Test
    public void testCheckFilterWithWrongScript() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }
    @Test
    public void testCheckFilterWithNotGoodParentSensors() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        parentsSensors.add("TEMP_444");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }
    @Test
    public void testCheckGoodComposite() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        parentsSensors.add("TEMP_444");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRUTAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), true);
    }

    @Test
    public void testCheckGoodCompositeWithWrongScript() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        parentsSensors.add("TEMP_444");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRUTAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_445); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }



    @Test
    public void testCheckGoodCompositeNotGoodParentSensors() throws Exception {
        OutputDataAccess outputDataAccess=new OutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<String> parentsSensors=new ArrayList<String>();
        parentsSensors.add("TEMP_443");
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRUTAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(outputDataAccess.saveSensorParams(sensorParams), false);
    }
}
