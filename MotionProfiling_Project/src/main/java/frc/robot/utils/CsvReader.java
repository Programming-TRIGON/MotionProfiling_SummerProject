
package frc.robot.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import jaci.pathfinder.Trajectory;

/**
 * Takes the csv file and translates it to a trajectory for the motionProfiling!
 */
public class CsvReader {

    public static Trajectory read(String path){
        File file = new File(path);
        FileReader r = new FileReader(file);
        BufferedReader br = new BufferedReader(r);
        try {
             String line;
            while((line = br.readLine()) != null)
                {
                    String[] numbers = line.split(",");
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            finally{
                br.close();
                r.close();
            }
        }

    return yes
}
}
