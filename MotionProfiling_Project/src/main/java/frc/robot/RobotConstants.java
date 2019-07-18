package frc.robot;

/**
 * all the constants on the code are stored here.
 */
public class RobotConstants {

    
    public static final double WHEEL_DIAMETER = 0.1524;
    public static final double WHEEL_BASE_WIDTH = 0.6;
    public static final double TIMEFRAME = 0.02;

    public static final double MAX_VELOCITY = 3;
    public static final double MAX_ACCELERATION = 2;
    public static final double MAX_JERK = 80;

    public static final SettingsForPID MotionProfilingPIDSettings = new SettingsForPID(0, 0, 80, 0);

    public static final double kP_TURN = 0.8;

    public static final double RIGHT_TICKS_DIVIDER = 575.0;
    public static final double LEFT_TICKS_DIVIDER = 771.5;
    public static final int TICKS_PER_REV = 50;


}
