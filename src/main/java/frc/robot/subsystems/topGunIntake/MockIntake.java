// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunIntake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

public class MockIntake implements IntakeBase {
  private double m_previousSpeed = 0;

  public void runIntakeWheels(double speed) {
    if (speed != m_previousSpeed) {
      System.out.println("Running intake at speed :" + speed);
      m_previousSpeed = speed;
    }
  }

  public void stopIntakeWheels() {
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

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
