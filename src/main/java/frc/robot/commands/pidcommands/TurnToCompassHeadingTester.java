package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ConfigReload;
import frc.robot.telemetries.Trace;

public class TurnToCompassHeadingTester extends SequentialCommandGroup {
  private TurnToCompassHeading m_command;

  public TurnToCompassHeadingTester(double heading) {
    super();
    SmartDashboard.putNumber("Compass Heading", 0);
    m_command = new TurnToCompassHeading(heading);
    addCommands(new ConfigReload(), m_command);
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
    m_command.setCompassHeading(SmartDashboard.getNumber("Compass Heading", 0));
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }

}
