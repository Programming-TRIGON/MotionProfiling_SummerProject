package frc.robot;

/**
 * Here all the ports for the robot
 */
public class RobotMap {
    static class CAN {
        public static final int REAR_LEFT_MOTOR = 1;
        public static final int FRONT_LEFT_MOTOR = 2;
        public static final int REAR_RIGHT_MOTOR = 3;
        public static final int FRONT_RIGHT_MOTOR = 4;
    }
     
    static class DIO{
        public final static int DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_A = 0;        
        public final static int DRIVE_TRAIN_LEFT_ENCODER_CHANNEL_B = 1;
        public final static int DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_A = 2;
        public final static int DRIVE_TRAIN_RIGHT_ENCODER_CHANNEL_B = 3;
    }


}
