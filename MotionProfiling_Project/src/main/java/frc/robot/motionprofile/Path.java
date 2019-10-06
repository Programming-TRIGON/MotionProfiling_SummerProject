
package frc.robot.motionprofile;

import frc.robot.RobotConstants;
import frc.robot.utils.CsvReader;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.io.IOException;

public enum Path {
    SCALE(new Waypoint[]{new Waypoint(0, 0, 0), new Waypoint(2, -3, 0)}),
    //TEST("path.csv"),
    TEST_JACI(new Waypoint[]{new Waypoint(0, 0, 0), new Waypoint(1, -1, -90)});
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

    /**
     * loads path from a PathPlanner formatted csv file.
     *
     * @param pathPlannerFile name of the file in the paths folder.
     */
    Path(String pathPlannerFile) {
        trajectory = CsvReader.read("/home/lvuser/paths/" + pathPlannerFile);
    }

    public Trajectory getTrajectory() {
        return trajectory;
    }
}
