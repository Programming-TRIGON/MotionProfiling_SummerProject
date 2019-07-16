package frc.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
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
  ADXRS450_Gyro gyro;
  Encoder leftEncoder, rightEncoder;
  
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

  public double getAngle(){
    return this.gyro.getAngle();
  }

  public double getLeftEncoder(){
    return this.leftEncoder.getDistancePerPulse();
  }

  public double getRightEncoder(){
    return this.rightEncoder.getDistancePerPulse();
  }

  public double getEncoders(){
    return (getLeftEncoder() + getRightEncoder()) / 2;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
