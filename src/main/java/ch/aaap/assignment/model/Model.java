package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Model {

  private final Set<PoliticalCommunity> politicalCommunities;
  private final Set<PostalCommunity> postalCommunities;
  private final Set<Canton> cantons;
  private final Set<District> districts;

  @Getter(lazy = true)
  private final Set<String> cantonCodes = extractCantonCodes();

  @Getter(lazy = true)
  private final Set<String> districtNumbers = extractDistrictNumbers();

  private Set<String> extractCantonCodes() {
    return Objects.requireNonNull(cantons).stream()
        .map(Canton::getCode)
        .collect(Collectors.toSet());
  }

  private Set<String> extractDistrictNumbers() {
    return Objects.requireNonNull(districts).stream()
        .map(District::getNumber)
        .collect(Collectors.toSet());
  }
}
