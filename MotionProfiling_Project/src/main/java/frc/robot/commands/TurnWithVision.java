package frc.robot.commands;

import com.spikes2212.dashboard.ConstantHandler;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.PidSettings;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;

public class TurnWithVision extends Command {
  private double lastTimeOnTarget, deltaTime;
  private int pipeline;
  private PIDController pIDControllerY, pIDControllerX;
  private PidSettings pidSettingsY, pidSettingsX;
  private boolean isFollowingDistance;

  public TurnWithVision(PidSettings pidSettingsY, PidSettings pidSettingsX, Target target, double deltaTime) {
    requires(Robot.drivetrain);
    this.pipeline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettingsY = pidSettingsY;
    this.pidSettingsX = pidSettingsX;
    this.isFollowingDistance = true;


  }

  public TurnWithVision(PidSettings pidSettingsX, Target target, double deltaTime) {
    requires(Robot.drivetrain);
    this.pipeline = target.getIndex();
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
    pIDControllerX.setAbsoluteTolerance(pidSettingsX.getTolerance());
    // setting pidX values
    if (isFollowingDistance) {
      pIDControllerY = new PIDController(pidSettingsY.getKP(), pidSettingsY.getKI(), pidSettingsY.getKD());
      pIDControllerY.setSetpoint(RobotConstants.RobotDimensions.LIMELIGHT_DISTANCE_OFFSET + pidSettingsY.getSetpoint());
      pIDControllerY.setOutputRange(-1, 1);
      pIDControllerY.setAbsoluteTolerance(pidSettingsY.getTolerance(), pidSettingsY.getDeltaTolerance());
    }
    // setting limelight settings
    Robot.limelight.setPipeline(pipeline);
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    // if it sees a target it will do pid on the x axis else it will not move
//    if (Robot.limelight.getTv()) {
      Robot.drivetrain.arcadeDrive(-pIDControllerX.calculate(Robot.limelight.getTx()),
          isFollowingDistance ? pIDControllerY.calculate(Robot.limelight.getDistance()) : 0);
//      lastTimeOnTarget = Timer.getFPGATimestamp();
//    } else {
//      Robot.drivetrain.arcadeDrive(0, 0);
    //}
  }

  @Override
  protected boolean isFinished() {
    // if it does not detect a target for delta time it will return true
//    return Timer.getFPGATimestamp() - lastTimeOnTarget > deltaTime || (isFollowingDistance
//        ? pIDControllerX.atSetpoint() /*&& pIDControllerY.atSetpoint())*/
//        : pIDControllerX.atSetpoint());
    return pIDControllerX.atSetpoint();
  }

  @Override
  protected void end() {
    Robot.drivetrain.arcadeDrive(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
