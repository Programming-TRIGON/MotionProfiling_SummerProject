package frc.robot.vision;

public enum Target {
    RocketMiddle(0, 91, 25), target2(1, 0, 0);
    private final int index;
    private final double height;
    private final double setpoint;

    Target(int index, double height, double setpoint) {
        this.index = index;
        this.height = height;
        this.setpoint = setpoint;
}


    public int getIndex() {
        return index;
    }

    public double getHeight() {
        return height;
    }

    public double getSetpoint() {
        return setpoint;
    }
}
