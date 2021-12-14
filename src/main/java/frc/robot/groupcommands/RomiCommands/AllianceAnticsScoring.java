// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.groupcommands.RomiCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.PauseRobot;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnDeltaAngle;
import frc.robot.commands.romiCommands.ToggleConveyor;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class AllianceAnticsScoring extends SequentialCommandGroup {
  /** Creates a new AllianceAnticsScoring. */

  public AllianceAnticsScoring(DriveTrain driveTrain) {

    ToggleConveyor start = new ToggleConveyor(
        Robot.getInstance().getSubsystemsContainer().getConveyor(), 0.6);
    MoveUsingEncoder move = new MoveUsingEncoder(driveTrain, 69, 0.55);
    TurnDeltaAngle turn = new TurnDeltaAngle(180);
    MoveUsingEncoder backward = new MoveUsingEncoder(driveTrain, -4, 0.5);
    PauseRobot pause = new PauseRobot(5500, driveTrain);
    ToggleConveyor stop = new ToggleConveyor(
        Robot.getInstance().getSubsystemsContainer().getConveyor(), 0.6);
    MoveUsingEncoder foward = new MoveUsingEncoder(driveTrain, 13, 0.5);
    TurnDeltaAngle align = new TurnDeltaAngle(-90);
    MoveUsingEncoder approach = new MoveUsingEncoder(driveTrain, -16, 0.5);
    TurnDeltaAngle adjust = new TurnDeltaAngle(85);
    MoveUsingEncoder park = new MoveUsingEncoder(driveTrain, -15.5, 0.5);
    addCommands(start, move, turn, backward, pause, stop, foward, align, approach, adjust, park);
  }

}