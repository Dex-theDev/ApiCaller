package org.example;

import io.norberg.automatter.AutoMatter;

@AutoMatter
public interface Medication {

  String brand_name();

  String strength();

  String dosage_form();

  // will leave this for now, i can't define methods in interfaces, but i'm sure i can add this
  // elsewhere.
  //  @Override
  //    public String toString() {
  //        return "Medication name='" + brand_name + "', dosage form=" + dosage_form + "',
  // strength=" + strength;
  //    }

}
