package frc.robot;

import java.io.File;
import java.util.function.Supplier;

import com.spikes2212.dashboard.ConstantHandler;
import com.spikes2212.dashboard.DashBoardController;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.CalibrateKa;
import frc.robot.commands.CalibrateKv;
import frc.robot.commands.CalibrateMaxSpeed;
import frc.robot.commands.*;
import frc.robot.utils.Limelight;
import frc.robot.utils.Limelight.LedMode;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import frc.robot.motionprofile.FollowPath;
import frc.robot.motionprofile.Path;
import frc.robot.motionprofile.PathCreater;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static DashBoardController dbc;
  public static Drivetrain drivetrain;
  public static OI oi;
  public static PathCreater pathCreater;
  public static Limelight limelight;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    Robot.limelight = new Limelight();
    Robot.drivetrain = new Drivetrain();

    Robot.dbc = new DashBoardController();

    dbc.addNumber("Left encoder", Robot.drivetrain::getLeftDistance);
    dbc.addNumber("Right encoder", Robot.drivetrain::getRightDistance);
    dbc.addNumber("Both encoders", Robot.drivetrain::getAverageDistance);
    dbc.addNumber("Gyro angle", Robot.drivetrain::getAngle);
    dbc.addNumber("Right velocity", Robot.drivetrain::getRightVelocity);
    dbc.addNumber("Left velocity", Robot.drivetrain::getLeftVelocity);
    dbc.addNumber("Right acceleration", Robot.drivetrain::getRightAcceleration);
    dbc.addNumber("left acceleration", Robot.drivetrain::getLeftAcceleration);
    dbc.addNumber("left ticks", Robot.drivetrain::getLeftTicks);
    dbc.addNumber("right ticks", Robot.drivetrain::getRightTicks);

    SmartDashboard.putData("test path", new FollowPath(Path.SCALE));
    SmartDashboard.putData("test jaci", new FollowPath(Path.TEST_JACI));
    SmartDashboard.putData("test csv reader", new FollowPath(Path.TEST));
    SmartDashboard.putData("Max speed Kv forward", new CalibrateMaxSpeed(false));
    SmartDashboard.putData("Max speed Kv Reversed", new CalibrateMaxSpeed(true));
    SmartDashboard.putData("test ka",
        new CalibrateKa(RobotConstants.Calibration.leftForwardKv, RobotConstants.Calibration.rightForwardKv,
            RobotConstants.Calibration.leftForwardVi, RobotConstants.Calibration.rightForwardVi, false));
    Supplier<Double> voltageSupplier = ConstantHandler.addConstantDouble("voltage start", 0.45);
    dbc.addNumber("distance from target",Robot.limelight::getDistance);
    InstantCommand reset = new InstantCommand(Robot.drivetrain::resetEncoders);
    reset.setRunWhenDisabled(true);
    SmartDashboard.putData("reset", reset);
    SmartDashboard.putData("test kv", new CalibrateKv(false, voltageSupplier));

    Robot.pathCreater = new PathCreater();
    Robot.oi = new OI();
    Command blink,notBlink,findDistance;
    blink = new InstantCommand(()->limelight.setLedMode(LedMode.blink));
    blink.setRunWhenDisabled(true);
    notBlink = new InstantCommand(()->limelight.setLedMode(LedMode.defaultPipeline));
    notBlink.setRunWhenDisabled(true);
    findDistance = new CalibrateDistance(oi.driveTest::get,15);
    findDistance.setRunWhenDisabled(true);
    SmartDashboard.putData("blink",blink);
    SmartDashboard.putData("not blink",notBlink);
    SmartDashboard.putData("find distance", findDistance);
    SmartDashboard.putData("vision",new TurnWithVision(RobotConstants.PID.turn, Limelight.Target.RocketMiddle,0.5));

    Waypoint[] points = new Waypoint[] { new Waypoint(0, 0, 0), // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
        new Waypoint(2, -3, 0) // Waypoint @ x=-2, y=-2, exit angle=0 radians
    };

    Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
        0.02, 3, 2.0, 80.01);
    Trajectory trajectory = Pathfinder.generate(points, config);
    Pathfinder.writeToCSV(new File("/home/lvuser/test_path.csv"), trajectory);

  }

  @Override
  public void robotPeriodic() {
    Robot.dbc.update();
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;
    }
  }

  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }
}