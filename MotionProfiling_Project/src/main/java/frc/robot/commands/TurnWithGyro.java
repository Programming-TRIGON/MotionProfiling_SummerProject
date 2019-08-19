package frc.robot.commands;

import com.spikes2212.utils.PIDSettings;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.utils.Limelight;
import frc.robot.utils.Limelight.Target;

import java.util.function.Supplier;


public class TurnWithGyro extends Command {
    private static final double TOLERANCE = 1;
    public static final double DELTA_TOLERANCE = 0.0;
    private Supplier<Double> setpointSupplier;
    private Target target;
    private PIDController pidController;

    public TurnWithGyro(Target target, PIDSettings pidSettings) {
        requires(Robot.driveTrain);
        this.target = target;
        pidController = new PIDController(pidSettings.getKP(), pidSettings.getKI(), pidSettings.getKD());
    }


    @Override
    protected void initialize() {
        Robot.limelight.setCamMode(Limelight.CamMode.vision);
        Robot.limelight.setPipeline(target.getValue());

        pidController.setAbsoluteTolerance(TOLERANCE, DELTA_TOLERANCE);

        pidController.setInputRange(-27, 27);
        pidController.setOutputRange(-1, 1);
        pidController.setSetpoint(Robot.driveTrain.getAngle() + Robot.limelight.getTx());
        Robot.limelight.setCamMode(Limelight.CamMode.driver);
    }


    @Override
    protected void execute() {
        double output = pidController.calculate(Robot.driveTrain.getAngle());
        Robot.driveTrain.arcadeDrive(output, 0);
    }


    @Override
    protected boolean isFinished() {
        return pidController.atSetpoint();
    }


    @Override
    protected void end() {
        Robot.driveTrain.tankDrive(0, 0);
    }


    @Override
    protected void interrupted() {
        end();
    }
}
