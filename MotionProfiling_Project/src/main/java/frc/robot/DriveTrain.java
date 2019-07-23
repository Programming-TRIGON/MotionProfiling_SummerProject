package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.analog.adis16448.frc.ADIS16448_IMU.Axis;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
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
  //private ADXRS450_Gyro gyro;
  private ADIS16448_IMU gyro;
  private Encoder leftEncoder, rightEncoder;
  private double RIGHT_TICKS_DIVIDER = 575.0, LEFT_TICKS_DIVIDER = 771.5;
  private double prevLeftDistance = 0, prevRightDistance = 0, prevTime = 0, leftVelocity = 0, rightVelocity = 0, leftAcceleration = 0, rightAcceleration = 0,
   prevLeftVelocity = 0, prevRightVelocity = 0;
  
  public DriveTrain(){
    super();
    this.rightGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_RIGHT_MOTOR), new WPI_TalonSRX(RobotMap.CAN.FRONT_RIGHT_MOTOR));
    this.leftGroup = new SpeedControllerGroup(new WPI_TalonSRX(RobotMap.CAN.REAR_LEFT_MOTOR), new WPI_TalonSRX(RobotMap.CAN.FRONT_LEFT_MOTOR));  
    this.driveTrain = new DifferentialDrive(this.leftGroup, this.rightGroup);
    //this.gyro = new ADXRS450_Gyro();
    this.gyro = new ADIS16448_IMU();
    this.leftEncoder = new Encoder(RobotMap.DIO.DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_A, RobotMap.DIO.DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_B);
    this.rightEncoder = new Encoder(RobotMap.DIO.DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_A, RobotMap.DIO.DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_B);

    this.leftEncoder.setDistancePerPulse(LEFT_TICKS_DIVIDER);
    this.rightEncoder.setDistancePerPulse(RIGHT_TICKS_DIVIDER);

    this.leftEncoder.setReverseDirection(true);
    this.rightEncoder.setReverseDirection(false);

  }

  public void tankDrive(double leftSpeed, double rightSpeed){
    this.driveTrain.tankDrive(leftSpeed, rightSpeed);
  }

  public void arcadeDrive(double x, double y){
    this.driveTrain.arcadeDrive(y, x);
  }

  public double getAngle(){
    return this.gyro.getAngleZ();
  }

  public double getLeftDistance(){
    return this.leftEncoder.getDistance();
  }


  public double getRightDistance(){
    return this.rightEncoder.getDistance();
  }

  public double getAverageDistance(){
    return (getLeftDistance() + getRightDistance()) / 2;
  }

  public double getLeftEncoderRate(){
    return leftEncoder.getRate();
  }

  public double getRightEncoderRate(){
    return rightEncoder.getRate();
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

  public void resetEncoders() {
    this.leftEncoder.reset();
    this.rightEncoder.reset();
  }
  
  public void resetGyro() {
    this.gyro.reset();
  }

  public void calibrateGyro() {
    this.gyro.calibrate();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new DriveArcade());
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
    this.prevLeftVelocity = leftVelocity;

    this.prevRightDistance = currentRightDistance;
    this.prevRightVelocity = rightVelocity;

    this.prevTime = currentTime;

  }
}
