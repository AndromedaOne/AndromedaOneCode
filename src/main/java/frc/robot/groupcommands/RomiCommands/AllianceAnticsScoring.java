// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.groupcommands.RomiCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.PauseRobot;
import frc.robot.commands.ToggleConveyor;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnDeltaAngle;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class AllianceAnticsScoring extends SequentialCommandGroup {
  /** Creates a new AllianceAnticsScoring. */

  public AllianceAnticsScoring(DriveTrain driveTrain) {
    MoveUsingEncoder move = new MoveUsingEncoder(driveTrain, 68, 0.5);
    TurnDeltaAngle turn = new TurnDeltaAngle(180);
    ToggleConveyor start = new ToggleConveyor(Robot.getInstance().getSubsystemsContainer().getConveyor());
    PauseRobot pause = new PauseRobot(7000, driveTrain);
    ToggleConveyor stop = new ToggleConveyor(Robot.getInstance().getSubsystemsContainer().getConveyor());
    MoveUsingEncoder foward = new MoveUsingEncoder(driveTrain, 8, 0.5);
    TurnDeltaAngle align = new TurnDeltaAngle(-90);
    MoveUsingEncoder approach = new MoveUsingEncoder(driveTrain, -15, 0.5);
    TurnDeltaAngle adjust = new TurnDeltaAngle(90);
    MoveUsingEncoder park = new MoveUsingEncoder(driveTrain, -15, 0.5);
    addCommands(move, turn, start, pause, stop, foward, align, approach, adjust, park);
  }

}