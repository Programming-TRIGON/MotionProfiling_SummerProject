package frc.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is the drive, chassis, subsystem.
 */
public class DriveTrain extends Subsystem {
  
  SpeedControllerGroup leftGroup, rightGroup; 
  DifferentialDrive driveTrain;
  
  public DriveTrain(SpeedController rearLeft, SpeedController frontLeft, SpeedController rearRight, SpeedController frontRight){
    this.rightGroup = new SpeedControllerGroup(rearRight, frontRight);
    this.leftGroup = new SpeedControllerGroup(rearLeft, frontLeft);  
    
    this.driveTrain = new DifferentialDrive(leftGroup,rightGroup); 
  }

  public void tankDrive(double leftSpeed, double rightSpeed){
    this.driveTrain.tankDrive(leftSpeed, rightSpeed);
  }

  public void arcadeDrive(double x, double y){
    this.driveTrain.arcadeDrive(y, x);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
