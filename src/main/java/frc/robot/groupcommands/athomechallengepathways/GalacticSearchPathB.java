package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;

public class GalacticSearchPathB extends SequentialCommandGroup {

  private DriveTrain m_driveTrain;
  private static final double MINIMIZING_FACTOR = 1.0;
  private static final double B1_TO_B3_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double B_TO_D_VERTICALDISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double D3_TO_D6_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double B6_TO_B9_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double D9_TO_D11_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches

  public GalacticSearchPathB(DriveTrain driveTrain, IntakeBase intake) {
    m_driveTrain = driveTrain;
    addCommands(new ParallelDeadlineGroup(getDriveTrainpathWay(), new DeployAndRunIntake(intake, () -> false)));
  }

  public SequentialCommandGroup getDriveTrainpathWay() {
    return new SequentialCommandGroup(new MoveUsingEncoder(m_driveTrain, B1_TO_B3_POWER_CELL_DISTANCE, true, 0),
        new TurnToCompassHeading(90), new MoveUsingEncoder(m_driveTrain, B_TO_D_VERTICALDISTANCE, true, 90),
        new TurnToCompassHeading(0), new MoveUsingEncoder(m_driveTrain, D3_TO_D6_POWER_CELL_DISTANCE, true, 0),
        new TurnToCompassHeading(270), new MoveUsingEncoder(m_driveTrain, B_TO_D_VERTICALDISTANCE, true, 270),
        new TurnToCompassHeading(0), new MoveUsingEncoder(m_driveTrain, B6_TO_B9_POWER_CELL_DISTANCE, true, 0),
        new TurnToCompassHeading(90), new MoveUsingEncoder(m_driveTrain, B_TO_D_VERTICALDISTANCE, true, 90),
        new TurnToCompassHeading(0), new MoveUsingEncoder(m_driveTrain, D9_TO_D11_POWER_CELL_DISTANCE, true, 0));
  }
}