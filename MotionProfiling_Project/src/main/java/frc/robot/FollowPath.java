package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.followers.EncoderFollower;

public class FollowPath extends Command { 
  int pathNumber;
  EncoderFollower right, left;

  public FollowPath(int pathNumber) {
    requires(Robot.driveTrain);
    this.pathNumber = pathNumber;
    this.left = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[0]);
    this.right = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[1]);
  }

  @Override
  protected void initialize() {
    left.configureEncoder((int)Robot.driveTrain.getLeftDistance(), 50, 0.1524);
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
