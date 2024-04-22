package frc.robot.commands.billthovenShooterCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;

public class TuneBillShooterFeedForward extends Command {
  /**
   * Creates a new TuneBillShooterFeedForward.
   */
  private BillShooterBase m_shooterWheel;

  public TuneBillShooterFeedForward(BillShooterBase shooterWheel, BillFeederBase feeder) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooterWheel = shooterWheel;
    SmartDashboard.putNumber("Shooter Feed Forward Value", 0.00025);
    SmartDashboard.putNumber("Shooter p Value", 0.0001);
    SmartDashboard.putNumber("Bill ShooterRPMTarget", 3000);
    System.out.println("end constructor of TuneBillShooterFeedForward");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("TuneBillShooterInitialize");
    double feedForward = SmartDashboard.getNumber("Shooter Feed Forward Value", 0.00025);
    double PValue = SmartDashboard.getNumber("Shooter p Value", 0.0001);
    double shootRPM = SmartDashboard.getNumber("Bill ShooterRPMTarget", 3000);
    System.out.println("Scheduling RunBillShooterWheelVelocity");
    CommandScheduler.getInstance()
        .schedule(new RunBillShooterWheelVelocity(m_shooterWheel, () -> shootRPM, true, feedForward,
            PValue, Config4905.getConfig4905().getBillShooterConfig(), () -> false));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
