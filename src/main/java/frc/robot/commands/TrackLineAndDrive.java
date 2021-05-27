package frc.robot.commands;

import java.util.function.DoubleConsumer;

import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.colorSensor.ColorSensor;
import frc.robot.telemetries.Trace;

public class TrackLineAndDrive extends PIDCommand4905 {

  public TrackLineAndDrive(PIDController4905 controller, ColorSensor colorSensor, DoubleConsumer output,
      double desiredColorValue) {
    super(controller, colorSensor::getValue, () -> desiredColorValue, output,
        Robot.getInstance().getSubsystemsContainer().getDrivetrain());

  }

  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  public void end(boolean interrupted) {
    super.end(interrupted);
    super.initialize();
    Trace.getInstance().logCommandStop(this);
  }

}
