
package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utils.Logger;

public class CalibrateKv extends Command {
  // TODO: change const values.
  private static final double EPSILON_ACC = 1e-1;
  private static final double EPSILON_VEL = 1e-2;
  private static final double DELTA_VOLTAGE = 0.05;
  private static final double MAX_DISTANCE = 7.5;

  private Supplier<Double> voltageSupplier;
  private double lastRightVel;
  private double lastLeftVel;
  private boolean isReversed;
  private Logger leftLogger;
  private Logger rightLogger;
  private double voltage = 0.4;
  private double startingPoint;

  /**
   * 
   * @param isReversed should the command be run in reversed
   */
  public CalibrateKv(boolean isReversed, Supplier<Double> voltageSupplier) {
    requires(Robot.driveTrain);
    this.isReversed = isReversed;
    this.voltageSupplier = voltageSupplier;
    // Creates loggers in order to save the results.
    
  }

  @Override
  protected void initialize() {
    Robot.driveTrain.resetEncoders();
    // Gets the robot's starting point.
    voltage = voltageSupplier.get();
    lastRightVel = Math.abs(Robot.driveTrain.getRightVelocity());
    lastLeftVel = Math.abs(Robot.driveTrain.getLeftVelocity());
    startingPoint = Robot.driveTrain.getAverageDistance();
    leftLogger = new Logger((isReversed ? "leftKvReversed" : "leftKv") + ".csv", "voltage", "velocity");
    rightLogger = new Logger((isReversed ? "rightKvReversed" : "rightKv") + ".csv", "voltage", "velocity");
  }

  @Override
  protected void execute() {
    // Gives voltage to the engines.
    Robot.driveTrain.arcadeDrive(0, isReversed ? -voltage : voltage);
    double leftVelocity = Math.abs(Robot.driveTrain.getLeftVelocity());
    double rightVelocity = Math.abs(Robot.driveTrain.getRightVelocity());
    // Checks if the accleration is 0 and the velocity has changed since last time.
    if (Math.abs(Robot.driveTrain.getLeftAcceleration()) < EPSILON_ACC
        && Math.abs(Robot.driveTrain.getRightAcceleration()) < EPSILON_ACC && rightVelocity - lastRightVel > EPSILON_VEL
        && leftVelocity - lastLeftVel > EPSILON_VEL) {
      leftLogger.log(voltage, leftVelocity);
      rightLogger.log(voltage, rightVelocity);
      voltage += DELTA_VOLTAGE;
      lastLeftVel = leftVelocity;
      lastRightVel = rightVelocity;

    }

  }

  @Override
  protected boolean isFinished() {
    // Checks if the robot has moved enough.
    return Math.abs(Robot.driveTrain.getRightDistance() - startingPoint) > MAX_DISTANCE
        || Math.abs(Robot.driveTrain.getLeftDistance() - startingPoint) > MAX_DISTANCE;
  }

  @Override
  protected void end() {
    Robot.driveTrain.tankDrive(0, 0);
    // Saves the data into files.
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
