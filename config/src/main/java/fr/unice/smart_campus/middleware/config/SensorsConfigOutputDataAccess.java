package fr.unice.smart_campus.middleware.config;

import fr.unice.smart_campus.middleware.config.model.SensorParams;
import fr.unice.smart_campus.middleware.config.model.SensorType;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SensorsConfigOutputDataAccess {

    private String configDatabaseURL="http://" + System.getProperty("middleware.ip") + ":5000/sensors";


    /**
     * Create a new connection to the database
     */
    public SensorsConfigOutputDataAccess() {

    }


    /**
     * Save a sensor param data into the database
     *
     * @param sensorParams a sensor param to store
     */
    //TODO: Push in the database
    public boolean saveSensorParams (SensorParams sensorParams) {

        String data = "";
        String url = configDatabaseURL;
        if(!check(sensorParams)){
            return false;
        }
        try {

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-type", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(
                    con.getOutputStream());
            writer.write(sensorParams.toString());
            writer.close();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Check if all sensor params are ok
     * @param sensorParams a sensor param
     * @return true if it's ok
     */
    public boolean check(SensorParams sensorParams){

        boolean isPhysical= (SensorType.PHYSICAL.equals(sensorParams.getSensorType()) && (sensorParams.getParentSensors()==null || sensorParams.getParentSensors().size()==0));
        boolean isFilter=(SensorType.VIRTUAL_FILTER.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()==1);
        boolean isComposite = (SensorType.VIRUTAL_COMPOSITE.equals(sensorParams.getSensorType()) && sensorParams.getParentSensors().size()>=2);
        if(!isComposite && !isFilter && !isPhysical){
            System.out.println("LAL");
            return false;
        }
        if(isPhysical){
            if((sensorParams.getScript()!=null && !"".equals(sensorParams.getScript()))) {
                return false;
            }
            return true;
        }


        Pattern pattern = Pattern.compile("\\$\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(sensorParams.getScript());
        List<String> sensorsFromScript=new ArrayList<String>();
        while (matcher.find()) {

                try{
                    sensorsFromScript.add(matcher.group(1));
                }
                catch (IndexOutOfBoundsException e){
                    return false;
                }

            }

        if(sensorsFromScript.size() < sensorParams.getParentSensors().size()){
            return false;
        }
        for(String sensor : sensorsFromScript){
            if(!sensorParams.getParentSensors().contains(sensor)){
                return false;
            }
        }

        return true;
    }
}


