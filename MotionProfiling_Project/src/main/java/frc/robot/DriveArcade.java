package frc.robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveArcade extends Command {
  public DriveArcade() {
    requires(Robot.drivetrain);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    Robot.drivetrain.arcadeDrive(Robot.oi.getJoystickX(), Robot.oi.getJoystickY());
  }

  @Override
  protected boolean isFinished() {
    return false;
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
