package frc.robot;

import java.io.File;
import java.io.IOException;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

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

    public static final PidSettings MOTION_PROFILING_PID_SETTINGS_LEFT = new PidSettings(0.3, 0, 0, 0);
    public static final PidSettings MOTION_PROFILING_PID_SETTINGS_RIGHT = new PidSettings(0.3, 0, 0, 0);

    public static final double MOTION_PROFILING_KP_TURN = 0.8;

    public static final double TICKS_PER_METER_RIGHT = 575.0;
    public static final double TICKS_PER_METER_LEFT = 771.5;
    public static final int TICKS_PER_REVOLUTION_RIGHT = 50;
    public static final int TICKS_PER_REVOLUTION_LEFT = 3;

    public enum Path {
        SCALE(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });

        private final Trajectory trajectory;

        private Path(Waypoint[] path) {
            trajectory = Pathfinder.generate(path, Robot.pathCreater.config);
        }

        private Path(File csvFile) {
            Trajectory trajectory = null;
            try {
                trajectory = Pathfinder.readFromCSV(csvFile);
            } catch (IOException e) {
                System.err.println("File not existing");
            }
            this.trajectory = trajectory;

        }

        public Trajectory getTrajectory() {
            return trajectory;
        }
    }
}
