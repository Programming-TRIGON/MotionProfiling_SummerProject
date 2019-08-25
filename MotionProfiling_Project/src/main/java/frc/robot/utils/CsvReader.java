
package frc.robot.utils;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;

import java.io.*;

public class CsvReader {
    /**
     * Takes the csv file and translates it to a trajectory for the motionProfiling!
     *
     * @param path to the file in absulute path.
     * @return a trajectory
     */
    public static Trajectory read(String path) {
        Segment[] segments = null;
        File file = new File(path);
        try (FileReader r = new FileReader(file); BufferedReader br = new BufferedReader(r)) {
            segments = new Segment[countLines(path)];
            for (int i = 0; i < segments.length; i++) {
                String line = br.readLine();
                String[] numbers = line.split(",");

                double dt = Double.parseDouble(numbers[0]);
                double x = Double.parseDouble(numbers[1]);
                double y = Double.parseDouble(numbers[2]);
                double p = Double.parseDouble(numbers[3]);
                double v = Double.parseDouble(numbers[4]);
                double a = Double.parseDouble(numbers[5]);
                double h = Math.toRadians(Double.parseDouble(numbers[6]));
                segments[i] = new Segment(dt, x, -y, p, v, a, 0, -h);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Trajectory(segments);
    }

    // Taken from stackOverflow. This function counts the amount of lines in a text file.
    private static int countLines(String filename) throws IOException {

        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024; ) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        }
    }
}
