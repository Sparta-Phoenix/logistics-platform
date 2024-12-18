package com.logistics.platform.hub_service.presentation.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoUtils {

  public static Point toPoint(Double lng, Double lat) {
    return new GeometryFactory().createPoint(new Coordinate(lng, lat));
  }
}