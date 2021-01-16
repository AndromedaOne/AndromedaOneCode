package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;

public class GalacticSearchPathA extends SequentialCommandGroup {
  private DriveTrain m_driveTrain;

  private static final double MINIMIZING_FACTOR = 1.0;
  private static final double START_TO_E6_POWER_CELL_DISTANCE = 150 * MINIMIZING_FACTOR; // in inches
  private static final double E6_TO_A6_DISTANCE = 120 * MINIMIZING_FACTOR; // in inches
  private static final double A6_TO_FINISH_LINE_DISTANCE = 175 * MINIMIZING_FACTOR; // in inches
  private static final double ANGLE_TO_LINE_UP_WITH_INITIAL_CELLS = 47.3; // degrees
  private static final double ANGLE_TO_LINE_UP_WITH_FINAL_CELLS = 49.9; // degrees

  public GalacticSearchPathA(DriveTrain driveTrain, IntakeBase intake) {
    m_driveTrain = driveTrain;
    addCommands(new ParallelDeadlineGroup(getDriveTrainpathWay(), new DeployAndRunIntake(intake, () -> false)));
  }

  public SequentialCommandGroup getDriveTrainpathWay() {
    return new SequentialCommandGroup(new MoveUsingEncoder(m_driveTrain, START_TO_E6_POWER_CELL_DISTANCE, ANGLE_TO_LINE_UP_WITH_INITIAL_CELLS, 0.4),
        new TurnToCompassHeading(270), new MoveUsingEncoder(m_driveTrain, E6_TO_A6_DISTANCE, true, 270),
        new TurnToCompassHeading(ANGLE_TO_LINE_UP_WITH_FINAL_CELLS),
        new MoveUsingEncoder(m_driveTrain, A6_TO_FINISH_LINE_DISTANCE, true, ANGLE_TO_LINE_UP_WITH_FINAL_CELLS));
  }

}
