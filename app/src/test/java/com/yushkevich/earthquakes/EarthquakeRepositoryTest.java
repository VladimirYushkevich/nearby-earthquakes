package com.yushkevich.earthquakes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EarthquakeRepositoryTest {

    private static HttpClient httpClient;
    private static HttpResponse<String> httpResponse;

    private EarthquakeRepository earthquakeRepository;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void beforeAll() {
        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
    }

    @BeforeEach
    void setUp() {
        earthquakeRepository = new EarthquakeRepository(httpClient);
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_get_all_earthquake_features_should_return_not_empty_list_on_success() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body())
                .thenReturn(Files.readString(Path.of(
                        Objects.requireNonNull(getClass().getClassLoader().getResource("all_month.geojson")).toURI())
                ));

        assertFalse(earthquakeRepository.getAllEarthquakes().isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void test_get_all_earthquake_features_should_return_empty_list_on_failure() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new RuntimeException("something went wrong"));

        assertTrue(earthquakeRepository.getAllEarthquakes().isEmpty());
    }
}
