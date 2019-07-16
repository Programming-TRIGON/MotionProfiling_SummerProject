package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is the drive, chassis, subsystem.
 */
public class DriveTrain extends Subsystem {
  
  private SpeedControllerGroup leftGroup, rightGroup; 
  private DifferentialDrive driveTrain;
  private ADXRS450_Gyro gyro;
  private Encoder leftEncoder, rightEncoder;
  private double TICKS_DIVIDER = 50;
  private double prevLeftDistance, prevRightDistance, prevTime, leftVelocity, rightVelocity, leftAcceleration, rightAcceleration,
   prevLeftVelocity, prevRightVelocity;
  
  public DriveTrain(){
    this.rightGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_RIGHT_MOTOR), new WPI_TalonSRX(RobotMap.CAN.FRONT_RIGHT_MOTOR));
    this.leftGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_LEFT_MOTOR), new WPI_TalonSRX(RobotMap.CAN.FRONT_LEFT_MOTOR));  
    
    this.driveTrain = new DifferentialDrive(this.leftGroup, this.rightGroup); 
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

  public double getLeftDistance(){
    return this.leftEncoder.getDistancePerPulse() / TICKS_DIVIDER;
  }

  public double getRightDistance(){
    return this.rightEncoder.getDistancePerPulse() / TICKS_DIVIDER;
  }

  public double getAverageDistance(){
    return (getLeftDistance() + getRightDistance()) / 2;
  }

  public double getRightVelocity(){
    return this.rightVelocity;
  }

  public double getLeftVelocity(){
    return this.leftVelocity;
  }

  public double getRightAcceleration() {
    return this.rightAcceleration;
  }

  public double getLeftAcceleration() {
    return this.leftAcceleration;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  @Override
  public void periodic() {
    double currentLeftDistance = getLeftDistance();

    double currentRightDistance = getRightDistance();
    double currentTime = Timer.getFPGATimestamp();

    this.leftVelocity = (currentLeftDistance - prevLeftDistance) / (currentTime - prevTime);
    this.leftAcceleration = (leftVelocity - prevLeftVelocity) / (currentTime - prevTime);

    this.rightVelocity = (currentRightDistance - prevRightDistance) / (currentTime - prevTime);
    this.rightAcceleration = (rightVelocity - prevRightVelocity) / (currentTime - prevTime);

    this.prevLeftDistance = currentLeftDistance;
    this.leftVelocity = prevLeftVelocity;

    this.prevRightDistance = currentRightDistance;
    this.rightVelocity = prevRightVelocity;

    this.prevTime = currentTime;

  }
}
