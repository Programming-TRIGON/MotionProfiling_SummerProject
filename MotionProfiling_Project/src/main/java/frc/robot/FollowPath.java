package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

public class FollowPath extends Command {

  int pathNumber;
  EncoderFollower right, left;
  double leftCalculate, rightCalculate, gyroHeading, desiredHeading, angleDifference, turn;

  public FollowPath(int pathNumber) {
    requires(Robot.driveTrain);
    this.pathNumber = pathNumber;

    this.left = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[0]);
    this.right = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[1]);
  }

  @Override
  protected void initialize() {
    this.left.configureEncoder(Robot.driveTrain.getLeftTicks(), 50, 0.1524);
    this.left.configurePIDVA(1.0, 0.0, 0.0, 1 / 80, 0);
  }

  @Override
  protected void execute() {
    this.leftCalculate = this.left.calculate(Robot.driveTrain.getLeftTicks());
    this.rightCalculate = this.right.calculate(Robot.driveTrain.getRightTicks());

    this.gyroHeading = Robot.driveTrain.getAngle();
    this.desiredHeading = Pathfinder.r2d(this.left.getHeading());
    this.angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);

    this.angleDifference = this.angleDifference % 360.0;
    if (Math.abs(angleDifference) > 180.0) {
  angleDiff = (angleDifference > 0) ? angleDifference - 360 : angleDiff + 360;
} 

    this.turn = 0.8 * (-1.0/80.0) * this.angleDifference;

    Robot.driveTrain.tankDrive(this.leftCalculate + turn, this.rightCalculate - turn);
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
