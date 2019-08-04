package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class CalibrateMaxSpeed extends Command {
  double leftForwardMaxSpeed = 0;
  double leftReverseMaxSpeed = 0;
  double rightForwardMaxSpeed = 0;
  double rightReverseMaxSpeed = 0;
  boolean isReversed = false;
  public CalibrateMaxSpeed(boolean isReversed) {
    this.isReversed = isReversed;
  }
  @Override
  protected void initialize() {
  }
  @Override
  protected void execute() {
    if(this.isReversed){
      if(Robot.driveTrain.getRightVelocity() < rightReverseMaxSpeed){
        rightReverseMaxSpeed = Robot.driveTrain.getRightVelocity();
      }
      if(Robot.driveTrain.getLeftVelocity() < leftReverseMaxSpeed){
        leftReverseMaxSpeed = Robot.driveTrain.getLeftVelocity();
      }
    }
    else{
      if(Robot.driveTrain.getRightVelocity() > rightForwardMaxSpeed) {
        rightForwardMaxSpeed = Robot.driveTrain.getRightVelocity();
      }
      if(Robot.driveTrain.getLeftVelocity() > leftForwardMaxSpeed){
        leftForwardMaxSpeed = Robot.driveTrain.getLeftVelocity();
      }
    }
  }
  @Override
  protected boolean isFinished() {
    return Robot.oi.MaxSpeedStop.get();
  }
  @Override
  protected void end() {
    if(isReversed){
      System.out.println("Right reverse max speed: " + rightReverseMaxSpeed);
      System.out.println("Left reverse max speed: " + leftReverseMaxSpeed);
    }
    else{
      System.out.println("Right forward max speed: " + rightForwardMaxSpeed);
      System.out.println("Left forward max speed: " + leftForwardMaxSpeed);
    }
  }
  @Override
  protected void interrupted() {
    end();
  }
}
