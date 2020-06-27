package ch.aaap.assignment;

import ch.aaap.assignment.converter.CsvToModelConverter;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {
  @Getter private Model model;

  /** Read the CSVs and initializes a in memory model */
  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();
    model = CsvToModelConverter.getModelFromCsvData(politicalCommunities, postalCommunities);
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    validateCantonCode(cantonCode);
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getCanton().getCode().equals(cantonCode))
        .count();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    validateCantonCode(cantonCode);
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getCanton().getCode().equals(cantonCode))
        .map(pc -> pc.getDistrict().getNumber())
        .distinct()
        .count();
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given district
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    validateDistrictNumber(districtNumber);
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getDistrict().getNumber().equals(districtNumber))
        .count();
  }

  /**
   * @param zipCode 4 digit zip code
   * @return districts that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    return model.getPoliticalCommunities().stream()
        .filter(
            pc ->
                pc.getPostalCommunities().stream()
                    .anyMatch(postalCommunity -> postalCommunity.getZipCode().equals(zipCode)))
        .map(pc -> pc.getDistrict().getName())
        .collect(Collectors.toSet());
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    return model.getPoliticalCommunities().stream()
        .filter(
            pc ->
                pc.getPostalCommunities().stream()
                    .anyMatch(
                        postalCommunity -> postalCommunity.getName().equals(postalCommunityName)))
        .map(PoliticalCommunity::getLastUpdate)
        .min(LocalDate::compareTo)
        .orElseThrow(IllegalArgumentException::new);
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of cantons
   */
  public long getAmountOfCantons() {
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    return model.getPoliticalCommunities().stream()
        .filter(pc -> pc.getPostalCommunities().isEmpty())
        .count();
  }

  private void validateCantonCode(String cantonCode) {
    boolean validCantonCode =
        model.getCantonCodes().stream().anyMatch(canton -> canton.equals(cantonCode));
    if (!validCantonCode) {
      throw new IllegalArgumentException("Invalid canton code: " + cantonCode);
    }
  }

  private void validateDistrictNumber(String districtNumber) {
    boolean validDistrictNumber =
        model.getDistrictNumbers().stream().anyMatch(district -> district.equals(districtNumber));
    if (!validDistrictNumber) {
      throw new IllegalArgumentException("Invalid district number: " + districtNumber);
    }
  }
}
