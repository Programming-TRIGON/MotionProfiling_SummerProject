package frc.robot;

import com.spikes2212.dashboard.DashBoardController;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public static DashBoardController dbc;
  public static DriveTrain driveTrain;


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
  }

  @Override
  public void testPeriodic() {
  }
}
