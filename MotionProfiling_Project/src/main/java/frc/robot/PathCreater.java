package frc.robot;

import java.io.File;
import java.util.ArrayList;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

/**
 * This class generates all the paths for the path follower command to use.
 */
public class PathCreater {

    public final Trajectory.Config config;
    private ArrayList<Waypoint[]> paths;
    private Trajectory[] trajectories;
    private TankModifier modifier;

    /**
     * We configure the pathfinding and create new pathes, then we generate all the
     * pathes we created
     */
    public PathCreater() {
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
                RobotConstants.TIMEFRAME, RobotConstants.MAX_VELOCITY, RobotConstants.MAX_ACCELERATION,
                RobotConstants.MAX_JERK);
        this.paths.add(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });


        generate_AllPaths();
    }

    /** Takes all the paths we created and generates them */
    public void generate_AllPaths() {
        for (int i = 0; i < this.paths.size(); i++) {
            this.trajectories[i] = Pathfinder.generate(this.paths.get(i), this.config);
        }
    }

    /**
     * We split the tank drive for bOth sides of the robot and get the trajectory of
     * each one. The index for the right trajectory is 0 and for the left its 1.
     */
    public Trajectory[] getSplitTrajectories(Path path) {
        this.modifier = new TankModifier(path.getTrajectory());
        this.modifier.modify(RobotConstants.WHEEL_BASE_WIDTH);
        Trajectory[] trajectories = { this.modifier.getRightTrajectory(), this.modifier.getLeftTrajectory() };
        return trajectories;
    }

    /** Writes all the paths to csv for quick generation of paths */
    public void writeToCSV_AllPaths() {
        for (int i = 0; i < this.paths.size(); i++) {
            String s = "/home/lvuser/Paths " + i + 1 + ".csv";
            Pathfinder.writeToCSV(new File(s), this.trajectories[i]);
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
