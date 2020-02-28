/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.controlpanelmanipulator;

import com.revrobotics.ColorMatchResult;
import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.TalonSRXController;
import frc.robot.sensors.colorsensor.ColorSensor;

public class RealControlPanelManipulator extends ControlPanelManipulatorBase {

  private Config controlPanelManipulatorConf;

  public DoubleSolenoid4905 extendSolenoid;
  public TalonSRXController controlPanelMotor;
  public ColorSensor colorSensor;
  private double controlPanelMotorSpeed;

  /**
   * Creates a new RealControlPanel.
   */
  public RealControlPanelManipulator() {
    controlPanelManipulatorConf = Config4905.getConfig4905().getControlPanelManipulatorConfig();
    extendSolenoid = new DoubleSolenoid4905(controlPanelManipulatorConf, "ExtendSolenoid");
    controlPanelMotor = new TalonSRXController(controlPanelManipulatorConf, "ControlPanelSRXController");
    controlPanelMotorSpeed = controlPanelManipulatorConf.getDouble("speed");
  }

  @Override
  public void periodic() {

  }

  @Override
  public void extendSystem() {
    extendSolenoid.extendPiston();
  }

  @Override
  public void retractSystem() {
    extendSolenoid.retractPiston();
  }

  @Override
  public void rotateOneTime() {
    ColorMatchResult originalColor = colorSensor.getColor();
    // Run through this twice for a full rotation
    for (int x = 0; x < 2; x++) {
      // Spin until not on original color
      for (ColorMatchResult detectedColor = colorSensor
          .getColor(); originalColor == detectedColor; detectedColor = colorSensor.getColor()) {
        runMotor(controlPanelMotorSpeed);
      }
      // Continue spinning until back on original color
      while (originalColor != colorSensor.getColor()) {
        runMotor(controlPanelMotorSpeed);
      }
    }
    stopMotor();
  }

  // Rotates to a specific color. Wants 'red', 'blue', 'green', or 'yellow'.
  @Override
  public void rotateToColor(ColorMatchResult targetColor) {
    for (ColorMatchResult detectedColor = colorSensor
        .getColor(); targetColor != detectedColor; detectedColor = colorSensor.getColor()) {
      runMotor(controlPanelMotorSpeed);
    }
    stopMotor();
  }

  @Override
  public void rotateControlPanel(int timesToRotate) {
    for (int x = 0; x < timesToRotate; x++) {
      rotateOneTime();
    }
  }

  @Override
  public void runMotor(double speed) {
    controlPanelMotor.set(speed);
  }

  @Override
  public void stopMotor() {
    runMotor(0);
  }
}
