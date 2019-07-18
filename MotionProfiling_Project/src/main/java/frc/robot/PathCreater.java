package frc.robot;

import java.io.File;
import java.util.ArrayList;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

/**
 * this class generates all the paths for the path follower command to use.
 */
public class PathCreater {

    private Trajectory.Config config;
    private ArrayList<Waypoint[]> paths;
    private Trajectory[] trajectories;
    private TankModifier modifier;

    /**
     * we configure the pathfinding and create new pathes, then we generate all the
     * pathes we created
     */
    public PathCreater() {
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH,
                RobotConstants.TIMEFRAME, RobotConstants.MAX_VELOCITY, RobotConstants.MAX_ACCELERATION,
                RobotConstants.MAX_JERK);
        this.paths.add(new Waypoint[] { new Waypoint(0, 0, 0), new Waypoint(2, -3, 0) });

        generate_AllPaths();
    }

    /** takes all the paths we created and generates them */
    public void generate_AllPaths() {
        for (int i = 0; i < this.paths.size(); i++) {
            this.trajectories[i] = Pathfinder.generate(this.paths.get(i), this.config);

        }
    }

    /**
     * we split the tank drive for bpth sides of the robot and get the trajectory of
     * each one, first we get the right then the left trajectory
     */
    public Trajectory[] getTrajectories(int pathNumber) {
        this.modifier = new TankModifier(trajectories[pathNumber]);
        this.modifier.modify(RobotConstants.WHEEL_BASE_WIDTH);
        Trajectory[] trajectories = { this.modifier.getRightTrajectory(), this.modifier.getLeftTrajectory() };
        return trajectories;
    }

    /** writes all the paths to csv for quick generation of paths */
    public void writeToCSV_AllPaths() {
        for (int i = 0; i < this.paths.size(); i++) {
            String s = "/home/lvuser/Path " + i + 1 + ".csv";
            Pathfinder.writeToCSV(new File(s), this.trajectories[i]);
        }
    }
}
