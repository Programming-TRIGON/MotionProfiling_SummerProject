package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.RobotConstants.Path;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

/**
 * This command uses the paths we generated on the path creater and uses it to
 * perform the motion profiling
 */
public class FollowPath extends Command {

  private EncoderFollower right, left;
  private double leftCalculate, rightCalculate, gyroHeading, desiredHeading, angleDifference, turn, angleDiff;
  private SplitTrajectories splitTrajectories;

  /** This command gets the path number and then follows it */
  public FollowPath(Path path) {
    requires(Robot.drivetrain);
    this.splitTrajectories = new SplitTrajectories(path); // splits the path to two sides of the robot.
    this.left = new EncoderFollower(splitTrajectories.getLeftTrajectory());
    this.right = new EncoderFollower(splitTrajectories.getRightTrajectory());
  }

  @Override
  /** We configure the encoder and the PIDVA */
  protected void initialize() {
    this.left.configureEncoder(Robot.drivetrain.getLeftTicks(), RobotConstants.TICKS_PER_REVOLUTION,
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
    this.leftCalculate = this.left.calculate(Robot.drivetrain.getLeftTicks());
    this.rightCalculate = this.right.calculate(Robot.drivetrain.getRightTicks());

    this.gyroHeading = Robot.drivetrain.getAngle();
    this.desiredHeading = Pathfinder.r2d(this.left.getHeading());
    this.angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);

    this.angleDifference = this.angleDifference % 360.0;
    if (Math.abs(angleDifference) > 180.0) {
      this.angleDiff = (angleDifference > 0) ? angleDifference - 360 : angleDiff + 360;
    }

    this.turn = RobotConstants.MOTION_PROFILING_KP_TURN * (-1.0 / 80.0) * this.angleDifference;

    Robot.drivetrain.tankDrive(this.leftCalculate + turn, this.rightCalculate - turn);
  }

  @Override
  protected boolean isFinished() {
    return this.left.isFinished() && this.right.isFinished();
  }

  @Override
  protected void end() {
    Robot.drivetrain.tankDrive(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
