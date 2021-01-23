package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class BarrelRacingPath extends SequentialCommandGroup {

  private DriveTrain m_driveTrain;
  private static final double MINIMIZING_FACTOR = 1.0;
  private static final double B1_TO_B3_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double B_TO_D_VERTICALDISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double D3_TO_D6_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double B6_TO_B9_POWER_CELL_DISTANCE = 90 * MINIMIZING_FACTOR; // in inches
  private static final double D9_TO_D11_POWER_CELL_DISTANCE = 60 * MINIMIZING_FACTOR; // in inches
  private static final double C2_ROW = 90;
  private static final double C2_COL = 60;
  private static final double C6_ROW = 90;
  private static final double C6_COL = 180;
  double maximumPower = 0.5;
  double robotLengthInches = 38;

  public static double compute_row(String position) {
    if (position.charAt(0) == 'A') {
      return 150.0;
    } else if (position.charAt(0) == 'B') {
      return 120;
    } else if (position.charAt(0) == 'C') {
      return 90;
    } else if (position.charAt(0) == 'D') {
      return 60;
    } else if (position.charAt(0) == 'E') {
      return 30;
    } else {
      return 0;
    }
  }

  public static double compute_col(String position) {

    double distance = 0;
    String col = position.substring(1);
    System.out.println("position = " + position);
    System.out.println("CharAt = " + position.charAt(0));
    System.out.println("subString = " + col);
    if (col.equals("1")) {
      distance = 30;
    } else if (col.equals("2")) {
      distance = 60;
    } else if (col.equals("3")) {
      distance = 90;
    } else if (col.equals("4")) {
      distance = 120;
    } else if (col.equals("5")) {
      distance = 150;
    } else if (col.equals("6")) {
      distance = 180;
    } else if (col.equals("7")) {
      distance = 210;
    } else if (col.equals("8")) {
      distance = 240;
    } else if (col.equals("9")) {
      distance = 270;
    } else if (col.equals("10")) {
      distance = 300;
    } else if (col.equals("11")) {
      distance = 330;
    }
    System.out.println("distance = " + distance);
    return distance;
  }

  public BarrelRacingPath(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
    addCommands(new DelayedSequentialCommandGroup(
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("C6") - compute_col("C2")) + robotLengthInches / 2, 0,
            maximumPower),
        new TurnToCompassHeading(90),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_row("E6") - compute_row("C6")), 90, maximumPower),
        new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("E4") - compute_col("E6")), 180, maximumPower),
        new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_row("C4") - compute_row("E4")), 270, maximumPower),
        new TurnToCompassHeading(0),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("C9") - compute_col("C4")), 0, maximumPower),
        new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_row("A9") - compute_row("C9")), 270, maximumPower),
        new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("A7") - compute_col("A9")), 180, maximumPower),
        new TurnToCompassHeading(90),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_row("E7") - compute_row("A7")), 90, maximumPower),
        new TurnToCompassHeading(0),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("E11") - compute_col("E7")), 0, maximumPower),
        new TurnToCompassHeading(270),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_row("C11") - compute_row("E11")), 270, maximumPower),
        new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, Math.abs(compute_col("C2") - compute_col("C11")), 180, maximumPower)));

  }

}