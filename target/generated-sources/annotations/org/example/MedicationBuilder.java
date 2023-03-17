package org.example;

import io.norberg.automatter.AutoMatter;
import javax.annotation.processing.Generated;

@Generated("io.norberg.automatter.processor.AutoMatterProcessor")
@AutoMatter.Generated
public final class MedicationBuilder {
  private String brand_name;

  private String strength;

  private String dosage_form;

  public MedicationBuilder() {
  }

  private MedicationBuilder(Medication v) {
    this.brand_name = v.brand_name();
    this.strength = v.strength();
    this.dosage_form = v.dosage_form();
  }

  private MedicationBuilder(MedicationBuilder v) {
    this.brand_name = v.brand_name();
    this.strength = v.strength();
    this.dosage_form = v.dosage_form();
  }

  public String brand_name() {
    return brand_name;
  }

  public MedicationBuilder brand_name(String brand_name) {
    if (brand_name == null) {
      throw new NullPointerException("brand_name");
    }
    this.brand_name = brand_name;
    return this;
  }

  public String strength() {
    return strength;
  }

  public MedicationBuilder strength(String strength) {
    if (strength == null) {
      throw new NullPointerException("strength");
    }
    this.strength = strength;
    return this;
  }

  public String dosage_form() {
    return dosage_form;
  }

  public MedicationBuilder dosage_form(String dosage_form) {
    if (dosage_form == null) {
      throw new NullPointerException("dosage_form");
    }
    this.dosage_form = dosage_form;
    return this;
  }

  public Medication build() {
    return new Value(brand_name, strength, dosage_form);
  }

  public static MedicationBuilder from(Medication v) {
    return new MedicationBuilder(v);
  }

  public static MedicationBuilder from(MedicationBuilder v) {
    return new MedicationBuilder(v);
  }

  @AutoMatter.Generated
  private static final class Value implements Medication {
    private final String brand_name;

    private final String strength;

    private final String dosage_form;

    private Value(@AutoMatter.Field("brand_name") String brand_name,
        @AutoMatter.Field("strength") String strength,
        @AutoMatter.Field("dosage_form") String dosage_form) {
      if (brand_name == null) {
        throw new NullPointerException("brand_name");
      }
      if (strength == null) {
        throw new NullPointerException("strength");
      }
      if (dosage_form == null) {
        throw new NullPointerException("dosage_form");
      }
      this.brand_name = brand_name;
      this.strength = strength;
      this.dosage_form = dosage_form;
    }

    @AutoMatter.Field
    @Override
    public String brand_name() {
      return brand_name;
    }

    @AutoMatter.Field
    @Override
    public String strength() {
      return strength;
    }

    @AutoMatter.Field
    @Override
    public String dosage_form() {
      return dosage_form;
    }

    public MedicationBuilder builder() {
      return new MedicationBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Medication)) {
        return false;
      }
      final Medication that = (Medication) o;
      if (brand_name != null ? !brand_name.equals(that.brand_name()) : that.brand_name() != null) {
        return false;
      }
      if (strength != null ? !strength.equals(that.strength()) : that.strength() != null) {
        return false;
      }
      if (dosage_form != null ? !dosage_form.equals(that.dosage_form()) : that.dosage_form() != null) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (this.brand_name != null ? this.brand_name.hashCode() : 0);
      result = 31 * result + (this.strength != null ? this.strength.hashCode() : 0);
      result = 31 * result + (this.dosage_form != null ? this.dosage_form.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Medication{" +
      "brand_name=" + brand_name +
      ", strength=" + strength +
      ", dosage_form=" + dosage_form +
      '}';
    }
  }
}
