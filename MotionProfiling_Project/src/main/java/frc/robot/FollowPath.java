package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

/**
 * this command uses the paths we generated on the path creater and uses it to
 * perform the motion profiling
 */
public class FollowPath extends Command {

  int pathNumber;
  EncoderFollower right, left;
  double leftCalculate, rightCalculate, gyroHeading, desiredHeading, angleDifference, turn, angleDiff;

  /** we get the correct path and creat the encoder follower objects */
  public FollowPath(int pathNumber) {
    requires(Robot.driveTrain);
    this.pathNumber = pathNumber;

    this.left = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[0]);
    this.right = new EncoderFollower(Robot.pathCreater.getTrajectories(this.pathNumber)[1]);
  }

  @Override
  /** we configure the encoder and the pidva to be ready of rmotion profiling */
  protected void initialize() {
    this.left.configureEncoder(Robot.driveTrain.getLeftTicks(), RobotConstants.TICKS_PER_REV,
        RobotConstants.WHEEL_DIAMETER);
    this.left.configurePIDVA(RobotConstants.MotionProfilingPIDSettings.KP, 0,
        RobotConstants.MotionProfilingPIDSettings.KD, 1 / RobotConstants.MotionProfilingPIDSettings.KV,
        RobotConstants.MotionProfilingPIDSettings.KA);
  }

  @Override
  /**
   * we calculate the needed powere for the motion profiling, then we calculate
   * the heading of the gyro to acount for the heading of the robot, the math is
   * the KP. 
   * the power we give to the motors is the calculation - / + the KP.
   */
  protected void execute() {
    this.leftCalculate = this.left.calculate(Robot.driveTrain.getLeftTicks());
    this.rightCalculate = this.right.calculate(Robot.driveTrain.getRightTicks());

    this.gyroHeading = Robot.driveTrain.getAngle();
    this.desiredHeading = Pathfinder.r2d(this.left.getHeading());
    this.angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);

    this.angleDifference = this.angleDifference % 360.0;
    if (Math.abs(angleDifference) > 180.0) {
      this.angleDiff = (angleDifference > 0) ? angleDifference - 360 : angleDiff + 360;
    }

    this.turn = RobotConstants.kP_TURN * (-1.0 / 80.0) * this.angleDifference;

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
