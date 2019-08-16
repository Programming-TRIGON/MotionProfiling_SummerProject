
package frc.robot.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import edu.wpi.first.wpilibj.Filesystem;

public class Logger {
    private String path;
    private StringBuilder data;
    /**
     * @param name the name of the file. Should end with .csv
     * @param columns the columns name (ex. velocity, acceleration)
     */
    public Logger(String name, String... columns) {
        this.path =  Filesystem.getOperatingDirectory()+"/logs/"+ getTimeStamp() + '-' + name;
        data = new StringBuilder();
        log(columns);
    }

    /**
     * @param values the values to be added to the logger
     */
    public void log(String... values) {
        for (String value : values) {
            data.append(value).append(',');
        }
        data.setCharAt(data.length() - 1, '\n');
    }
    /**
     * @param values the values to be added to the logger
     */
    public void log(double ... values) {
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Double.toString(values[i]);
        }
        log(array);
    }
    /**
     * Saves the file and closes it. Should only be called once.
     */
    public void close(){
        File file = new File(path);
        File parentFile = file.getParentFile();
        
        parentFile.mkdirs();
    
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(data.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }
    
}