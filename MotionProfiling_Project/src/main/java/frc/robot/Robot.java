package frc.robot;

import java.io.File;

import com.spikes2212.dashboard.DashBoardController;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.CalibrateKv;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static DashBoardController dbc;
  public static DriveTrain driveTrain;
  public static OI oi;

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    this.driveTrain = new DriveTrain();

    Robot.dbc = new DashBoardController();

    dbc.addNumber("Left encoder", this.driveTrain::getLeftDistance);
    dbc.addNumber("Right encoder", this.driveTrain::getRightDistance);
    dbc.addNumber("Both encoders", this.driveTrain::getAverageDistance);
    dbc.addNumber("Gyro angle", this.driveTrain::getAngle);
    dbc.addNumber("Right velocity", this.driveTrain::getRightVelocity);
    dbc.addNumber("Left velocity", this.driveTrain::getLeftVelocity);
    dbc.addNumber("Right acceleration", this.driveTrain::getRightAcceleration);
    dbc.addNumber("left acceleration", this.driveTrain::getLeftAcceleration);
    SmartDashboard.putData("test kv", new CalibrateKv(false));

    Robot.oi = new OI();

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