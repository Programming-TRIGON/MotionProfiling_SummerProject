
package frc.robot.motionprofile;

import java.io.File;
import java.io.IOException;

import frc.robot.RobotConstants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public enum Path {
    SCALE(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });

    private final Trajectory trajectory;

    private Path(Waypoint[] path) {
        trajectory = Pathfinder.generate(path, new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
        RobotConstants.TIMEFRAME, RobotConstants.MAX_VELOCITY, RobotConstants.MAX_ACCELERATION,
        RobotConstants.MAX_JERK));
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
