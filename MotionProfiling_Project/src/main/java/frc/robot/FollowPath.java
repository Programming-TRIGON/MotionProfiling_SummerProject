package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

/**
 * This command uses the paths we generated on the path creater and uses it to
 * perform the motion profiling
 */
public class FollowPath extends Command {

  private int pathNumber;
  private EncoderFollower right, left;
  private double leftCalculate, rightCalculate, gyroHeading, desiredHeading, angleDifference, turn, angleDiff;

  /** This command gets the path number and then follows it */
  public FollowPath(int pathNumber) {
    requires(Robot.driveTrain);
    this.pathNumber = pathNumber;

    this.left = new EncoderFollower(Robot.pathCreater.getSplitTrajectories(this.pathNumber)[0]);
    this.right = new EncoderFollower(Robot.pathCreater.getSplitTrajectories(this.pathNumber)[1]);
  }

  @Override
  /** We configure the encoder and the PIDVA */
  protected void initialize() {
    this.left.configureEncoder(Robot.driveTrain.getLeftTicks(), RobotConstants.TICKS_PER_REVOLUTION,
        RobotConstants.WHEEL_DIAMETER);
    this.left.configurePIDVA(RobotConstants.MOTION_PROFILING_PID_SETTINGS.KP, 0,
        RobotConstants.MOTION_PROFILING_PID_SETTINGS.KD, 1 / RobotConstants.MOTION_PROFILING_PID_SETTINGS.KV,
        RobotConstants.MOTION_PROFILING_PID_SETTINGS.KA);
  }

  @Override
  /**
   * We calculate the needed power , then we calculate the heading of the gyro to
   * acount for the heading of the robot. The power we give to the motors is the
   * calculation in the beginning - / + the KP.
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

    this.turn = RobotConstants.MOTION_PROFILING_KP_TURN * (-1.0 / 80.0) * this.angleDifference;

    Robot.driveTrain.tankDrive(this.leftCalculate + turn, this.rightCalculate - turn);
  }

  @Override
  protected boolean isFinished() {
    return this.left.isFinished() && this.right.isFinished();
  }

  @Override
  protected void end() {
    Robot.driveTrain.tankDrive(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
