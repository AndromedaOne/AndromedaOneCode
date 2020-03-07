package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.NavXGyroSensor;

public class SetGyroAdjustment extends CommandBase {
    private NavXGyroSensor gyro;
    public SetGyroAdjustment(double angle) {
        gyro = NavXGyroSensor.getInstance();
        gyro.setAngleAdjustment(angle);
    }
}