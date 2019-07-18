package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class OI {    
    public Joystick driver = new Joystick(0);
    public Button btn1;

    public OI() {
        this.btn1 = new JoystickButton(driver, 1);
        this.btn1.whenPressed(new InstantCommand(() -> Robot.driveTrain.resetEncoders()));
        CameraServer.getInstance().startAutomaticCapture(0);
    }
}
