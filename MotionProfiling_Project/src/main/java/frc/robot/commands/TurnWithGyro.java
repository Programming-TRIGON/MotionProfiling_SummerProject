package frc.robot.commands;

import com.spikes2212.utils.PIDSettings;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.utils.Limelight;
import frc.robot.utils.Limelight.Target;


public class TurnWithGyro extends Command {
    private double DELTA_TOLERANCE;
    private Target target;
    private PIDController pidController;
    private PIDSettings pidSettings;

    public TurnWithGyro(Target target, PIDSettings pidSettings,double deltaTolerance) {
        requires(Robot.drivetrain);
        this.target = target;
        this.pidSettings = pidSettings;
    }


    @Override
    protected void initialize() {
        Robot.limelight.setCamMode(Limelight.CamMode.vision);
        Robot.limelight.setPipeline(target.getIndex());
        pidController = new PIDController(pidSettings.getKP(), pidSettings.getKI(), pidSettings.getKD());
        pidController.setAbsoluteTolerance(pidSettings.getTolerance(), DELTA_TOLERANCE);
        pidController.setInputRange(-27, 27);
        pidController.setOutputRange(-1, 1);
        pidController.setSetpoint(Robot.drivetrain.getAngle() + Robot.limelight.getTx());
        Robot.limelight.setCamMode(Limelight.CamMode.driver);
    }


    @Override
    protected void execute() {
        double output = pidController.calculate(Robot.drivetrain.getAngle());
        Robot.drivetrain.arcadeDrive(output, 0);
    }


    @Override
    protected boolean isFinished() {
        return pidController.atSetpoint();
    }


    @Override
    protected void end() {
        Robot.drivetrain.tankDrive(0, 0);
    }


    @Override
    protected void interrupted() {
        end();
    }
}
