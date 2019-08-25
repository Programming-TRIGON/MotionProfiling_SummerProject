package frc.robot.commands;

import java.util.function.Supplier;
import com.spikes2212.dashboard.ConstantHandler;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PidSettings;
import frc.robot.utils.Limelight;


public class TestPID extends Command {
  private Supplier<Double> KP = ConstantHandler.addConstantDouble("KP", 0.01);
  private Supplier<Double> KI = ConstantHandler.addConstantDouble("KI", 0);
  private Supplier<Double> KD = ConstantHandler.addConstantDouble("KD", 0.00);
  private Supplier<Double> TOLERANCE = ConstantHandler.addConstantDouble("tolerance", 1);
  private Supplier<Double> DELTA_TOLERANCE = ConstantHandler.addConstantDouble("delta tolerance", 1);
  Supplier<Double> Setpoint = ConstantHandler.addConstantDouble("Setpoint", 0);

  private PidSettings pidSettings;
  private Command testCommand;

  public TestPID() {
  }

  @Override
  protected void initialize() {
    updatePID();
    testCommand = new TurnWithGyro(Limelight.Target.RocketMiddle,pidSettings);
    testCommand.start();
  }

  @Override
  protected void execute() {
  }

  @Override
  protected boolean isFinished() {
    return testCommand.isCompleted();
  }

  @Override
  protected void end() {
    //Robot.lift.setMotorSpeed(0);
  }

  public void updatePID(){
    this.pidSettings = new PidSettings(KP.get(), KI.get(), KD.get(), TOLERANCE.get(),Setpoint.get(), DELTA_TOLERANCE.get());
    SmartDashboard.putString("PID setting", "" + KP.get() + KI.get() + KD.get() + TOLERANCE.get() + DELTA_TOLERANCE.get());
  }

  @Override
  protected void interrupted() {
    end();
  }
}

