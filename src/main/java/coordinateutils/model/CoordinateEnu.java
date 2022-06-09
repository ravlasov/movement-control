package coordinateutils.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Topocentric cartesian coordinate
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoordinateEnu implements Cloneable {

    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    public CoordinateEnu setX(double x) {
        this.x = x;
        return this;
    }

    public CoordinateEnu setY(double y) {
        this.y = y;
        return this;
    }

    public CoordinateEnu setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return "ENU{x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public CoordinateEnu clone() {
        try {
            return (CoordinateEnu) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
