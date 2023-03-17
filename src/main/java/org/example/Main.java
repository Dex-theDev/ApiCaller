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
import java.util.Optional;

public class Main {
  public static void main(String[] args) throws Exception {

    String apiUrl = "https://api.fda.gov/drug/drugsfda.json?&limit=1000";

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

      Connection jDbCconnection =
          DriverManager.getConnection("jdbc:postgresql://localhost:5432/dex");
      PreparedStatement statement =
          jDbCconnection.prepareStatement(
              "INSERT INTO medication_with_uuid (brand_name, strength, dosage_form) VALUES (?,?,?)");

      List<String> brandNameList = new ArrayList<>();
      List<Medication> medications = new ArrayList<>();
      for (JsonElement resultElement : results) {
        JsonObject result = resultElement.getAsJsonObject();
        // products may be null
        Optional<JsonArray> maybeProducts = Optional.ofNullable(result.getAsJsonArray("products"));
        if (maybeProducts.isEmpty()) {
          continue;
        }
        // if we're here that means products are good to go
        JsonArray products = maybeProducts.get();

        Optional<String> maybeBrandName =
            Optional.ofNullable(
                products
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonPrimitive("brand_name")
                    .getAsString());

        Optional<String> maybeStrength =
            Optional.ofNullable(
                products
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonArray("active_ingredients")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonPrimitive("strength")
                    .getAsString());

        Optional<String> maybe_dosage_form =
            Optional.ofNullable(
                products
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonPrimitive("dosage_form")
                    .getAsString());
        if (maybeBrandName.isEmpty() || maybeStrength.isEmpty() || maybe_dosage_form.isEmpty()) {
          continue;
        }

        // i should have values for everything by time i'm here
        String brandName = maybeBrandName.get();

        String strength = maybeStrength.get();

        String dosage_form = maybe_dosage_form.get();
        brandNameList.add(brandName);

        Medication medication =
            new MedicationBuilder()
                .brand_name(brandName)
                .strength(strength)
                .dosage_form(dosage_form)
                .build();

        medications.add(medication);

        statement.setString(1, brandName);
        statement.setString(2, strength);
        statement.setString(3, dosage_form);
        statement.execute();
      }
      for (String brand : brandNameList) {
        System.out.println(brand);
      }
      for (Medication medication : medications) {
        System.out.println(medication.toString());
      }

      statement.close();
      jDbCconnection.close();
    } else {
      System.out.println("Error retrieving medication data. Response code: " + responseCode);
    }
  }
}
