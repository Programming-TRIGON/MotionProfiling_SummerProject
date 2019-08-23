package frc.robot.commands;

import com.spikes2212.utils.PIDSettings;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;

public class VisionPID extends Command {
  private double lastTimeOnTarget, deltaTime;
  private int pipline;
  private PIDController pIDController;
  private double deltaTolerance;
  private PIDSettings pidSettings;

  public VisionPID(PIDSettings pidSettings, double deltaTolarance, Target target, double deltaTime) {
    requires(Robot.driveTrain);
    this.deltaTolerance = deltaTolarance;
    this.pipline = target.getIndex();
    this.deltaTime = deltaTime;
    this.pidSettings = pidSettings;
  }

  @Override
  protected void initialize() {
    // setting pid values
    pIDController = new PIDController(pidSettings.getKP(), pidSettings.getKI(), pidSettings.getKD());
    pIDController.setSetpoint(0);
    pIDController.setInputRange(-27, 27);
    pIDController.setOutputRange(-1, 1);
    pIDController.setAbsoluteTolerance(pidSettings.getTolerance(), deltaTolerance);
    // setting limelight settings
    Robot.limelight.setPipeline(pipline);
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    // if it sees a target it will do pid on the x axis else it will not move
    if (Robot.limelight.getTv()) {
      Robot.driveTrain.arcadeDrive(pIDController.calculate(Robot.limelight.getTx()), 0);
      lastTimeOnTarget = Timer.getFPGATimestamp();
    } else {
      Robot.driveTrain.arcadeDrive(0, 0);
    }
  }

  @Override
  protected boolean isFinished() {
    // if it does not detect a target for delta time it will return true
    return pIDController.atSetpoint() || Timer.getFPGATimestamp() - lastTimeOnTarget > deltaTime;
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
