
package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utils.Logger;

public class CalibrateKv extends Command {
  // TODO: change const values.
  private static final double EPSILON = 9e-2;
  private static final double DELTA_VOLTAGE = 0.01;
  private static final double MAX_DISTANCE = 2.5;

  private Supplier<Double> voltageSupplier;
  private double lastVelocity;
  private boolean isReversed;
  private Logger leftLogger;
  private Logger rightLogger;
  private double voltage = 0.45;
  private double startingPoint;

  /**
   * 
   * @param isReversed should the command be run in reversed
   */
  public CalibrateKv(boolean isReversed,Supplier<Double> voltageSupplier) {
    requires(Robot.driveTrain);
    this.isReversed = isReversed;
    this.voltageSupplier = voltageSupplier;
    //Creates loggers in order to save the results.
    leftLogger = new Logger(
        (isReversed ? "leftKvReversed" : "leftKv") + ".csv",
        "voltage", "velocity");
    rightLogger = new Logger(
        (isReversed ? "rightKvReversed" : "rightKv") + ".csv",
        "voltage", "velocity");
  }

  @Override
  protected void initialize() {
    Robot.driveTrain.resetEncoders();
    //Gets the robot's starting point.
    voltage = voltageSupplier.get();
    lastVelocity = Robot.driveTrain.getLeftVelocity() + Robot.driveTrain.getRightVelocity();
    startingPoint = Robot.driveTrain.getAverageDistance();
  }

  @Override
  protected void execute() {
    //Gives voltage to the engines.
    Robot.driveTrain.arcadeDrive(0, isReversed ? -voltage : voltage);
    double velocity = Robot.driveTrain.getLeftVelocity() + Robot.driveTrain.getRightVelocity();
    //Checks if the accleration is 0 and the velocity has changed since last time.
    if (Math.abs(Robot.driveTrain.getLeftAcceleration()) < EPSILON
        && Math.abs(Robot.driveTrain.getRightAcceleration()) < EPSILON
        && Math.abs(velocity) - Math.abs(lastVelocity) > EPSILON) {
      leftLogger.log(voltage, Math.abs(Robot.driveTrain.getLeftVelocity()));
      rightLogger.log(voltage, Math.abs(Robot.driveTrain.getRightVelocity()));
      voltage += DELTA_VOLTAGE;
      lastVelocity = velocity;
    }

  }

  @Override
  protected boolean isFinished() {
    //Checks if the robot has moved enough.
    return Math.abs(Robot.driveTrain.getAverageDistance() - startingPoint) > MAX_DISTANCE;
  }

  @Override
  protected void end() {
    Robot.driveTrain.tankDrive(0, 0);
    //Saves the data into files.
      leftLogger.close();
    rightLogger.close();
  
    
  }

  @Override
  protected void interrupted() {
    leftLogger.log("INTERRUPTED!");
    rightLogger.log("INTERRUPTED!");
    end();
  }
}
