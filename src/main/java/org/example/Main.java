package org.example;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;


import java.awt.image.AreaAveragingScaleFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String endpoint = "https://api.fda.gov/drug/event.json?search=patient.drug.openfda.brand_name:aspirin&limit=10";

        HttpGet request = new HttpGet(endpoint);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(request);

        if(response.getCode() != 200){
            throw new RuntimeException("Failed to get the shit " + response.getCode());
        }
        String jsonResponse = EntityUtils.toString(((ClassicHttpResponse) response).getEntity());
        JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);

        //Create list of Medication objects
        List<Medication> medications = new ArrayList<>();

        for(int i = 0; i < jsonObject.getAsJsonArray("results").size(); i++){
            String brandName = jsonObject.getAsJsonArray("results").get(i)
                    .getAsJsonObject().getAsJsonObject("patient")
                    .getAsJsonArray("drug").get(0).getAsJsonObject().getAsJsonArray("openfda")
                    .get(0).getAsString();

            Medication medication = new Medication();
            medication.setBrand_name(brandName);
            medications.add(medication);
        }

        //printing them out
        for(Medication medication : medications){
            System.out.println(medication.getBrand_name());
        }
    }
}