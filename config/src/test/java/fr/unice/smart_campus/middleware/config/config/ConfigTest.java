package fr.unice.smart_campus.middleware.config.config;

import fr.unice.smart_campus.middleware.model.config.ParentSensor;
import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.model.config.SensorType;
import fr.unice.smart_campus.middleware.config.SensorsConfigOutputDataAccess;

import fr.unice.smart_campus.middleware.model.sensor.SensorValueType;
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
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
       assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), true);
    }
    @Test
    public void testCheckPhysicalWithScript() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }
    @Test
    public void testCheckPhysicalWithTooManySensors() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }

    @Test
    public void testCheckGoodFilter() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), true);
    }
    @Test
    public void testCheckFilterWithWrongScript() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }
    @Test
    public void testCheckFilterWithNotGoodParentSensors() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }
    @Test
    public void testCheckGoodComposite() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), true);
    }

    @Test
    public void testCheckGoodCompositeWithWrongScript() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_445); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }



    @Test
    public void testCheckGoodCompositeNotGoodParentSensors() throws Exception {
        SensorsConfigOutputDataAccess sensorsConfigOutputDataAccess =new SensorsConfigOutputDataAccess();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDataAccess.check(sensorParams), false);
    }
}
