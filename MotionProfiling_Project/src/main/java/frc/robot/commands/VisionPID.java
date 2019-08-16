package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.PIDController;
import frc.robot.Robot;
import frc.robot.utils.Limelight.CamMode;
import frc.robot.utils.Limelight.Target;

public class VisionPID extends Command {
  double kp, ki, kd, tolrance, timer, DELTATIME;
  int pipline;
  PIDController pIDController;
  public VisionPID(double kp, double ki, double kd, double tolrance, Target target) {
    requires(Robot.driveTrain);
    this.kp = kp;
    this.ki = ki;
    this.kd = kd;
    this.tolrance = tolrance;
    this.pipline = target.getValue();
    DELTATIME = 25;
  }

  @Override
  protected void initialize() {
    //setting pid values
    pIDController = new PIDController(this.kp, this.ki, this.kd);
    pIDController.setSetpoint(0);
    pIDController.setInputRange(-27, 27);
    pIDController.setOutputRange(-1,1);
    pIDController.setAbsoluteTolerance(tolrance);
    // setting limelight settings
    Robot.limelight.setPipeline(pipline);
    Robot.limelight.setCamMode(CamMode.vision);
  }

  @Override
  protected void execute() {
    //if it sees a target it will do pid on the x axis else it will not move
    if(Robot.limelight.getTv()){
    Robot.driveTrain.arcadeDrive(pIDController.calculate(Robot.limelight.getTx()), 0);
    timer = Timer.getFPGATimestamp(); 
    }
    else{
      Robot.driveTrain.arcadeDrive(0, 0);
    }
  }

  @Override
  protected boolean isFinished() {
    //if it does not detect a target for delta time it will return true
    return pIDController.atSetpoint() || Timer.getFPGATimestamp() - timer > DELTATIME;
  }

  @Override
  protected void end() {
    Robot.driveTrain.arcadeDrive(0, 0);
    Robot.limelight.setCamMode(CamMode.driver);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
