package frc.robot.subsystems.controlpanelmanipulator;

import edu.wpi.first.wpilibj.util.Color;

public class MockControlPanelManipulator extends ControlPanelManipulatorBase {

  @Override
  public void extendSystem() {
    System.out.println("Extending Mock Control Panel Manipulator");
  }

  @Override
  public void retractSystem() {
    System.out.println("Retracting Mock Control Panel Manipulator");
  }

  @Override
  public void rotateOneTime() {
    System.out.println("Rotating control panel one time");
  }

  @Override
  public void rotateToColor(Color targetColor) {
    System.out.println("Rotating control panel to color: " + targetColor);
  }

  @Override
  public void rotateThreeTimes() {
    System.out.println("Rotating control panel three times");
  }

}