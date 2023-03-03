package org.example;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        String apiUrl = "https://api.fda.gov/drug/label.json?&limit=50";


        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);
            JsonArray results = jsonObject.getAsJsonArray("results");

            Connection jDbCconnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dex");
            PreparedStatement statement = jDbCconnection.prepareStatement("INSERT INTO medication_test (brand_name) VALUES (?)");



                List<String> brandNameList = new ArrayList<>();
                List<Medication> medications = new ArrayList<>();
                for (JsonElement resultElement : results) {
                    JsonObject result = resultElement.getAsJsonObject();
                    JsonArray brandNames = result.getAsJsonObject("openfda").getAsJsonArray("brand_name");
                    if (brandNames == null) {
                        continue;
                    }
                    for (JsonElement brandNameElement : brandNames) {
                        String brandName = brandNameElement.getAsString();

                        Medication medication = new Medication();
                        medication.setBrand_name(brandName);
                        medications.add(medication);

                        statement.setString(1, brandName);
                        statement.execute();
                    }
                }
                for (String brand : brandNameList) {
                    System.out.println(brand);
                }

            statement.close();
            jDbCconnection.close();
        } else {
            System.out.println("Error retrieving medication data. Response code: " + responseCode);
        }

    }
}