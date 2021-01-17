package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;

public class BarrelRacingPath extends SequentialCommandGroup {

  private DriveTrain m_driveTrain;
  private static final double MINIMIZING_FACTOR = 1.0;
  private static final double B1_TO_B3_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double B_TO_D_VERTICALDISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double D3_TO_D6_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double B6_TO_B9_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double D9_TO_D11_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  double maximumPower = 0.5;
  double robotLengthInches = 38;

  public BarrelRacingPath(DriveTrain driveTrain, IntakeBase intake) {
    m_driveTrain = driveTrain;
    addCommands(new DelayedSequentialCommandGroup(
        new MoveUsingEncoder(driveTrain, 120 + robotLengthInches / 2, 0, maximumPower), new TurnToCompassHeading(90),
        new MoveUsingEncoder(driveTrain, 60, 90, maximumPower), new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, 60, 180, maximumPower), new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, 60, 270, maximumPower), new TurnToCompassHeading(0),
        new MoveUsingEncoder(driveTrain, 150, 0, maximumPower), new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, 60, 270, maximumPower), new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, 60, 180, maximumPower), new TurnToCompassHeading(90),
        new MoveUsingEncoder(driveTrain, 60, 90, maximumPower), new TurnToCompassHeading(0),
        new MoveUsingEncoder(driveTrain, 120, 0, maximumPower), new TurnToCompassHeading(90),
        new MoveUsingEncoder(driveTrain, 60, 90, maximumPower), new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, 60, 180, maximumPower), new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, 60, 270, maximumPower), new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, 240, 180, maximumPower)));
  }

}