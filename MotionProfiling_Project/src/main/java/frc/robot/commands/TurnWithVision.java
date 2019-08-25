package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.PidSettings;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;

public class TurnWithVision extends Command {
  private double lastTimeOnTarget, deltaTime;
  private int pipline;
  private PIDController pIDControllerY, pIDControllerX;
  private PidSettings pidSettingsY, pidSettingsX;
  private boolean isFollowingDistance;

  public TurnWithVision(PidSettings pidSettingsY, PidSettings pidSettingsX, Target target, double deltaTime) {
    requires(Robot.driveTrain);
    this.pipline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettingsY = pidSettingsY;
    this.isFollowingDistance = true;
  }

  public TurnWithVision(PidSettings pidSettingsX, Target target, double deltaTime) {
    requires(Robot.driveTrain);
    this.pipline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettingsX = pidSettingsX;
    this.isFollowingDistance = false;
  }

  @Override
  protected void initialize() {
    // setting pidY values
    pIDControllerX = new PIDController(pidSettingsX.getKP(), pidSettingsX.getKI(), pidSettingsX.getKD());
    pIDControllerX.setSetpoint(0);
    pIDControllerX.setInputRange(-27, 27);
    pIDControllerX.setOutputRange(-1, 1);
    pIDControllerX.setAbsoluteTolerance(pidSettingsX.getTolerance(), pidSettingsX.getDeltaTolerance());
    // setting pidX values
    if (isFollowingDistance) {
      pIDControllerY = new PIDController(pidSettingsY.getKP(), pidSettingsY.getKI(), pidSettingsY.getKD());
      pIDControllerY.setSetpoint(RobotConstants.RobotDimensions.LIMELIGHT_DISTANCE_OFFSET + pidSettingsY.getSetpoint());
      pIDControllerY.setOutputRange(-1, 1);
      pIDControllerY.setAbsoluteTolerance(pidSettingsY.getTolerance(), pidSettingsY.getDeltaTolerance());
    }
    // setting limelight settings
    Robot.limelight.setPipeline(pipline);
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    // if it sees a target it will do pid on the x axis else it will not move
//    if (Robot.limelight.getTv()) {
//      Robot.driveTrain.arcadeDrive(-pIDControllerX.calculate(Robot.limelight.getTx()),
//          isFollowingDistance ? pIDControllerY.calculate(Robot.limelight.getDistance()) : 0);
//      lastTimeOnTarget = Timer.getFPGATimestamp();
//    } else {
//      Robot.driveTrain.arcadeDrive(0, 0);
    //}
    Robot.driveTrain.arcadeDrive(pIDControllerX.calculate(Robot.limelight.getTx()),0);
  }

  @Override
  protected boolean isFinished() {
    // if it does not detect a target for delta time it will return true
    return Timer.getFPGATimestamp() - lastTimeOnTarget > deltaTime || isFollowingDistance
        ? (pIDControllerX.atSetpoint() && pIDControllerY.atSetpoint())
        : pIDControllerX.atSetpoint();
  }

  @Override
  protected void end() {
    Robot.driveTrain.arcadeDrive(0, 0);
    //Robot.limelight.setCamMode(CamMode.driver);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
