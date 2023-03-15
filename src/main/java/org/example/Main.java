package org.example;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



import java.io.BufferedReader;
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

        String apiUrl = "https://api.fda.gov/drug/drugsfda.json?&limit=500";


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

            Class.forName("org.postgresql.Driver");

            Connection jDbCconnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dex");
            PreparedStatement statement = jDbCconnection.prepareStatement("INSERT INTO medication_test2 (brand_name, strength, dosage_form) VALUES (?,?,?)");



                List<String> brandNameList = new ArrayList<>();
                List<Medication> medications = new ArrayList<>();
                for (JsonElement resultElement : results) {
                    JsonObject result = resultElement.getAsJsonObject();
                    JsonArray products = result.getAsJsonArray("products");
                    String brandName = products.getAsJsonArray().get(0).getAsJsonObject().getAsJsonPrimitive("brand_name").getAsString();
                    String strength = products.getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("active_ingredients").getAsJsonArray().get(0).getAsJsonObject().getAsJsonPrimitive("strength").getAsString();
                    String dosage_form = products.getAsJsonArray().get(0).getAsJsonObject().getAsJsonPrimitive("dosage_form").getAsString();
                    if (brandName == null) {
                        continue;
                    }
                    brandNameList.add(brandName);

                    Medication medication = new Medication();
                    medication.setBrand_name(brandName);
                    medication.setStrength(strength);
                    medication.setDosage_form(dosage_form);
                    medications.add(medication);

                    statement.setString(1, brandName);
                    statement.setString(2, strength);
                    statement.setString(3, dosage_form);
                    statement.execute();
//                    for (JsonElement brandNameElement : brandName) {
//                       // String brandName = brandNameElement.getAsString();
//
//                        Medication medication = new Medication();
//                      //  medication.setBrand_name(brandName);
//                        medications.add(medication);
//
//                       // statement.setString(1, brandName);
//                       // statement.execute();
//                    }
                }
                for (String brand : brandNameList) {
                    System.out.println(brand);
                }
                for(Medication medication : medications){
                    System.out.println(medication.toString());
                }

           statement.close();
            jDbCconnection.close();
        } else {
            System.out.println("Error retrieving medication data. Response code: " + responseCode);
        }

    }
}