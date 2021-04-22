package com.yushkevich.earthquakes;

import org.geojson.Feature;
import org.geojson.Point;

import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toCollection;

public class EarthquakeService {

    private final EarthquakeRepository earthquakeRepository;

    public EarthquakeService(EarthquakeRepository earthquakeRepository) {
        this.earthquakeRepository = earthquakeRepository;
    }

    /**
     * Returns top nearest earthquakes to point of interests based on following criteria
     *
     * @param lat   Latitude of point of interests
     * @param lon   Longitude of point of interests
     * @param limit Top limit
     * @return Stream of {@link com.yushkevich.earthquakes.Earthquake} objects
     */
    public Stream<Earthquake> getNearestEarthquakes(double lat, double lon, int limit) {
        return earthquakeRepository.getAllEarthquakes().stream()
                .map(f -> from(f, lat, lon))
                .collect(toCollection(() -> new TreeSet<>(comparingInt(Earthquake::getDistance))))
                .stream()
                .limit(limit);
    }

    private Earthquake from(Feature feature, double fromLat, double fromLon) {
        var title = feature.getProperty("title").toString();
        var distance = distance(fromLat, ((Point) feature.getGeometry()).getCoordinates().getLatitude(),
                fromLon, ((Point) feature.getGeometry()).getCoordinates().getLongitude());

        return new Earthquake(title, distance);
    }

    /**
     * (Copied from StackOverflow)
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point
     *
     * @returns Distance in km
     */
    private int distance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        distance = Math.pow(distance, 2);

        return (int) Math.sqrt(distance);
    }
}
