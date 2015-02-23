package fr.unice.smart_campus.middleware.virtual_sensor_processing;

import groovy.lang.GroovyShell;

import java.util.List;

/**
 * Created by Moka on 23/02/2015.
 */
public class ScriptEvaluator {

    public static String evaluateScript(List<String> sensors){
        //TODO get script
        String script = "";


        GroovyShell shell = new GroovyShell();
        shell.setVariable("input","2");
        return shell.evaluate(script).toString();
    }

}
