package com.yushkevich.earthquakes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EarthquakeServiceTest {

    private static EarthquakeRepository earthquakeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private EarthquakeService earthquakeService;

    @BeforeAll
    static void beforeAll() {
        earthquakeRepository = mock(EarthquakeRepository.class);
    }

    @BeforeEach
    void setUp() {
        earthquakeService = new EarthquakeService(earthquakeRepository);
    }

    @Test
    void test_nearest_earthquakes() throws Exception {
        when(earthquakeRepository.getAllEarthquakes()).thenReturn(
                objectMapper.readValue(
                        Files.readString(Path.of(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("cities.geojson")).toURI())),
                        FeatureCollection.class).getFeatures());

        assertEquals(Arrays.asList("Dresden || 165", "Warsaw || 517", "Amsterdam || 576", "Paris || 877", "Minsk || 953"),
                earthquakeService.getNearestEarthquakes(52.520008d, 13.404954d, 10).map(Earthquake::toString).collect(Collectors.toList()),
                "should order by distance to Berlin");
        assertEquals(Arrays.asList("Paris || 342", "Amsterdam || 357", "Dresden || 963", "Warsaw || 1448", "Minsk || 1871"),
                earthquakeService.getNearestEarthquakes(51.509865d, -0.118092d, 10).map(Earthquake::toString).collect(Collectors.toList()),
                "should order by distance to London");
        assertEquals(Arrays.asList("Paris || 342", "Amsterdam || 357"),
                earthquakeService.getNearestEarthquakes(51.509865d, -0.118092d, 2).map(Earthquake::toString).collect(Collectors.toList()),
                "should limit top N");
    }
}
