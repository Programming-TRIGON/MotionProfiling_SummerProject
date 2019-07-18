
package frc.robot;

/**
 * this class is used to store settings for different PIDs
 */
public class SettingsForPID {

    double KP, KI, KD, KV, KA, tolerance, waitTime;
    /**
    * @param KP the the Proportional coefficient of the PID loop in this command.
    * @param KI the Integral coefficient of the PID loop in this command.
    * @param KD the Differential coefficient of the PID loop in this command.
    * @param KV the velocity at which to go.
    * @param KA the acceleration of the robot
    * @param tolerance  the error tolerance of this command.
    * @param waitTime  the time this PID loop will wait while within tolerance of the setpoint before ending.
    */
    public SettingsForPID(double KP, double KI, double KD, double tolerance, double waitTime) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
        this.tolerance = tolerance;
        this.waitTime = waitTime;

    }

    public SettingsForPID(double KP, double KD, double KV, double KA) {
        this.KP = KP;
        this.KD = KD;
        this.KV = KV;
        this.KA = KA;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getKP() {
        return KP;
    }

    public void setKP(double KP) {
        this.KP = KP;
    }

    public double getKI() {
        return KI;
    }

    public void setKI(double KI) {
        this.KI = KI;
    }

    public double getKD() {
        return KD;
    }

    public void setKD(double KD) {
        this.KD = KD;
    }

    public double getKV() {
        return KV;
    }

    public void setKV(double KV) {
        this.KV = KV;
    }

    public double getKA() {
        return KA;
    }
    
    public void setKA(double KA) {
        this.KA = KA;
    }    

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

}
