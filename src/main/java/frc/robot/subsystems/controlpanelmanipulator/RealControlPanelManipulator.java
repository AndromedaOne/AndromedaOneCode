/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.controlpanelmanipulator;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.TalonSRXController;
import frc.robot.sensors.colorsensor.ColorSensor;

public class RealControlPanelManipulator extends ControlPanelManipulatorBase {

  private Config controlPanelManipulatorConf;

  public DoubleSolenoid4905 extendSolenoid;
  public TalonSRXController controlPanelMotor;
  public ColorSensor colorSensor;

  /**
   * Creates a new RealControlPanel.
   */
  public RealControlPanelManipulator() {
    controlPanelManipulatorConf = Config4905.getConfig4905().getControlPanelManipulatorConfig();
    extendSolenoid = new DoubleSolenoid4905(controlPanelManipulatorConf, "ExtendSolenoid");
    controlPanelMotor = new TalonSRXController(controlPanelManipulatorConf, "ControlPanelSRXController");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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
    Color detectedColor = colorSensor.getColor();
  }

  @Override
  public void rotateToColor(Color targetColor) {

  }

  @Override
  public void rotateThreeTimes() {
    for (int x = 0; x < 3; x++) {
      rotateOneTime();
    }
  }
}
