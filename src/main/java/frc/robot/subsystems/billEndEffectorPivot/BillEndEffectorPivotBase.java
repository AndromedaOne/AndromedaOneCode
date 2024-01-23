package frc.robot.subsystems.billEndEffectorPivot;

import frc.robot.subsystems.SubsystemInterface;

public interface BillEndEffectorPivotBase extends SubsystemInterface {
  public void initialize();

  public void moveHighEndEffector();

  public void moveLowEndEffector();

  public BillEndEffectorState getState();
}
