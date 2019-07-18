package frc.robot;

import java.io.File;
import java.util.ArrayList;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

/**
 * this class generates all the paths for the path follower command to use.
 */
public class PathCreater {     

    private Trajectory.Config config;
    private ArrayList<Waypoint[]> paths;
    public static Trajectory[] trajectories;

    public PathCreater(){
        this.config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
        0.02, 3, 2.0, 80);
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

    public void writeToCSV_AllPaths(){
        for(int i=0; i<this.paths.size(); i++){
            String s = "/home/lvuser/Path " + i+1 + ".csv";
            Pathfinder.writeToCSV(new File(s), this.trajectories[i]);
        }
    }
}
