package frc.robot;

import java.io.File;
import java.util.ArrayList;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * This class generates all the paths for the path follower command to use.
 */
public class PathCreater {

    public final Trajectory.Config config;
    private ArrayList<Waypoint[]> paths;

    /**
     * We configure the pathfinding and create new pathes, then we generate all the
     * pathes we created
     */
    public PathCreater() {
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
                RobotConstants.TIMEFRAME, RobotConstants.MAX_VELOCITY, RobotConstants.MAX_ACCELERATION,
                RobotConstants.MAX_JERK);
        this.paths.add(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });

    }

    /** Writes all the paths to csv for quick generation of paths */
    public void writeToCSV_AllPaths() {
        for (Path path : Path.values()) {
            String s = "/home/lvuser/Paths " + path.name() + ".csv";
            Pathfinder.writeToCSV(new File(s), path.getTrajectory());
        }
    }

    public enum Path{
        TO_SWITCH(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(1, -3, 0), new Waypoint(3, 2, 0) }),
        TO_SCALE(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });

        private final Trajectory trajectory;
        private Path(Waypoint[] path){
            trajectory = Pathfinder.generate(path, Robot.pathCreater.config);
        }

        public Trajectory getTrajectory(){
            return trajectory;
        }
    }
}
