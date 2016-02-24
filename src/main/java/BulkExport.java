/**
 * Created by NADEESHA on 2/24/2016.
 */

import com.google.common.io.ByteStreams;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;




public class BulkExport {
    static final String TRUST_STORE_URL_PROPERTY ="trust.store.url";
    static final String TRUST_STORE_PASSWORD_PROPERTY ="trust.store.password";
    static final String PUBLISHER_API_VERSION_PROPERTY ="publisher.api.version";
    static final String HOST_PROPERTY ="host";
    static final String PORT_PROPERTY ="port";
    static final String EXPORT_FOLDER_PROPERTY ="export.path";
    static final String ADMIN_USERID_PROPOERTY ="admin.userid";
    static final String ADMIN_PASSWORD_PROPERTY ="admin.password";
    static final String EXPORT_API_VERSION_PROPERTY="export.api.version";
    static final String GET ="GET";
    static final String AUTHORIZATION_HTTP_HEADER ="Authorization";
    static final String BASIC_KEY ="Basic";
    static final String ZIP_KEY =".zip";
    static final String API_NAME_KEY ="name";
    static final String API_VERSION_KEY ="version";
    static final String API_PROVIDER_KEY ="provider";
    static final String LIST_KEY ="list";
    static Properties prop;
    static String authString;


    public static void main (String[] args){
        ReadProperties();
        try {
            //SSL Cert
            String trustStore = prop.getProperty(TRUST_STORE_URL_PROPERTY);
            String trustStorePassword = prop.getProperty(TRUST_STORE_PASSWORD_PROPERTY);
            if(trustStore != null && !trustStore.isEmpty() && trustStorePassword != null && !trustStorePassword.isEmpty()) {
                System.setProperty("javax.net.ssl.trustStore", trustStore);
                System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
            }

            //GET API List from Publisher API
            URL url = new URL(prop.getProperty(HOST_PROPERTY)+":"+prop.getProperty(PORT_PROPERTY)+"/api/am/publisher/"+prop.getProperty(PUBLISHER_API_VERSION_PROPERTY)+"/apis");
            HttpURLConnection e = (HttpURLConnection)url.openConnection();
            e.setDoOutput(true);
            e.setRequestMethod(GET);
            authString= encodeCredentials (prop.getProperty(ADMIN_USERID_PROPOERTY),prop.getProperty(ADMIN_PASSWORD_PROPERTY));
            e.setRequestProperty(AUTHORIZATION_HTTP_HEADER, BASIC_KEY + " "+authString);
            //System.out.println(new String(ByteStreams.toByteArray(e.getInputStream())));
            String responseStr = new String(ByteStreams.toByteArray(e.getInputStream()));
            JSONParser parser = new JSONParser();
            try {
                JSONObject responseList = (JSONObject) parser.parse(responseStr);

                JSONArray apiList = (JSONArray) responseList.get(LIST_KEY);
                System.out.println("EXPORTING OUT "+apiList.size()+" API's");
                for (int cnt=0;cnt<apiList.size();cnt++) {
                    JSONObject api =(JSONObject) apiList.get(cnt);
                    String provider = (String)api.get(API_PROVIDER_KEY);
                    String name = (String)api.get(API_NAME_KEY);
                    String version = (String)api.get(API_VERSION_KEY);
                    exportAPI(name,version,provider);

                }
                System.out.println("API's exported to "+prop.getProperty(EXPORT_FOLDER_PROPERTY));
            } catch (ParseException error) {
                System.out.println("API List requested from API Manager is in wrong format" + error);
            }

        } catch (IOException error) {
            System.out.println("Error invoking Publisher API" + error);
        }

    }


    public static void exportAPI (String name, String version, String provider){
        try {
            /*
            //SSL Cert
            String trustStore = "D:\\APIM-Demo\\API_M_1.10\\wso2am-1.10.0\\repository\\resources\\security\\wso2carbon.jks";
            String trustStorePassword = "wso2carbon";

            if(trustStore != null && !trustStore.isEmpty() && trustStorePassword != null && !trustStorePassword.isEmpty()) {
                System.setProperty("javax.net.ssl.trustStore", trustStore);
                System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
            }
            */
            // Exporting API
            URL url = new URL(prop.getProperty(HOST_PROPERTY)+":"+prop.getProperty(PORT_PROPERTY)+"/api-import-export-"+prop.getProperty(EXPORT_API_VERSION_PROPERTY)+"/export-api?name="+name+"&version="+version+"&provider="+provider);
            HttpURLConnection e = (HttpURLConnection)url.openConnection();
            e.setDoOutput(true);
            e.setRequestMethod(GET);
            authString= encodeCredentials (prop.getProperty(ADMIN_USERID_PROPOERTY),prop.getProperty(ADMIN_PASSWORD_PROPERTY));
            e.setRequestProperty(AUTHORIZATION_HTTP_HEADER, BASIC_KEY + " "+authString);


            //Writing to file
            FileOutputStream fos = new FileOutputStream(prop.getProperty(EXPORT_FOLDER_PROPERTY)+name+ZIP_KEY);
            fos.write(ByteStreams.toByteArray(e.getInputStream()));
            fos.close();
        } catch (IOException error) {
            System.out.println("Error invoking API Export Service" + error);
        }

    }

    public static void ReadProperties(){
        prop= new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);



        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String encodeCredentials(String uid, String password){
        byte[] encodedBytes = Base64.encodeBase64((uid + ":" + password).getBytes());
        return new String(encodedBytes);

    }

}
