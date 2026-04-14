// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoCommands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.FileVersionException;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LeftTrenchDoubleLoop extends SequentialCommandGroup4905 {
  /** Creates a new LeftTrenchDoubleLoop. */
  public LeftTrenchDoubleLoop() throws FileVersionException, IOException, ParseException {
    Command autoCommand = AutoBuilder.buildAuto("LeftTrenchDoubleLoop");
    addCommands(new DelayedSequentialCommandGroup(autoCommand));
  }
}
