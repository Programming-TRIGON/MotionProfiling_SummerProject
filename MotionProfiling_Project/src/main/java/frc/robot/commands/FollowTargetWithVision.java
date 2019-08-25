package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.PidSettings;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;
import frc.robot.vision.Target;

public class FollowTargetWithVision extends Command {
    private double lastTimeOnTarget;
    private int pipeline;
    private PIDController pIDControllerY, pidController;
    private PidSettings pidSettingsY, pidSettingsX;

    /**
     * @param target       The target to follow.
     * @param pidSettingsY PID settings for the distance
     * @param pidSettingsX PID settings for the rotation
     */
    public FollowTargetWithVision(Target target, PidSettings pidSettingsY, PidSettings pidSettingsX) {
        requires(Robot.drivetrain);
        this.pipeline = target.getIndex();
        this.pidSettingsY = pidSettingsY;
        this.pidSettingsX = pidSettingsX;
    }
    public FollowTargetWithVision(Target target){
      this(target, RobotConstants.PID.FOLLOW_TARGET_Y,RobotConstants.PID.FOLLOW_TARGET_X);
    }


    @Override
    protected void initialize() {
        // setting PID X values
        pidController = new PIDController(pidSettingsX.getKP(), pidSettingsX.getKI(), pidSettingsX.getKD());
        pidController.setSetpoint(0);
        pidController.setInputRange(-27, 27);
        pidController.setOutputRange(-1, 1);
        pidController.setAbsoluteTolerance(pidSettingsX.getTolerance(), pidSettingsX.getDeltaTolerance());
        // setting PID Y values
        pIDControllerY = new PIDController(pidSettingsY.getKP(), pidSettingsY.getKI(), pidSettingsY.getKD());
        pIDControllerY.setSetpoint();
        pIDControllerY.setOutputRange(-1, 1);
        pIDControllerY.setAbsoluteTolerance(pidSettingsY.getTolerance(), pidSettingsY.getDeltaTolerance());
        // setting limelight settings
        Robot.limelight.setPipeline(pipeline);
        Robot.limelight.setCamMode(CamMode.vision);
    }

    @Override
    protected void execute() {
        // if it sees a target it will do PID on the x axis else it will not move
        if (Robot.limelight.getTv()) {
            Robot.drivetrain.arcadeDrive(-pidController.calculate(Robot.limelight.getTx()),
                    pIDControllerY.calculate(Robot.limelight.getDistance()));
            lastTimeOnTarget = Timer.getFPGATimestamp();
        } else {
            //the target hasn't been found.
            Robot.drivetrain.arcadeDrive(0, 0);
        }
    }

    @Override
    protected boolean isFinished() {
        // if it does not detect a target for enough time it will return true
        return Timer.getFPGATimestamp() - lastTimeOnTarget > pidSettingsX.getWaitTime() || (pidController.atSetpoint() && pIDControllerY.atSetpoint());
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
