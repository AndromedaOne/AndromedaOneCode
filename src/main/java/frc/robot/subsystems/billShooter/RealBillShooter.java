package frc.robot.subsystems.billShooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RealBillShooter extends SubsystemBase implements BillShooterBase {
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
