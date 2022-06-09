package movement;

import coordinateutils.model.CoordinateWgs;

public interface UavControl {

    CoordinateWgs getCachedPosition();

    CoordinateWgs getTargetPoint();

    MovementCoefficients getMovementCoefficients();

    String getName();

    Controller getParent();
}
