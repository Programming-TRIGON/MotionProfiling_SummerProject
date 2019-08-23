package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utils.Logger;

import java.util.function.Supplier;

import javax.lang.model.util.ElementScanner6;

public class CalibrateDistance extends Command {
    private Supplier<Boolean> wantToLog;
    private boolean isPressed = false; 
    private double deltaDistance;
    private double currentDistance = 0;
    private Logger logger;

    public CalibrateDistance(Supplier<Boolean> wantToLog, double deltaDistance) {
        this.wantToLog = wantToLog;
        this.deltaDistance = deltaDistance;
    }

    @Override
    protected void initialize() {
        logger = new Logger("distance calibration.csv", "Distance", "Area Size");
    }


    @Override
    protected void execute() {
        if (wantToLog.get()) {
            if(!isPressed){
            isPressed = true;
            logger.log(currentDistance, Robot.limelight.getTa());
            currentDistance += deltaDistance;
            }
        }
        else
        isPressed = false;
    }

    @Override
    protected boolean isFinished() {
        return currentDistance>200;

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

