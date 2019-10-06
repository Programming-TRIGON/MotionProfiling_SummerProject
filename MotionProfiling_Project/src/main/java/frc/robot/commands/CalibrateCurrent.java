
package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.utils.Logger;

public class CalibrateCurrent extends Command {
  WPI_TalonSRX talon;
  Logger logger;
  double startTime = 0, maxCurrent, runNumber, currentPower = 0.2;
  final double DELTA_TIME = 1000, MAX_POWER = 0.5, DELTA_POWER = 0.05;

  public CalibrateCurrent(Subsystem subsystem, WPI_TalonSRX talonSRX) {
    requires(subsystem);
    this.talon = talonSRX;
  }

  // Initializes the logger and starts to measure time.
  @Override
  protected void initialize() {
    logger = new Logger("current calibration.csv", "power", "ampers");
    startTime = Timer.getFPGATimestamp();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    talon.set(currentPower);
    if (Timer.getFPGATimestamp() - startTime > DELTA_TIME) {
      //starts measuring the current.
      maxCurrent = Math.max(maxCurrent, talon.getOutputCurrent());
      if(Timer.getFPGATimestamp() - startTime > 2*DELTA_TIME){
      /*
       * More than two seconds have passed. Logs the current power and the sum of the
       * current.
       */
        logger.log(currentPower, maxCurrent/runNumber);
        // increases the power and resets other variables.
        currentPower += DELTA_POWER;
        maxCurrent = 0;
        startTime = Timer.getFPGATimestamp();
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return currentPower > MAX_POWER;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    talon.set(0);
    logger.close();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    logger.log("interrupted");
    end();
  }
}
