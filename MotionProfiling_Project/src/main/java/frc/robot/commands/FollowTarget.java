package frc.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PidSettings;
import frc.robot.Robot;
import frc.robot.RobotConstants;
import frc.robot.VisionPIDSource;
import frc.robot.utils.Limelight;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.vision.Target;

public class FollowTarget extends Command {
    private double lastTimeOnTarget;
    private Target target;
    private PIDController pidControllerY, pidControllerX;
    private PidSettings pidSettingsY, pidSettingsX;
    private PIDOutput pidOutput;

    /**
     * @param target       The target to follow.
     * @param pidSettingsY PID settings for the distance
     * @param pidSettingsX PID settings for the rotation
     */
    public FollowTarget(Target target, PidSettings pidSettingsY, PidSettings pidSettingsX) {
        requires(Robot.drivetrain);
        this.target = target;
        this.pidSettingsY = pidSettingsY;
        this.pidSettingsX = pidSettingsX;
    }

    /**
     * @param target The target to follow.
     */
    public FollowTarget(Target target) {
        this(target, RobotConstants.PID.FOLLOW_TARGET_Y, RobotConstants.PID.FOLLOW_TARGET_X);
    }

    @Override
    protected void initialize() {
        this.pidOutput = new PIDOutput() {

            public void pidWrite(double x) {
                Robot.drivetrain.arcadeDrive(x, 0);
            }
        };
        // setting PID X values
        this.pidControllerX = new PIDController(pidSettingsX.getKP(), pidSettingsX.getKI(), pidSettingsX.getKD(),
                Robot.visionPIDSource, this.pidOutput);
        pidControllerX.setSetpoint(0);
        // pidControllerX.setInputRange(-27, 27);
        pidControllerX.setOutputRange(1, -1);
        pidControllerX.setAbsoluteTolerance(pidSettingsX.getTolerance());
        // setting PID Y values
        pidControllerY = new PIDController(pidSettingsY.getKP(), pidSettingsY.getKI(), pidSettingsY.getKD());
        // pidControllerY.setSetpoint(target.getSetpoint());
        pidControllerY.setSetpoint(0);
        pidControllerY.setOutputRange(1, -1);
        pidControllerY.setAbsoluteTolerance(pidSettingsY.getTolerance(), pidSettingsY.getDeltaTolerance());
        // setting limelight settings
        Robot.limelight.setPipeline(target.getIndex());
        Robot.limelight.setCamMode(CamMode.vision);
    }

    @Override
    protected void execute() {
        // if it sees a target it will do PID on the x axis else it won't move
        if (Robot.limelight.getTv()) {
            // Robot.drivetrain.arcadeDrive(pidControllerX.calculate(Robot.limelight.getTx()),
            // pidControllerY.calculate(Robot.limelight.getDistance()));
            Robot.drivetrain.arcadeDrive(Robot.limelight.getTx() * pidSettingsX.getKP(),
                    Robot.limelight.getDistance() * pidSettingsY.getKP());
            lastTimeOnTarget = Timer.getFPGATimestamp();
        } else {
            // the target hasn't been found.
            Robot.drivetrain.arcadeDrive(0, 0);
        }
    }

    @Override
    protected boolean isFinished() {
        // if it does not detect a target for enough time it will return true
        return Timer.getFPGATimestamp() - lastTimeOnTarget > pidSettingsX.getWaitTime()
                || (pidControllerX.atSetpoint() && pidControllerY.atSetpoint());
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
