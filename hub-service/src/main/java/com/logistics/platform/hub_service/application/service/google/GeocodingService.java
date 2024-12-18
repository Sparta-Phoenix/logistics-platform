package com.logistics.platform.hub_service.application.service.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.logistics.platform.hub_service.presentation.response.AddressResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeocodingService {

  private String googleMapsApiKey;

  @Value("${google.maps.api.key}")
  public void setGoogleMapsApiKey(String googleMapsApiKey) {
    this.googleMapsApiKey = googleMapsApiKey;
  }

  public AddressResponse getLatLngByAddress(String address)
      throws IOException, InterruptedException, ApiException {
    String entireAddress = "대한민국 " + address;
    GeoApiContext context = new GeoApiContext.Builder()
        .apiKey(googleMapsApiKey)
        .build();
    GeocodingResult[] results = GeocodingApi.geocode(context, entireAddress)
        .region("kr")
        .await();
    if (results.length > 0) {
      GeocodingResult result = results[0];
      double latitude = result.geometry.location.lat;
      double longitude = result.geometry.location.lng;
      return new AddressResponse(latitude, longitude);
    } else {
      return null;
    }
  }
}
