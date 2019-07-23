package frc.robot;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;
import frc.robot.RobotConstants.Path;

/**
 * This class splits the trajectories for each side of the robot.
 */
public class SplitTrajectories {

    private TankModifier modifier;
    private Path path;

    public SplitTrajectories(Path path) {
        this.modifier = new TankModifier(this.path.getTrajectory());
        this.modifier.modify(RobotConstants.WHEEL_BASE_WIDTH);
    }

    public Trajectory getRightTrajectory() {
        return modifier.getRightTrajectory();
    }

    public Trajectory getLeftTrajectory() {
        return modifier.getLeftTrajectory();
    }
}
