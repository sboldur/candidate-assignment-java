package ch.aaap.assignment.model;

import ch.aaap.assignment.raw.CSVPostalCommunity;
import lombok.Data;

@Data
public class PostalCommunity {

  private String zipCode;
  private String zipCodeAddition;
  private String name;

  public PostalCommunity(CSVPostalCommunity csvPostalCommunity) {
    this.zipCode = csvPostalCommunity.getZipCode();
    this.zipCodeAddition = csvPostalCommunity.getZipCodeAddition();
    this.name = csvPostalCommunity.getName();
  }
}
