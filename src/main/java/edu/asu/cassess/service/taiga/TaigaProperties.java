package edu.asu.cassess.service.taiga;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class TaigaProperties {
    Properties taigaprop;
    BufferedReader reader;
    String username;
    String password;


    public TaigaProperties() {
        taigaprop = new Properties();

        try {

            InputStream in = getClass().getResourceAsStream("/taiga.properties");
            reader = new BufferedReader(new InputStreamReader(in));

            taigaprop.load(reader);

            username = taigaprop.getProperty("username");
            password = taigaprop.getProperty("password");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
