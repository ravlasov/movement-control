package coordinateutils.utils;

import coordinateutils.model.CoordinateEcef;
import coordinateutils.model.CoordinateEnu;
import coordinateutils.model.CoordinateWgs;

public class CoordinateMath {

    public static CoordinateWgs sum(CoordinateWgs... coordinates) {
        double xSum = 0.0;
        double ySum = 0.0;
        double zSum = 0.0;
        for (CoordinateWgs coordinate : coordinates) {
            xSum += coordinate.getLon();
            ySum += coordinate.getLat();
            zSum += coordinate.getAlt();
        }
        return new CoordinateWgs(xSum, ySum, zSum);
    }

    public static CoordinateEcef sum(CoordinateEcef... coordinates) {
        double xSum = 0.0;
        double ySum = 0.0;
        double zSum = 0.0;
        for (CoordinateEcef coordinate : coordinates) {
            xSum += coordinate.getX();
            ySum += coordinate.getY();
            zSum += coordinate.getZ();
        }
        return new CoordinateEcef(xSum, ySum, zSum);
    }

    public static CoordinateEnu sum(CoordinateEnu... coordinates) {
        double xSum = 0.0;
        double ySum = 0.0;
        double zSum = 0.0;
        for (CoordinateEnu coordinate : coordinates) {
            xSum += coordinate.getX();
            ySum += coordinate.getY();
            zSum += coordinate.getZ();
        }
        return new CoordinateEnu(xSum, ySum, zSum);
    }

    public static CoordinateWgs multiplyScalar(CoordinateWgs coordinate, double multiplier) {
        return new CoordinateWgs(
                coordinate.getLon() * multiplier,
                coordinate.getLat() * multiplier,
                coordinate.getAlt() * multiplier
        );
    }

    public static CoordinateEnu multiplyScalar(CoordinateEnu coordinate, double multiplier) {
        return new CoordinateEnu(
                coordinate.getX() * multiplier,
                coordinate.getY() * multiplier,
                coordinate.getZ() * multiplier
        );
    }

    public static CoordinateEcef multiplyScalar(CoordinateEcef coordinate, double multiplier) {
        return new CoordinateEcef(
                coordinate.getX() * multiplier,
                coordinate.getY() * multiplier,
                coordinate.getZ() * multiplier
        );
    }

    public static CoordinateWgs abs(CoordinateWgs coordinate) {
        return new CoordinateWgs(
                Math.abs(coordinate.getLon()),
                Math.abs(coordinate.getLat()),
                Math.abs(coordinate.getAlt()));
    }

    public static CoordinateEnu abs(CoordinateEnu coordinate) {
        return new CoordinateEnu(
                Math.abs(coordinate.getX()),
                Math.abs(coordinate.getY()),
                Math.abs(coordinate.getZ()));
    }

    public static CoordinateEcef abs(CoordinateEcef coordinate) {
        return new CoordinateEcef(
                Math.abs(coordinate.getX()),
                Math.abs(coordinate.getY()),
                Math.abs(coordinate.getZ()));
    }

    public static CoordinateEnu norm(CoordinateEnu coordinateEnu) {
        if (length(coordinateEnu) == 0) {
            return coordinateEnu;
        }
        return multiplyScalar(coordinateEnu, 1d / length(coordinateEnu));
    }

    public static double length(CoordinateEnu coordinateEnu) {
        return Math.sqrt(Math.pow(coordinateEnu.getX(), 2) + Math.pow(coordinateEnu.getY(), 2) + Math.pow(
                coordinateEnu.getZ(),
                2));
    }

    public static double length(CoordinateEcef coordinateEcef) {
        return Math.sqrt(Math.pow(coordinateEcef.getX(), 2) + Math.pow(coordinateEcef.getY(), 2) + Math.pow(
                coordinateEcef.getZ(),
                2));
    }
}
