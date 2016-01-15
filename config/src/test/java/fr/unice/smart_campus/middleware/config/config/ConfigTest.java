package fr.unice.smart_campus.middleware.config.config;

/*
 * #%L
 * config
 * %%
 * Copyright (C) 2015 - 2016 Université de Nice Sophia-Antipolis (UNS) - Centre National de la Recherche Scientifique (CNRS)
 * %%
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Université de Nice Sophia-Antipolis (UNS) -
 * Centre National de la Recherche Scientifique (CNRS)
 * Copyright © 2015 UNS, CNRS
 * 
 * 
 *   Authors: SmartCampus Team - http://smartcampus.github.io/sc-contacts/
 * 
 *   Architects: Sébastien Mosser – Laboratoire I3S – mosser@i3s.unice.fr
 *               Michel Riveill - Laboratoire I3S - riveill@i3s.unice.fr
 * #L%
 */

import fr.unice.smart_campus.middleware.model.config.ParentSensor;
import fr.unice.smart_campus.middleware.model.config.SensorParams;
import fr.unice.smart_campus.middleware.model.config.SensorType;
import fr.unice.smart_campus.middleware.config.SensorsConfigOutputDAO;

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
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
       assertEquals(sensorsConfigOutputDAO.check(sensorParams), true);
    }
    @Test
    public void testCheckPhysicalWithScript() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }
    @Test
    public void testCheckPhysicalWithTooManySensors() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.PHYSICAL);
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }

    @Test
    public void testCheckGoodFilter() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), true);
    }
    @Test
    public void testCheckFilterWithWrongScript() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }
    @Test
    public void testCheckFilterWithNotGoodParentSensors() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_FILTER);
        sensorParams.setScript("int B=3975; a=$(TEMP_443); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }
    @Test
    public void testCheckGoodComposite() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), true);
    }

    @Test
    public void testCheckGoodCompositeWithWrongScript() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        parentsSensors.add(new ParentSensor("TEMP_444", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_445); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }



    @Test
    public void testCheckGoodCompositeNotGoodParentSensors() throws Exception {
        SensorsConfigOutputDAO sensorsConfigOutputDAO =new SensorsConfigOutputDAO();
        SensorParams sensorParams=new SensorParams();
        List<ParentSensor> parentsSensors=new ArrayList<ParentSensor>();
        parentsSensors.add(new ParentSensor("TEMP_443", SensorValueType.INTEGER));
        sensorParams.setParentSensors(parentsSensors);
        sensorParams.setSensorType(SensorType.VIRTUAL_COMPOSITE);
        sensorParams.setScript("int B=3975; a=$(TEMP_443)*$(TEMP_444); resistance=(float)(1023-a)*10000/a; temperature=1/(log(resistance/10000)/B+1/298.15)-273.15;");
        assertEquals(sensorsConfigOutputDAO.check(sensorParams), false);
    }
}
