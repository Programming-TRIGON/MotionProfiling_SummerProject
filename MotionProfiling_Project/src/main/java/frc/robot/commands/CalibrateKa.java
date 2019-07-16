/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utils.Logger;

public class CalibrateKa extends Command {
  // TODO: change values.
  private static final double EPSILON_ACC = 1e-15;
  private static final double EPSILON_VEL = 1e-15;
  private static final double VOLTAGE = 0.3;
  private static final double MAX_DISTANCE = 2;

  private Logger leftLogger;
  private Logger rightLogger;
  private boolean isReversed;
  private double startingPoint;
  private double leftKv;
  private double leftVi;
  private double rightKv;
  private double rightVi;
/**
 * 
 * @param leftKv kv of the left motor
 * @param rightKv  kv of the right motor
 * @param LeftVi  vi of the left motor
 * @param rightVi  vi of the right motor
 * @param isReversed should the command be run in reversed
 */
  public CalibrateKa(double leftKv, double rightKv, double LeftVi, double rightVi, boolean isReversed) {
    requires(Robot.driveTrain);

    this.isReversed = isReversed;
    this.leftKv = leftKv;
    this.leftVi = LeftVi;
    this.rightKv = rightKv;
    this.rightVi = rightVi;
     //Creates loggers in order to save the results.
    leftLogger = new Logger((isReversed ? "leftKaReversed" : "leftKa") + ".csv", "voltage acceleration",
        "acceleration");
    leftLogger = new Logger((isReversed ? "rightKaReversed" : "rightKa") + ".csv", "voltage acceleration",
        "acceleration");
  }

  @Override
  protected void initialize() {
    //Gets the robot's starting point.
    startingPoint = Robot.driveTrain.getEncoders();
  }

  
  @Override
  protected void execute() {
    //Gives voltage to the engines.
    Robot.driveTrain.arcadeDrive(0, isReversed ? -VOLTAGE : VOLTAGE);
    //Calculates (voltage - kv*velocity-ki) and logs the result.
    double leftVoltageAcceleration = VOLTAGE - leftKv * Math.abs(Robot.driveTrain.getLeftVelocity()) - leftVi;
    double rightVoltageAcceleration = VOLTAGE - rightKv * Math.abs(Robot.driveTrain.getRightVelocity()) - rightVi;
    leftLogger.log(leftVoltageAcceleration, Robot.driveTrain.getLeftAcceleration());
    rightLogger.log(rightVoltageAcceleration, Robot.driveTrain.getRightAcceleration());
  }

  
  @Override
  protected boolean isFinished() {
    //Checks if the robot stoped accelerating or reached it's max distance.
    double velocity = (Robot.driveTrain.getLeftVelocity() + Robot.driveTrain.getRightVelocity()) / 2;
    double acc = (Robot.driveTrain.getLeftAcceleration() + Robot.driveTrain.getRightAcceleration()) / 2;
    return Robot.driveTrain.getEncoders() - startingPoint > MAX_DISTANCE
        || (Math.abs(velocity) > EPSILON_VEL && Math.abs(acc) < EPSILON_ACC);
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
