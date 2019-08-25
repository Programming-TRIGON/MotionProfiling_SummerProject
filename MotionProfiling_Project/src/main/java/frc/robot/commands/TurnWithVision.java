package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.PidSettings;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.vision.Target;

public class TurnWithVision extends Command {
  private double lastTimeOnTarget;
  private Target target;
  private PIDController pIDController;
  private PidSettings pidSettings;

  public TurnWithVision(Target target, PidSettings pidSettings) {
    requires(Robot.drivetrain);
    this.target = target;
    this.pidSettings = pidSettings;
  }

  public TurnWithVision(Target target) {
    this(target, RobotConstants.PID.VISION_TURN_PID_SETTINGS);
  }

  @Override
  protected void initialize() {
    // setting pid values
    pIDController = new PIDController(pidSettings.getKP(), pidSettings.getKI(), pidSettings.getKD());
    pIDController.setSetpoint(target.getSetpoint());
    pIDController.setOutputRange(-1, 1);

    pIDController.setAbsoluteTolerance(pidSettings.getTolerance(), pidSettings.getDeltaTolerance());
    // setting limelight settings
    Robot.limelight.setPipeline(target.getIndex());
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    System.out.println(Robot.limelight.getTx());
    if(Robot.limelight.getTv()) {
      Robot.drivetrain.arcadeDrive(pIDController.calculate(-Robot.limelight.getTx()), 0);
      lastTimeOnTarget = Timer.getFPGATimestamp(); 
    } else 
      Robot.drivetrain.arcadeDrive(0, 0);
  }

  protected boolean isFinished() {
    //if it does not detect a target for delta time it will return true
    return Timer.getFPGATimestamp() - lastTimeOnTarget >= pidSettings.getWaitTime() || pIDController.atSetpoint();

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
