package frc.robot.subsystems.billEndEffectorPivot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RealBillEndEffectorPivot extends SubsystemBase implements BillEndEffectorPivotBase {

  // Needs methods
  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

}
