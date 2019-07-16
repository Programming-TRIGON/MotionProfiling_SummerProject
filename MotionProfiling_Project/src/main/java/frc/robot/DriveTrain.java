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
  double TICKS_DIVIDER = 500;
  
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
    return this.leftEncoder.getDistancePerPulse() / TICKS_DIVIDER;
  }


  public double getRightEncoder(){
    return this.rightEncoder.getDistancePerPulse() / TICKS_DIVIDER;
  }

  public double getEncoders(){
    return (getLeftEncoder() + getRightEncoder()) / 2;
  }
  public double getLeftAcceleration(){
    return 0;
  }
  public double getRightAcceleration(){
    return 0;
  }
  public double getRightVelocity(){
    return 0;
  }
  public double getLeftVelocity(){
    return 0;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
