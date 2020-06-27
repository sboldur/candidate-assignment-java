package ch.aaap.assignment.model;

import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class PoliticalCommunity {

  private String number;
  private String name;
  private String shortName;
  private LocalDate lastUpdate;

  private Canton canton;
  private District district;

  private Set<PostalCommunity> postalCommunities;

  public PoliticalCommunity(CSVPoliticalCommunity csvPoliticalCommunity) {
    this.number = csvPoliticalCommunity.getNumber();
    this.name = csvPoliticalCommunity.getName();
    this.shortName = csvPoliticalCommunity.getShortName();
    this.lastUpdate = csvPoliticalCommunity.getLastUpdate();
    this.canton =
        new Canton(csvPoliticalCommunity.getCantonCode(), csvPoliticalCommunity.getCantonName());
    this.district =
        new District(
            csvPoliticalCommunity.getDistrictNumber(), csvPoliticalCommunity.getDistrictName());
    this.postalCommunities = new HashSet<>();
  }
}
