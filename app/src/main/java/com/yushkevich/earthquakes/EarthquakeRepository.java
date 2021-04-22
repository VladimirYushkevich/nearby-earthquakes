package com.yushkevich.earthquakes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class EarthquakeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EarthquakeRepository.class);

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public EarthquakeRepository(HttpClient httpClient) {
        this.objectMapper = new ObjectMapper();
        this.httpClient = httpClient;
    }

    public List<Feature> getAllEarthquakes() {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(new URI("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson"))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), FeatureCollection.class).getFeatures();
        } catch (Exception e) {
            LOGGER.error("Can't fetch earthquakes", e);
        }

        return Collections.emptyList();
    }
}
