// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class LeftTrenchLoop extends SequentialCommandGroup4905 {
  /**
   * Creates a new GoToCenterRight.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  public LeftTrenchLoop() throws FileVersionException, IOException, ParseException {
    Command autoCommand = AutoBuilder.buildAuto("LeftTrenchLoop");
    addCommands(new DelayedSequentialCommandGroup(autoCommand));
  }

}