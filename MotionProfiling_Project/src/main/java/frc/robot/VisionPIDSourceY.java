

package frc.robot;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;


public class VisionPIDSourceY implements PIDSource{
    
    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return Robot.limelight.getDistance();
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }
}