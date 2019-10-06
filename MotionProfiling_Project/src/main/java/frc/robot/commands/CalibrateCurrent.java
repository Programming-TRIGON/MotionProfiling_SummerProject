
package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.utils.Logger;

public class CalibrateCurrent extends Command {
    private WPI_TalonSRX talon;
    private Logger logger;
    private double startTime = 0, maxCurrent, currentPower = 0.2;
    private static final double DELTA_TIME = 1, MAX_POWER = 0.5, DELTA_POWER = 0.05;

  /**
   * @param subsystem the subsystem the talonSRX belongs to.
   * @param talonSRX the talonSRX which the command will calibrate.
   */
    public CalibrateCurrent(Subsystem subsystem, WPI_TalonSRX talonSRX) {
        requires(subsystem);
        this.talon = talonSRX;
    }

    // Initializes the logger and starts to measure time.
    @Override
    protected void initialize() {
        logger = new Logger("current calibration.csv", "power", "amps");
        startTime = Timer.getFPGATimestamp();
        currentPower = 0.2;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        talon.set(currentPower);
        double changeTime = Timer.getFPGATimestamp() - startTime;
        if (changeTime > DELTA_TIME) {
            //starts measuring the current.
            maxCurrent = Math.max(maxCurrent, talon.getOutputCurrent());
            if (changeTime > 2 * DELTA_TIME) {
                /*
                 * More than two seconds have passed. Logs the current power and the sum of the
                 * current.
                 */
                logger.log(currentPower, maxCurrent);
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
        return currentPower >= MAX_POWER;
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
