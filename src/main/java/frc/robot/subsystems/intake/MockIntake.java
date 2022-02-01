// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

public class MockIntake extends IntakeBase {
  private double m_previousSpeed = 0;

  public void runIntake(double speed) {
    if (speed != m_previousSpeed) {
      System.out.println("Running intake at speed :" + speed);
      m_previousSpeed = speed;
    }
  }

  public void stopIntake() {
    System.out.println("Stopping intake.");
    m_previousSpeed = 0;
  }

  @Override
  public void deployIntake() {
    System.out.println("deploy intake");
  }

  @Override
  public void retractIntake() {
    System.out.println("retract intake");
  }

  /** Creates a new MockIntake. */
  /*
   * public MockIntake() { }
   * 
   * @Override public void periodic() { // This method will be called once per
   * scheduler run }
   */
}
