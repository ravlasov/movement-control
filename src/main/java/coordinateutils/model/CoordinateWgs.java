package coordinateutils.model;

import coordinateutils.utils.CoordinateConversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.proj4j.ProjCoordinate;

/**
 * Geodetic coordinate
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoordinateWgs implements Cloneable {

    private double lon = 0.0;
    private double lat = 0.0;
    private double alt = 0.0;

    public CoordinateWgs(ProjCoordinate projCoordinate) {
        this(projCoordinate.x, projCoordinate.y, projCoordinate.z);
    }

    public CoordinateEcef getEcef() {
        return CoordinateConversion.wgsToEcef(this);
    }

    public CoordinateEnu getEnu(CoordinateWgs origin) {
        return CoordinateConversion.wgsToEnu(this, origin);
    }

    public CoordinateWgs setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public CoordinateWgs setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public CoordinateWgs setAlt(double alt) {
        this.alt = alt;
        return this;
    }

    @Override
    public String toString() {
        return "WGS{lon=" + lon + ", lat=" + lat + ", alt=" + alt + '}';
    }

    @Override
    public CoordinateWgs clone() {
        try {
            return (CoordinateWgs) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
