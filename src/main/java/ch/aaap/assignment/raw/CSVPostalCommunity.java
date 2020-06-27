package ch.aaap.assignment.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CSVPostalCommunity {

  // PLZ4
  private String zipCode;

  // PLZZ
  private String zipCodeAddition;

  // PLZNAMK
  private String name;

  // KTKZ
  private String cantonCode;

  // GDENR
  private String politicalCommunityNumber;

  // GDENAMK
  private String politicalCommunityShortName;

  public String getZipCodeKey() {
    return zipCode + zipCodeAddition;
  }
}
