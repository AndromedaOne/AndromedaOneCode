package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.sensors.gyro.NavXGyroSensor;

public class SetGyroAdjustment extends CommandBase {
  private NavXGyroSensor gyro;

  public SetGyroAdjustment(double angle) {
    gyro = Robot.getInstance().getSensorsContainer().getNavXGyro();
    gyro.setAngleAdjustment(angle);
  }
}