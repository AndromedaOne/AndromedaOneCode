// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockCoralIntakeEject implements CoralIntakeEjectBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void runWheels(double speed) {
  }

  @Override
  public void runWheelsEject() {
  }

  @Override
  public void setEjectState() {
  }

  @Override
  public boolean getCoralDetected() {
    return true;
  }

  @Override
  public boolean hasScored() {
    return false;
  }

  @Override
  public void stop() {
  }

  @Override
  public boolean intakeDetector() {
    return false;
  }

  @Override
  public boolean ejectDetector() {
    return false;
  }

  @Override
  public void setCoastMode() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void setDriveController(DriveController driveController) {
  }

  @Override
  public void exitL4ScoringPosition() {
  }

  @Override
  public void scoreL4() {
  }

  public void setSubsystemController(SubsystemController subsystemController) {
  }

}
