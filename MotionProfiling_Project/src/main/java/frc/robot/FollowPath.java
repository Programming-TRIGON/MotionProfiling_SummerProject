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
    this.left.configureEncoder((int)Robot.driveTrain.getLeftDistance(), 50, 0.1524);
    this.left.configurePIDVA(1.0, 0.0, 0.0, 1 / 80, 0);
  }

  @Override
  protected void execute() {
    double l = left.calculate(Robot.driveTrain.getLeftDistance());
    double r = right.calculate(Robot.driveTrain.getRightDistance());

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
