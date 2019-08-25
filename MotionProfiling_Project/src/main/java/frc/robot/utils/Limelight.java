package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.Vector2d;
import frc.robot.RobotConstants;
import frc.robot.vision.Target;

public class Limelight {

    private final NetworkTableEntry tv, tx, ty, ta, ts, ledMode, camMode, pipeline;

    /**
     * @param tableKey the key of the limelight - if it was overwritten.
     */
    public Limelight(String tableKey) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable limelightTable = inst.getTable(tableKey);
        tv = limelightTable.getEntry("tv");
        tx = limelightTable.getEntry("tx");
        ty = limelightTable.getEntry("ty");
        ta = limelightTable.getEntry("ta");
        ts = limelightTable.getEntry("ts");
        ledMode = limelightTable.getEntry("ledMode");
        camMode = limelightTable.getEntry("camMode");
        pipeline = limelightTable.getEntry("pipeline");

    }

    public Limelight() {
        this("limelight");
    }

    /**
     * @return Whether the limelight has any valid targets (0 or 1)
     */
    public boolean getTv() {
        return tv.getDouble(0) == 1;
    }

    /**
     * @return Horizontal Offset From Crosshair To Target
     */
    public double getTx() {
        return tx.getDouble(0);
    }

    /**
     * @return Vertical Offset From Crosshair To Target
     */
    public double getTy() {
        return ty.getDouble(0);
    }

    /**
     * @return Target Area (0% of image to 100% of image)
     */
    public double getTa() {
        return ta.getDouble(0);
    }

    /**
     * @return Skew or rotation (-90 degrees to 0 degrees)
     */
    public double getTs() {
        return ts.getDouble(0);
    }

    /**
     * @return The distance between the the target and the limelight
     */
//    public double getDistance() {
//        return (getTarget().height - RobotConstants.RobotDimensions.LIMELIGHT_HEIGHT) /
//                Math.tan(Math.toRadians(RobotConstants.RobotDimensions.LIMELIGHT_ANGLE + getTy()));
//    }
    public double getDistance(){
        double x = getTa();
        return -36.88*Math.log(x)+124.53/*+ RobotConstants.RobotDimensions.LIMELIGHT_DISTANCE_OFFSET-10.6*/;
    }

    /**
     * @return The distance between the the target and the the middle of the robot
     */
//    public double getDistance() {
//        return calculateVector().magnitude();
//    }

    public double getAngle() {
        return Math.toDegrees(Math.atan(calculateVector().y / calculateVector().x));
    }

    /**
     * @return the cam mode in the NetworkTable.
     */
    public CamMode getCamMode() {
        return camMode.getDouble(0) == 0 ? CamMode.vision : CamMode.driver;
    }

    /**
     * @param camMode the mode to be changed to.
     */
    public void setCamMode(int camMode) {
        this.camMode.setNumber(camMode);
    }

    /**
     * @param camMode the mode to be changed to.
     */
    public void setCamMode(CamMode camMode) {
        this.camMode.setNumber(camMode.getValue());
    }

    /**
     * This enum has two states - send images to driver and calculate the vision.
     */
    public enum CamMode {
        vision(0), driver(1);

        private int value;

        CamMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum LedMode {
        defaultPipeline(0), off(1), blink(2), on(3);

        private int value;

        LedMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * @return the led mode in the NetworkTable.
     */
    public LedMode getLedMode() {
        int index = (int) ledMode.getDouble(0);
        return LedMode.values()[index];
    }

    /**
     * @param ledMode the mode to be changed to.
     */
    public void setLedMode(LedMode ledMode) {
        this.ledMode.setNumber(ledMode.getValue());
    }

    /**
     * @param ledMode the mode to be changed to.
     */
    public void setLedMode(int ledMode) {
        this.ledMode.setNumber(ledMode);
    }



    /**
     * @return the current target in the NetworkTable.
     */
    public Target getTarget() {
        int index = (int) pipeline.getDouble(0);
        return Target.values()[index];
    }

    /**
     * @param pipeline pipeline index to be changed to.
     */
    public void setPipeline(int pipeline) {
        this.pipeline.setNumber(pipeline);
    }

    /**
     * @param target the target to be changed to.
     */
    public void setPipeline(Target target) {
        setPipeline(target.getIndex());
    }

    private Vector2d calculateVector() {
        Vector2d MiddleToLimelight = new Vector2d(0, RobotConstants.RobotDimensions.DISTANCE_FROM_MIDDLE_TO_LIMELIGHT);
        Vector2d limelightToTarget = new Vector2d(0, getDistance());
        limelightToTarget.rotate(-getTx());
        return new Vector2d(MiddleToLimelight.x + limelightToTarget.x, MiddleToLimelight.y + limelightToTarget.y);
    }
}

