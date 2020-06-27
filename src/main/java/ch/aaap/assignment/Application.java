package ch.aaap.assignment;

import ch.aaap.assignment.converter.CsvToModelConverter;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.model.PoliticalCommunity;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

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

  /** @return cantons with amount of districts sorted by amount of districts DESC */
  public Map<String, Long> getCountOfDistrictsByCantonSorted() {
    return model.getPoliticalCommunities().stream()
        .map(pc -> Map.entry(pc.getDistrict(), pc.getCanton()))
        .filter(distinctByKey(Map.Entry::getKey))
        .collect(groupingBy(entry -> entry.getValue().getCode(), Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .collect(
            toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (u, v) -> {
                  throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new));
  }

  /**
   * @return cantons with amount of political communities sorted by amount of political communities
   *     DESC
   */
  public Map<String, Long> countPoliticalCommunitiesByCantonSorted() {
    return model.getPoliticalCommunities().stream()
        .collect(groupingBy(pc -> pc.getCanton().getCode(), Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .collect(
            toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (u, v) -> {
                  throw new IllegalStateException(String.format("Duplicate key %s", u));
                },
                LinkedHashMap::new));
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    final Set<Object> seen = new HashSet<>();
    return (T t) -> seen.add(keyExtractor.apply(t));
  }

  /**
   * @param cantonCode verifies whether specified canton code exists and throws
   *     IllegalArgumentException otherwise
   */
  private void validateCantonCode(String cantonCode) {
    boolean validCantonCode =
        model.getCantonCodes().stream().anyMatch(canton -> canton.equals(cantonCode));
    if (!validCantonCode) {
      throw new IllegalArgumentException("Invalid canton code: " + cantonCode);
    }
  }

  /**
   * @param districtNumber verifies whether specified district number exists and throws
   *     IllegalArgumentException otherwise
   */
  private void validateDistrictNumber(String districtNumber) {
    boolean validDistrictNumber =
        model.getDistrictNumbers().stream().anyMatch(district -> district.equals(districtNumber));
    if (!validDistrictNumber) {
      throw new IllegalArgumentException("Invalid district number: " + districtNumber);
    }
  }
}
