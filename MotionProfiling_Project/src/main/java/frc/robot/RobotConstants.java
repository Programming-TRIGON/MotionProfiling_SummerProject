package frc.robot;

/**
 * All the constants on the code are stored here.
 */
public class RobotConstants {

    public static final double WHEEL_DIAMETER = 0.1524;
    public static final double WHEEL_BASE_WIDTH = 0.6;
    public static final double TIMEFRAME = 0.02;

    public static final double MAX_VELOCITY = 3;
    public static final double MAX_ACCELERATION = 2;
    public static final double MAX_JERK = 80;

    public static final PidSettings MOTION_PROFILING_PID_SETTINGS = new PidSettings(0, 0, 80, 0);

    public static final double MOTION_PROFILING_KP_TURN = 0.8;

    public static final double TICKS_PER_METER_RIGHT = 575.0;
    public static final double TICKS_PER_METER_LEFT = 771.5;
    public static final int TICKS_PER_REVOLUTION = 50;

}
