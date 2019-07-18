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
    private double WHEEL_BASE_WIDTH = 0.6;
    private double TIMEFRAME = 0.02;
    private double MAX_SPEED = 3;
    private double MAX_ACC = 2;
    private double MAX_JERK = 80;

    public PathCreater(){
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
        TIMEFRAME, MAX_SPEED, MAX_ACC, MAX_JERK);
        this.paths.add(new Waypoint[]{
            new Waypoint(0, 0, 0),
            new Waypoint(2, -3, 0)
        });
        
        generate_AllPaths();
    }

    public void generate_AllPaths(){
        for(int i=0; i<this.paths.size(); i++){
            this.trajectories[i] = Pathfinder.generate(this.paths.get(i), this.config);
            
        }
    }

    public Trajectory[] getTrajectories(int pathNumber){
        this.modifier = new TankModifier(trajectories[pathNumber]);
        this.modifier.modify(this.WHEEL_BASE_WIDTH);
        Trajectory[] trajectories = {this.modifier.getRightTrajectory(), this.modifier.getLeftTrajectory()};
        return trajectories;
    }



    public void writeToCSV_AllPaths(){
        for(int i=0; i<this.paths.size(); i++){
            String s = "/home/lvuser/Path " + i+1 + ".csv";
            Pathfinder.writeToCSV(new File(s), this.trajectories[i]);
        }
    }
}
