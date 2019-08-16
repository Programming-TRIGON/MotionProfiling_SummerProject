package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.command.Command;

public class DriveArcade extends Command {
  private Supplier<Double> x;
private Supplier<Double> y; 
  public DriveArcade(Supplier<Double> x,Supplier<Double> y) {
    requires(Robot.driveTrain);
    this.x = x;
    this.y = y;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    Robot.driveTrain.arcadeDrive(x.get(), y.get());
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
