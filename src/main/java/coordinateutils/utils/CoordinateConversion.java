package coordinateutils.utils;

import coordinateutils.model.CoordinateEcef;
import coordinateutils.model.CoordinateEnu;
import coordinateutils.model.CoordinateWgs;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

public class CoordinateConversion {

    // WGS-84 geodetic constants
    private static final double a = 6378137.0;            // WGS-84 Earth semimajor axis (m)

    private static final double b = 6356752.314245;      // Derived Earth semiminor axis (m)
    private static final double f = (a - b) / a;         // Ellipsoid Flatness
    private static final double f_inv = 1.0 / f;         // Inverse flattening

    //private static final double f_inv = 298.257223563; // WGS-84 Flattening Factor of the Earth
    //private static final double b = a - a / f_inv;
    //private static final double f = 1.0 / f_inv;

    private static final double a_sq = a * a;
    private static final double b_sq = b * b;
    private static final double e_fsq = f * (2 - f); // Square of Eccentricity
    private static final double e_ssq = Math.sqrt(e_fsq); // Eccentricity

    protected static final CRSFactory crsFactory = new CRSFactory();
    protected static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    protected static final CoordinateReferenceSystem WGS84 = crsFactory.createFromName("epsg:4326");
    //protected static final CoordinateReferenceSystem ECEF = crsFactory.createFromName("epsg:4978");
    protected static final CoordinateReferenceSystem ECEF = crsFactory.createFromParameters(
            "ECEF",
            "+proj=geocent +ellps=WGS84 +datum=WGS84");

    private static final CoordinateTransform wgsToEcef = ctFactory.createTransform(WGS84, ECEF);
    private static final CoordinateTransform ecefToWgs = ctFactory.createTransform(ECEF, WGS84);

    public static CoordinateEnu ecefToEnu(
            CoordinateEcef coordinate,
            CoordinateWgs origin) {
        // Convert to radians in notation consistent with the paper:
        double lambda = Math.toRadians(origin.getLat());
        double phi = Math.toRadians(origin.getLon());
        double s = Math.sin(lambda);
        double N = a / Math.sqrt(1 - e_fsq * s * s);

        double sin_lambda = Math.sin(lambda);
        double cos_lambda = Math.cos(lambda);
        double cos_phi = Math.cos(phi);
        double sin_phi = Math.sin(phi);

        CoordinateEcef originEcef = wgsToEcef(origin);
        double x0 = originEcef.getX();
        double y0 = originEcef.getY();
        double z0 = originEcef.getZ();

        double xd, yd, zd;
        xd = coordinate.getX() - x0;
        yd = coordinate.getY() - y0;
        zd = coordinate.getZ() - z0;

        // This is the matrix multiplication
        double xEast = -sin_phi * xd + cos_phi * yd;
        double yNorth = -cos_phi * sin_lambda * xd - sin_lambda * sin_phi * yd + cos_lambda * zd;
        double zUp = cos_lambda * cos_phi * xd + cos_lambda * sin_phi * yd + sin_lambda * zd;

        return new CoordinateEnu(xEast, yNorth, zUp);
    }

    //     Converts WGS-84 Geodetic point (lat, lon, h) to the
    //     Earth-Centered Earth-Fixed (ECEF) coordinates (x, y, z).
    public static CoordinateEcef wgsToEcef(CoordinateWgs coordinateWgs) {
        // Convert to radians in notation consistent with the paper:
        double lat = Math.toRadians(coordinateWgs.getLat());
        double lon = Math.toRadians(coordinateWgs.getLon());
        double alt = coordinateWgs.getAlt();

        double N = a / Math.sqrt(1 - e_fsq * Math.pow(Math.sin(lat), 2));

        double x = (N + alt) * Math.cos(lat) * Math.cos(lon);
        double y = (N + alt) * Math.cos(lat) * Math.sin(lon);
        double z = ((1 - e_fsq) * N + alt) * Math.sin(lat);

        return new CoordinateEcef(x, y, z);
    }

    public static CoordinateEnu wgsToEnu(CoordinateWgs coordinateWgs, CoordinateWgs origin) {
        return ecefToEnu(wgsToEcef(coordinateWgs), origin);
    }

    //TODO check h, 3rd coordinate is not implemented in proj4j
    public static CoordinateWgs ecefToWgs(CoordinateEcef coordinateEcef) {
        ProjCoordinate wgs = new ProjCoordinate();
        ecefToWgs.transform(
                new ProjCoordinate(coordinateEcef.getX(), coordinateEcef.getY(), coordinateEcef.getZ()),
                wgs);
        return new CoordinateWgs(wgs);
    }
}
