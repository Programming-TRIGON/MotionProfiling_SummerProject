
package frc.robot;


public class RobotConstants {



    public static class Calibration{
        public static final double leftForwardKv = 0.2103;
        public static final double leftForwardVi = 0.3717;
        public static final double rightForwardKv = 0.1929;
        public static final double rightForwardVi = 0.3913;


    }
    public static class RobotDimensions{
        //TODO: set real values
        public static final double DISTANCE_FROM_MIDDLE_TO_LIMELIGHT = 0;
        public static final double LIMELIGHT_HEIGHT = 86.8;
        public static final double LIMELIGHT_ANGLE = -1.588;
        public static final double LIMELIGHT_DISTANCE_OFFSET = 23.673024888929177;
    }
    public static class PID{
        public static final PidSettings turn = new PidSettings(0.001,0,0.,1,0,10);
    }

}
