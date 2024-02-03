package frc.robot.commands.billthovenArmRotateCommands;

import java.util.function.DoubleSupplier;

/** Add your docs here. */
public class ArmRotationSingleton {
  private static ArmRotationSingleton m_instance = new ArmRotationSingleton();
  private double m_angle = 180;

  private ArmRotationSingleton() {
  }

  public static ArmRotationSingleton getInstance() {
    return m_instance;
  }

  public DoubleSupplier getAngle() {
    return () -> m_angle;
  }

  public void setAngle(double angle) {
    m_angle = angle;
  }

}
