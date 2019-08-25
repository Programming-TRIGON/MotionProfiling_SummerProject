package frc.robot;

/**
 * This class is used to store settings for different PIDs
 */
public class PidSettings {

    double KP, KI, KD, KV, KA, tolerance, waitTime, deltaTolerance;

    /**
     * @param KP             The the Proportional coefficient of the PID loop in
     *                       this command.
     * @param KI             The Integral coefficient of the PID loop in this
     *                       command.
     * @param KD             The Differential coefficient of the PID loop in this
     *                       command.

     * @param tolerance      The error tolerance of this command.
     *                       of the setpoint before ending.
     * @param deltaTolerance The maximum allowable change in error from the previous
     *                       iteration.
     */


    public PidSettings(double KP, double KI, double KD, double tolerance, double deltaTolerance) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
        this.tolerance = tolerance;
        this.deltaTolerance = deltaTolerance;
    }
    public PidSettings(double KP, double KI, double KD, double tolerance,double deltaTolerance, double waitTime) {
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
        this.tolerance = tolerance;
        this.deltaTolerance = deltaTolerance;
        this.waitTime = waitTime;}

    public PidSettings(double KP, double KD, double KV, double KA) {
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

    public double getDeltaTolerance(){
        return this.deltaTolerance;
    }

    public void setDeltaTolerance(double deltaTolerance){
        this.deltaTolerance = deltaTolerance;
    }

}
