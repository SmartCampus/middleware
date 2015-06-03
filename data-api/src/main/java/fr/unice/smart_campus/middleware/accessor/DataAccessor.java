package fr.unice.smart_campus.middleware.accessor;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import fr.unice.smart_campus.middleware.Helper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.postgresql.ds.PGPoolingDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DataAccessor class
 * To get access to data in SensorsData database.
 */
public class DataAccessor {

    public Connection connection;
    public static String dataConfigApiUrl = "http://" + System.getProperty("middleware.ip") + ":5000/sensors";
    public static String mode = System.getProperty("mode");

    public class Indexes {
        public int indexBeg;
        public int indexEnd;
    }

    /**
     * DataAccessor Constructor to access SensorsData database
     *
     * @throws Exception if there is a problem in database connection or when retrieving pool connections
     */
    public DataAccessor() throws Exception {

        try {
            Context initialContext = new InitialContext();
            if (initialContext == null) {
                System.err.println("Failed to get initial context");
                throw new Exception("Failed to get initial context");
            } else {
                // Get the data source from the context
                PGPoolingDataSource datasource = (PGPoolingDataSource) initialContext.lookup("java:/comp/env/jdbc/SensorsData");
                if (datasource != null) {
                    // Get the connection from the datasource
                    connection = datasource.getConnection();
                } else {
                    System.err.println("Failed to get datasource");
                    throw new ConnectException("Failed to get datasource");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Cannot get connection: " + ex);
        }

    }

    /**
     * @return JSON String list of sensors
     */
    public String getSensors(String idSensor) {

        String data = "";
        String url = "http://" + System.getProperty("middleware.ip") + ":5000/sensors";

        if (idSensor != null) url += "/" + idSensor;

        URL obj = null;
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

            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");
            Reader reader = new InputStreamReader(con.getInputStream());
            while (true) {
                int ch = reader.read();
                if (ch == -1) {
                    break;
                }
                data += (char) ch;
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

        return data;
    }




    /**
     * Returns a JSON object which contains all values of one sensor
     * depending on time (if time specified).
     * If only beginning time is specified, the last value saved before this date is returned.
     * If beginning and ending time are specified, the values between these two dates are returned.
     * If there is no date specified, all values of the sensor are returned.
     *
     * @param idSensor ID sensor that we want to retrieve values
     * @param beg      timestamp of beginning date
     * @param end      timestamp of ending date
     * @param convert  convert timestamp to date
     * @param format   Output format (XML or JSON)
     * @return a JSON Object with the values of the sensor, depending on time.
     * @throws SQLException if there is a problem with the database connection.
     */
    public String getDataFromSensor(String idSensor, long beg, long end, boolean convert, String format) throws Exception {


        String type = "";
        try {
            JSONObject sensors = new JSONObject(getSensors(idSensor));
            type = sensors.getString("sensorType");
        } catch (JSONException exc) {
            throw new Exception("Invalid sensor");
        }

		/* SQL Statement */
        String selectSQL = "SELECT * FROM \"public\".\"SensorsData\" WHERE sensor_id = ?";

        if (beg != 0L) {
            if (end != 0L) {
                selectSQL += " AND sensor_date >= ? AND sensor_date <= ?";
            } else {
                selectSQL += " AND sensor_date = ";
                selectSQL += "                  (SELECT MAX(sensor_date) FROM \"public\".\"SensorsData\" ";
                selectSQL += "                      WHERE sensor_id = ? AND sensor_date <= ?)";
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        String data;



        try {
            ps = connection.prepareStatement(selectSQL);

			/* Parameters for SQL Statement */
            ps.setString(1, idSensor);
            if (beg != 0L) {
                if (end != 0L) {
                    ps.setString(2, String.valueOf(beg));
                    ps.setString(3, String.valueOf(end));
                } else {
                    ps.setString(2, idSensor);
                    ps.setString(3, String.valueOf(beg));
                }
            }

            rs = ps.executeQuery();

            if (format == "json")
                data = buildJSONResult(rs, idSensor, convert);
            else if (format == "rss")
                data = buildXMLResult(rs, idSensor, convert);
            else throw new Exception("Unknown result format");

            ps.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new SQLException(e);
        }

        return data;
    }

    private String buildXMLResult(ResultSet rs, String idSensor, boolean convert) throws SQLException{
        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle(idSensor);
        feed.setDescription("RSS Field for " + idSensor);
        feed.setFeedType("rss_2.0");
        feed.setLink("http://smartcampus.github.io");
        List entries = new ArrayList();
        while (rs.next()) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(rs.getString("sensor_value"));
            entry.setPublishedDate(Helper.getDateFromTimestamp(Long.parseLong(rs.getString("sensor_date"))));
            entry.setLink("http://smartcampus.github.io/#" + (new Random()).nextInt(10000000));
            entries.add(entry);
        }
        feed.setEntries(entries);
        SyndFeedOutput out = new SyndFeedOutput();
        String result;
        try {
            result = out.outputString(feed);
        } catch (FeedException e) {
            result = e.getMessage();
        }
        return result;
    }

    private String buildJSONResult(ResultSet rs, String idSensor, boolean convert) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        while (rs.next()) {
            JSONObject obj = new JSONObject();
            String date;
            if (convert)
                date = Helper.getDateFromTimestamp(Long.parseLong(rs.getString("sensor_date"))).toString();
            else
                date = rs.getString("sensor_date");
            obj.put("date", date);
            obj.put("value", rs.getString("sensor_value"));
            jsonArray.put(obj);
        }

        	/* Returned JSON Object */
        jsonObject.put("id", idSensor);
        jsonObject.put("values", jsonArray);

        return jsonObject.toString();
    }


    /**
     * To close and release connection
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
