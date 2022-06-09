package movement;

import coordinateutils.model.CoordinateEnu;
import coordinateutils.model.CoordinateWgs;
import coordinateutils.utils.CoordinateMath;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MovementController {

    private final Controller parent;
    private final UavControl uav;

    private MovementCoefficients movementCoefficients;

    public MovementController(UavControl uav) {
        this.parent = uav.getParent();
        this.uav = uav;
    }

    private CoordinateWgs getSwarmCenter() {
        final CoordinateWgs[] sumPosition = {new CoordinateWgs()};
        Set<UavControl> swarm = parent.getSwarm();
        swarm.forEach(militant -> sumPosition[0] = CoordinateMath.sum(
                sumPosition[0],
                militant.getCachedPosition()));
        return CoordinateMath.multiplyScalar(sumPosition[0], 1d / swarm.size());
    }

    private CoordinateEnu calcSpeedToDestination() {
        CoordinateEnu destinationEnu = uav.getTargetPoint().getEnu(uav.getCachedPosition());
        log.info("Destination: {}", destinationEnu);
        destinationEnu = CoordinateMath.norm(destinationEnu);
        log.debug("SpeedToDestination normalized: {}", destinationEnu);
        return destinationEnu;
    }

    private CoordinateEnu calcSpeedToSwarmCenter() {
        CoordinateEnu swarmCenter = getSwarmCenter().getEnu(uav.getCachedPosition());
        log.debug("SwarmCenter: {}", swarmCenter);
        swarmCenter = CoordinateMath.norm(swarmCenter);
        log.debug("SpeedToSwarmCenter normalized: {}", swarmCenter);
        return swarmCenter;
    }

    private CoordinateEnu calcSpeedFromNearby() {
        AtomicReference<CoordinateEnu> speed = new AtomicReference<>(new CoordinateEnu());
        parent.getSwarm().forEach(nearbyMilitant -> {
            if (nearbyMilitant.equals(uav)) {
                return;
            }
            CoordinateEnu difference = uav.getCachedPosition().getEnu(nearbyMilitant.getCachedPosition());
            double distance = CoordinateMath.length(difference); // todo distance depends on longitude
            if (distance > movementCoefficients.dangerousRadius()) {
                return;
            }
            double k_crit =
                    movementCoefficients.criticalRadius() / Math.pow(
                            distance - movementCoefficients.criticalRadius(), 2);
            if (distance < movementCoefficients.criticalRadius()) {
                log.warn(
                        "Critical distance {} <-> {}: {} m.",
                        nearbyMilitant.getName(),
                        uav.getName(),
                        distance);
            }
            CoordinateEnu v3i = CoordinateMath.multiplyScalar(difference, k_crit);
            speed.set(CoordinateMath.sum(speed.get(), v3i));
        });
        log.debug("SpeedFromNearby: {}", speed.get());
        return speed.get();
    }

    private CoordinateEnu calcRandomSpeed() {
        CoordinateEnu speed = CoordinateMath.norm(new CoordinateEnu(
                Math.random() - 0.5,
                Math.random() - 0.5,
                Math.random() - 0.5));
        log.debug("RandomSpeed normalized: {}", speed);
        return speed;
    }

    public CoordinateEnu calculateVelocity() {
        this.movementCoefficients = uav.getMovementCoefficients();
        log.debug(movementCoefficients.toString());

        CoordinateEnu speed = CoordinateMath.multiplyScalar(
                CoordinateMath.sum(
                        CoordinateMath.multiplyScalar(calcSpeedToDestination(), movementCoefficients.k1()),
                        CoordinateMath.multiplyScalar(calcSpeedToSwarmCenter(), movementCoefficients.k2()),
                        CoordinateMath.multiplyScalar(calcSpeedFromNearby(), movementCoefficients.k3()),
                        CoordinateMath.multiplyScalar(calcRandomSpeed(), movementCoefficients.k4())),
                movementCoefficients.kCommon());

        log.debug("Final speed: {}", speed);

        speed = speed
                .setX(Math.min(9.9, Math.max(-9.9, movementCoefficients.ke() * speed.getX())))
                .setY(Math.min(9.9, Math.max(-9.9, movementCoefficients.kn() * speed.getY())))
                .setZ(Math.min(4.9, Math.max(-4.9, -movementCoefficients.ku() * speed.getZ())));
        log.debug("Final speed normalized: {}", speed);

        return speed;
    }

}
