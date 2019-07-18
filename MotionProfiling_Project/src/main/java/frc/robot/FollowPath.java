package frc.robot;

import edu.wpi.first.wpilibj.command.Command;

public class FollowPath extends Command { 
  int pathNumber;

  public FollowPath(int pathNumber) {
    requires(Robot.driveTrain);
    this.pathNumber = pathNumber;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
  }
}
