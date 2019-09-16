
package frc.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class VisionPIDSourceX implements PIDSource{
    
    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return Robot.limelight.getTx();
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }
}
