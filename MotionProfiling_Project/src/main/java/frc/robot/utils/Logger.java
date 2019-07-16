
package frc.robot.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

public class Logger {
    private String path;
    private StringBuilder data;
    /**
     * @param name the name of the file. Should end with .csv
     * @param columns the columns name (ex. velocity, acceleration)
     */
    public Logger(String name, String... columns) {
        this.path = "\\lvuser\\logs\\" + getTimeStamp() + '-' + name;
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
        file.getParentFile().mkdirs();

        try {
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            writer.println(data.toString());
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