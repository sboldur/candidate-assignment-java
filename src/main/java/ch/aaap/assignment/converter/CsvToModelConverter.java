package ch.aaap.assignment.converter;

import ch.aaap.assignment.model.*;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvToModelConverter {
  public static Model getModelFromCsvData(
      Set<CSVPoliticalCommunity> csvPoliticalCommunities,
      Set<CSVPostalCommunity> csvPostalCommunities) {

    final Map<String, PoliticalCommunity> politicalCommunitiesByNumber =
        getPoliticalCommunitiesGroupedByNumber(csvPoliticalCommunities);

    Set<PostalCommunity> postalCommunities =
        getPostalCommunities(csvPostalCommunities, politicalCommunitiesByNumber);
    Set<PoliticalCommunity> politicalCommunities =
        new HashSet<>(politicalCommunitiesByNumber.values());
    Set<Canton> cantons = getCantons(politicalCommunities);
    Set<District> districts = getDistricts(politicalCommunities);

    return new Model(politicalCommunities, postalCommunities, cantons, districts);
  }

  private static Set<PostalCommunity> getPostalCommunities(
      Set<CSVPostalCommunity> csvPostalCommunities,
      Map<String, PoliticalCommunity> politicalCommunitiesByNumber) {
    return groupCsvPostalCommunitiesByZipData(csvPostalCommunities).values().stream()
        .flatMap(
            (csvPostalCommunitySet) ->
                csvPostalCommunitySet.stream()
                    .map(
                        csvPostalCommunity ->
                            createPostalCommunityAndAssociateToPoliticalCommunity(
                                politicalCommunitiesByNumber, csvPostalCommunity)))
        .collect(Collectors.toSet());
  }

  private static PostalCommunity createPostalCommunityAndAssociateToPoliticalCommunity(
      Map<String, PoliticalCommunity> politicalCommunitiesByNumber,
      CSVPostalCommunity csvPostalCommunity) {
    PostalCommunity postalCommunity = new PostalCommunity(csvPostalCommunity);
    PoliticalCommunity politicalCommunity =
        politicalCommunitiesByNumber.get(csvPostalCommunity.getPoliticalCommunityNumber());
    politicalCommunity.getPostalCommunities().add(postalCommunity);
    return postalCommunity;
  }

  private static Map<String, Set<CSVPostalCommunity>> groupCsvPostalCommunitiesByZipData(
      Set<CSVPostalCommunity> csvPostalCommunities) {
    return csvPostalCommunities.stream()
        .collect(Collectors.groupingBy(CSVPostalCommunity::getZipCodeKey, Collectors.toSet()));
  }

  private static Map<String, PoliticalCommunity> getPoliticalCommunitiesGroupedByNumber(
      Set<CSVPoliticalCommunity> csvPoliticalCommunities) {
    return csvPoliticalCommunities.stream()
        .map(PoliticalCommunity::new)
        .collect(Collectors.toMap(PoliticalCommunity::getNumber, Function.identity()));
  }

  private static Set<Canton> getCantons(Set<PoliticalCommunity> politicalCommunities) {
    return politicalCommunities.stream()
        .map(PoliticalCommunity::getCanton)
        .collect(Collectors.toSet());
  }

  private static Set<District> getDistricts(Set<PoliticalCommunity> politicalCommunities) {
    return politicalCommunities.stream()
        .map(PoliticalCommunity::getDistrict)
        .collect(Collectors.toSet());
  }
}
