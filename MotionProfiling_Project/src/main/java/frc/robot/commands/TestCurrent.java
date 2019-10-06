
package frc.robot.commands;

import java.sql.Time;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TestCurrent extends Command {
  private WPI_TalonSRX talon;
  private double startTime;
  public TestCurrent(WPI_TalonSRX talon) {
    requires(Robot.drivetrain);
    this.talon = talon;
  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startTime = Timer.getFPGATimestamp();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    talon.set(0.4);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(Timer.getFPGATimestamp()-startTime<2)
      return false;
    double current = talon.getOutputCurrent();
    double power = 0.4;
    double y = 9.375*power - 0.6384;
    return current - y > 3;

  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
