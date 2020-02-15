package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ConfigReload;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class MoveUsingEncoderTester extends SequentialCommandGroup {
  private MoveUsingEncoder m_command;

  public MoveUsingEncoderTester(DriveTrain drivetrain) {
    super();
    SmartDashboard.putNumber("Distance To Move", 12);
    m_command = new MoveUsingEncoder(drivetrain, SmartDashboard.getNumber("Distance To Move", 12));
    addCommands(new ConfigReload(), m_command);
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart("MoveUsingEncoderTester");
    m_command.setDistance(SmartDashboard.getNumber("Distance To Move", 12));
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop("MoveUsingEncoderTester");
  }

}
