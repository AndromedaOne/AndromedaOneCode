package frc.robot.lib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public enum ButtonsEnumerated {
  ABUTTON(1), BBUTTON(2), XBUTTON(3), YBUTTON(4), LEFTBUMPERBUTTON(5), RIGHTBUMPERBUTTON(6),
  BACKBUTTON(7), STARTBUTTON(8), LEFTSTICKBUTTON(9), RIGHTSTICKBUTTON(10);

  public int getValue() {
    return m_buttonValue;
  }

  private int m_buttonValue;

  private ButtonsEnumerated(int value) {
    m_buttonValue = value;
  }

  public static JoystickButton getJoystickButton(ButtonsEnumerated button, Joystick joystick) {
    return button.getJoystickButton(joystick);
  }

  public JoystickButton getJoystickButton(GenericHID joystick) {
    return new JoystickButton(joystick, m_buttonValue);
  }

  public static boolean isPressed(ButtonsEnumerated button, Joystick joystick) {
    return button.isPressed(joystick);
  }

  public boolean isPressed(Joystick joystick) {
    return joystick.getRawButton(m_buttonValue);
  }

}