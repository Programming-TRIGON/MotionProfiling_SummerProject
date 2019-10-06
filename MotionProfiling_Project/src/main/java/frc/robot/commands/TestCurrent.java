
package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TestCurrent extends Command {
    private static final double WIND_UP_TIME = 2, POWER = 0.4;
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
        double lastPower = talon.get();
        if (isInStall())
            talon.set(0);
        else
            talon.set(POWER);
        if (lastPower != talon.get())
            startTime = Timer.getFPGATimestamp();

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        talon.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }

    private boolean isInStall() {
        if (Timer.getFPGATimestamp() - startTime < WIND_UP_TIME)
            return false;
        double current = talon.getOutputCurrent();
        double power = 0.4;
        //this is extracted from the calibration.
        double y = 9.375 * power - 0.6384;
        return current - y > 3;
    }
}
