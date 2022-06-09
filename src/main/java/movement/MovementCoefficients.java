package movement;

import lombok.Builder;

@Builder
public record MovementCoefficients(double k1,
                                   double k2,
                                   double k3,
                                   double k4,
                                   double ke,
                                   double kn,
                                   double ku,
                                   double kCommon,
                                   double criticalRadius,
                                   double dangerousRadius) {

    @Override
    public String toString() {
        return "MovementCoefficients: direction {k1=" + k1 + ", k2=" + k2 + ", k3=" + k3 + ", k4=" + k4 +
                "}, speed {ke=" + ke + ", kn=" + kn + ", ku=" + ku + ", kCommon=" + kCommon +
                "}, criticalRadius=" + criticalRadius +
                ", dangerousRadius=" + dangerousRadius;
    }
}

