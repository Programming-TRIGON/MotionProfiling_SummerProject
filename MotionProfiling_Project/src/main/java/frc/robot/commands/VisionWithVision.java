package frc.robot.commands;

import com.spikes2212.utils.PIDSettings;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;

public class VisionWithVision extends Command {
  private double lastTimeOnTarget, deltaTime, ySetpoint, xSetpoint, deltaToleranceY, deltaToleranceX;
  private int pipline;
  private PIDController pIDControllerY, pIDControllerX;
  private PIDSettings pidSettingsY, pidSettingsX;
  private boolean isFollowingDistance;

  public VisionWithVision(PIDSettings pidSettingsY, PIDSettings pidSettingsX, double ySetpoint, double xSetpoint,
      double deltaTolaranceY, double deltaToleranceX, Target target, double deltaTime) {
    requires(Robot.driveTrain);
    this.deltaToleranceY = deltaTolaranceY;
    this.deltaToleranceX = deltaToleranceX;
    this.pipline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettingsY = pidSettingsY;
    this.isFollowingDistance = true;
    this.ySetpoint = ySetpoint;
    this.xSetpoint = xSetpoint;
  }

  public VisionWithVision(PIDSettings pidSettingsX, double xSetpoint, double deltaTolaranceX, Target target,
      double deltaTime) {
    requires(Robot.driveTrain);
    this.deltaToleranceX = deltaTolaranceX;
    this.pipline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettingsX = pidSettingsX;
    this.isFollowingDistance = false;
    this.xSetpoint = xSetpoint;
  }

  @Override
  protected void initialize() {
    // setting pidY values
    pIDControllerX = new PIDController(pidSettingsX.getKP(), pidSettingsX.getKI(), pidSettingsX.getKD());
    pIDControllerX.setSetpoint(this.xSetpoint);
    pIDControllerX.setInputRange(-27, 27);
    pIDControllerX.setOutputRange(-1, 1);
    pIDControllerX.setAbsoluteTolerance(pidSettingsX.getTolerance(), deltaToleranceX);
    // setting pidX values
    if (isFollowingDistance) {
      pIDControllerY = new PIDController(pidSettingsY.getKP(), pidSettingsY.getKI(), pidSettingsY.getKD());
      pIDControllerY.setSetpoint(RobotConstants.RobotDimensions.LIMELIGHT_DISTANCE_OFFSET + this.ySetpoint);
      pIDControllerY.setOutputRange(-1, 1);
      pIDControllerY.setAbsoluteTolerance(pidSettingsY.getTolerance(), deltaToleranceY);
    }
    // setting limelight settings
    Robot.limelight.setPipeline(pipline);
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    // if it sees a target it will do pid on the x axis else it will not move
    if (Robot.limelight.getTv()) {
      Robot.driveTrain.arcadeDrive(pIDControllerX.calculate(Robot.limelight.getTx()),
          isFollowingDistance ? pIDControllerY.calculate(Robot.limelight.getDistance()) : 0);
      lastTimeOnTarget = Timer.getFPGATimestamp();
    } else {
      Robot.driveTrain.arcadeDrive(0, 0);
    }
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
    Robot.limelight.setCamMode(CamMode.driver);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
