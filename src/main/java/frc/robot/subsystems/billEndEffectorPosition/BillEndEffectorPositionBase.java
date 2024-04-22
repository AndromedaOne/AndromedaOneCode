package frc.robot.subsystems.billEndEffectorPosition;

import frc.robot.subsystems.SubsystemInterface;

public interface BillEndEffectorPositionBase extends SubsystemInterface {

  public void moveHighEndEffector();

  public void moveLowEndEffector();

  public BillEndEffectorState getState();

}
