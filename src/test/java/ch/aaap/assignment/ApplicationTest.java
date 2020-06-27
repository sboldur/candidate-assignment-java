package ch.aaap.assignment;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationTest {

  /* The system under test */
  Application sut = new Application();

  @Test
  public void testModel() {
    assertEquals(26, sut.getModel().getCantons().size(), "Correct amount of cantons");
    assertEquals(143, sut.getModel().getDistricts().size(), "Correct amount of districts");
    assertEquals(
        2215,
        sut.getModel().getPoliticalCommunities().size(),
        "Correct amount of political communities");
    assertEquals(
        4064, sut.getModel().getPostalCommunities().size(), "Correct amount of postal communities");
  }

  @Test
  public void returnsCorrectAmountOfPoliticalCommunitiesInCanton() {
    assertEquals(
        162,
        sut.getAmountOfPoliticalCommunitiesInCanton("ZH"),
        "Correct amount of political communities in canton with short code 'ZH'");
    assertEquals(
        6,
        sut.getAmountOfPoliticalCommunitiesInCanton("AI"),
        "Correct amount of political communities in canton with short code 'AI'");
    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfPoliticalCommunitiesInCanton("XX"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectAmountOfDistrictsInCanton() {
    assertEquals(
        12,
        sut.getAmountOfDistrictsInCanton("ZH"),
        "Correct amount of districts in canton with short code 'ZH'");
    assertEquals(
        11,
        sut.getAmountOfDistrictsInCanton("GR"),
        "Correct amount of districts in canton with short code 'GR'");
    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfDistrictsInCanton("XX"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsAmountOfPoliticalCommunitiesInDistrict() {
    assertEquals(
        14,
        sut.getAmountOfPoliticalCommunitiesInDistrict("101"),
        "Correct amount of political communities in in distric with number '101'");

    assertThrows(
        IllegalArgumentException.class,
        () -> sut.getAmountOfPoliticalCommunitiesInDistrict("9999"),
        "Expected IllegalArgumentException");
  }

  @Test
  public void returnsCorrectDistrictNamesForZipCode() {
    assertEquals(
        "Bezirk Bülach",
        sut.getDistrictsForZipCode("8305").iterator().next(),
        "Correct district name for zip code '8305'");
    assertEquals(
        "Region Albula",
        sut.getDistrictsForZipCode("7457").iterator().next(),
        "Correct district name for zip code '7457'");

    assertEquals(0, sut.getDistrictsForZipCode("9999").size(), "Expected 0 results");
    assertEquals(2, sut.getDistrictsForZipCode("8866").size(), "Expected 2 results");
  }

  @Test
  public void returnsCorrectLastUpdateOfMunicipalityByPostalCommunityName() {
    assertEquals(
        "2016-04-10",
        sut.getLastUpdateOfPoliticalCommunityByPostalCommunityName("Vergeletto").toString(),
        "Correct last update of political community by postal community name 'Vergeletto'");
  }

  @Test
  public void returnsCorrectAmountOfCantons() {
    assertEquals(26, sut.getAmountOfCantons(), "Correct amount of cantons");
  }

  @Test
  public void returnsAmountOfPoliticalCommunityWithoutPostalCommunities() {
    assertEquals(
        3,
        sut.getAmountOfPoliticalCommunityWithoutPostalCommunities(),
        "Correct amount of political communities without postal communities");
  }

  @Test
  public void returnCountOfDistrictsByCanton() {
    Map<String, Long> countOfDistrictsByCanton = sut.getCountOfDistrictsByCantonSorted();
    assertEquals(
        26, countOfDistrictsByCanton.size(), "Correct count of districts grouped by canton");
    Map.Entry<String, Long> cantonWithHighestAmountOfDistricts =
        countOfDistrictsByCanton.entrySet().iterator().next();
    assertEquals(
        "VS",
        cantonWithHighestAmountOfDistricts.getKey(),
        "Correct canton with highest amount of districts");
    assertEquals(
        13,
        (long) cantonWithHighestAmountOfDistricts.getValue(),
        "Correct count of highest amount of districts per canton");
  }

  @Test
  public void returnCountOfPoliticalCommunitiesByCanton() {
    Map<String, Long> politicalCommunitiesCountByCanton =
        sut.countPoliticalCommunitiesByCantonSorted();
    assertEquals(
        26,
        politicalCommunitiesCountByCanton.size(),
        "Correct count of political communities grouped by canton");

    Map.Entry<String, Long> cantonWithHighestAmountOfDistricts =
        politicalCommunitiesCountByCanton.entrySet().iterator().next();

    assertEquals(
        "BE",
        cantonWithHighestAmountOfDistricts.getKey(),
        "Correct canton with highest amount of political communities");
    assertEquals(
        346,
        (long) cantonWithHighestAmountOfDistricts.getValue(),
        "Correct count of highest amount of political communities per canton");
  }
}
