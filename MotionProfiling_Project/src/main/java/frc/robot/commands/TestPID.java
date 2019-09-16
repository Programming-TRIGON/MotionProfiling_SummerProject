package frc.robot.commands;

import java.util.function.Supplier;
import com.spikes2212.dashboard.ConstantHandler;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PidSettings;
import frc.robot.vision.Target;

public class TestPID extends Command {
  private Supplier<Double> KP = ConstantHandler.addConstantDouble("KP", 0.01);
  private Supplier<Double> KI = ConstantHandler.addConstantDouble("KI", 0);
  private Supplier<Double> KD = ConstantHandler.addConstantDouble("KD", 0);
  private Supplier<Double> TOLERANCE = ConstantHandler.addConstantDouble("tolerance", 1);
  private Supplier<Double> DELTA_TOLERANCE = ConstantHandler.addConstantDouble("delta tolerance", 1);
  private Supplier<Double> KP2 = ConstantHandler.addConstantDouble("KP2", 0.01);
  private Supplier<Double> KI2 = ConstantHandler.addConstantDouble("KI2", 0);
  private Supplier<Double> KD2 = ConstantHandler.addConstantDouble("KD2", 0);
  private Supplier<Double> TOLERANCE2 = ConstantHandler.addConstantDouble("tolerance2", 1);
  private Supplier<Double> DELTA_TOLERANCE2 = ConstantHandler.addConstantDouble("delta tolerance2", 1);

  private PidSettings pidSettings;
  private PidSettings pidSettings2;
  private Command testCommand;

  public TestPID() {
  }

  @Override
  protected void initialize() {
    updatePID();
    testCommand = new FollowTarget(Target.RocketMiddle, pidSettings, pidSettings2);
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
    testCommand.cancel();
    testCommand.close();
  }

  public void updatePID(){
    this.pidSettings = new PidSettings(KP.get(), KI.get(), KD.get(), TOLERANCE.get(), DELTA_TOLERANCE.get());
    this.pidSettings2 = new PidSettings(KP2.get(), KI2.get(), KD2.get(), TOLERANCE2.get(), DELTA_TOLERANCE2.get(),2);
    SmartDashboard.putString("PID setting", "" + KP.get() + KI.get() + KD.get() + TOLERANCE.get() + DELTA_TOLERANCE.get());
  }

  @Override
  protected void interrupted() {
    end();

  }
}

