package edu.asu.cassess.service.github;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class GitHubProperties {
    private String accessToken;

    public GitHubProperties() {
        Properties properties = new Properties();

        BufferedReader reader = null;
        try {
            InputStream in = getClass().getResourceAsStream("/github.properties");
            reader = new BufferedReader(new InputStreamReader(in));
            properties.load(reader);
            accessToken = properties.getProperty("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

}
