package frc.robot.subsystems.billArmPivot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RealBillArmPivot extends SubsystemBase implements BillArmPivotBase {

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
