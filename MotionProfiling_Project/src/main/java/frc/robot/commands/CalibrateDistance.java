package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utils.Logger;

import java.util.function.Supplier;

public class CalibrateDistance extends Command {
    private Supplier<Boolean> wantToLog;

    private double deltaDistance;
    private double currentDistance = 20;
    private Logger logger;

    public CalibrateDistance(Supplier<Boolean> wantToLog, double deltaDistance) {
        this.wantToLog = wantToLog;
        this.deltaDistance = deltaDistance;
    }

    @Override
    protected void initialize() {
        logger = new Logger("distance calibration", "Distance", "Area Size");
    }

    @Override
    protected void execute() {
        if (wantToLog.get()) {
            logger.log(currentDistance, Robot.limelight.getTa());
            currentDistance -= deltaDistance;
        }
    }

    @Override
    protected boolean isFinished() {
        return currentDistance < 5;
    }

    @Override
    protected void end() {
        logger.close();
    }

    @Override
    protected void interrupted() {
        end();
    }
}
