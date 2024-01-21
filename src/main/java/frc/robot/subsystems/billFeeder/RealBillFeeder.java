package frc.robot.subsystems.billFeeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RealBillFeeder extends SubsystemBase implements BillFeederBase {

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
