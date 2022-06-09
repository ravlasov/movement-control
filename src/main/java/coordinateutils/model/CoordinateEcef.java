package coordinateutils.model;

import coordinateutils.utils.CoordinateConversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.proj4j.ProjCoordinate;

/**
 * Geocentric cartesian coordinate
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoordinateEcef implements Cloneable {

    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public CoordinateEcef(ProjCoordinate projCoordinate) {
        this(projCoordinate.x, projCoordinate.y, projCoordinate.z);
    }

    public CoordinateWgs getWgs() {
        return CoordinateConversion.ecefToWgs(this);
    }

    public CoordinateEnu getEnu(CoordinateWgs origin) {
        return CoordinateConversion.ecefToEnu(this, origin);
    }

    public CoordinateEcef setX(double x) {
        this.x = x;
        return this;
    }

    public CoordinateEcef setY(double y) {
        this.y = y;
        return this;
    }

    public CoordinateEcef setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return "ECEF{x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public CoordinateEcef clone() {
        try {
            return (CoordinateEcef) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
