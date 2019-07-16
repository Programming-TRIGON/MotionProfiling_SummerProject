package frc.robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveArcade extends Command {
  public DriveArcade() {
    requires(Robot.driveTrain);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    Robot.driveTrain.arcadeDrive(Robot.oi.driver.getX(), 0);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
    Robot.driveTrain.arcadeDrive(0, 0);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
