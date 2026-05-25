package az.finalproject.msmerchant.service;

import az.finalproject.msmerchant.dto.Coordinate;
import az.finalproject.msmerchant.dto.response.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final RestTemplate restTemplate;

    public Coordinate fetchCoordinates(String addressText) {

        String url = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", addressText)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<GeocodingResponse>> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<GeocodingResponse>>() {
                    }
            );

            if (response.getBody() != null && !response.getBody().isEmpty()) {
                GeocodingResponse firstResult = response.getBody().get(0);
                return new Coordinate(Double.valueOf(firstResult.lat()), Double.valueOf(firstResult.lon()));
            } else {
                log.warn("API can not find this API: {}", addressText);
            }
        } catch (Exception e) {
            log.error("Geocoding error: {}", e.getMessage());
        }
        return null;
    }
}