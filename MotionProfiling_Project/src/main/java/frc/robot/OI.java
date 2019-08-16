package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.commands.DriveArcade;

public class OI {
    public Joystick driver = new Joystick(0);
    public Button resetEncodersButton;
    public Button driveTest, MaxSpeedStop;

    public OI() {
        this.resetEncodersButton = new JoystickButton(driver, 1);
        this.resetEncodersButton.whenPressed(new InstantCommand(() -> Robot.driveTrain.resetEncoders()));
        driveTest = new JoystickButton(driver, 2);
        driveTest.whileHeld(new DriveArcade(() -> 0.0, () -> 1.0));
        //CameraServer.getInstance().startAutomaticCapture(0);
        MaxSpeedStop = new JoystickButton(driver, 3);
    }
    
    public double getJoystickX() {
        return this.driver.getX();
    }

    public double getJoystickY() {
        return -this.driver.getY();
    }
}
