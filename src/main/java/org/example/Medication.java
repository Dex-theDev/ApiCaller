package org.example;

public class Medication {
  private String brand_name;

  private String purpose;

  private String strength;

  private String dosage_form;

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(String dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
  @Override
    public String toString() {
        return "Medication name='" + brand_name + "', dosage form=" + dosage_form + "', strength=" + strength;
    }

}
