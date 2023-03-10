// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SAMgripperCommands.OpenCloseGripper;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.MoveToCenterOfChargingStation;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.BottomScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PlacePickEngageAutoDock extends SequentialCommandGroup {
  /** Creates a new PlacePickEngageAutoDock. */
  public PlacePickEngageAutoDock() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    // 166 may need to be lowered.
    final double distanceToMove = -166;
    final double maxOutPut = 0.5;
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToMove, maxOutPut);
    addCommands(new SequentialCommandGroup4905(
        new MiddleScorePosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase(), true, true, false),
        new OpenCloseGripper(subsystemsContainer.getGripper()),
        new StowPosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase()),
        moveCommand,
        // Add turn command, roughly 45 degrees.
        new BottomScorePosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase(), true, true, true),
        new OpenCloseGripper(subsystemsContainer.getGripper()),
        new StowPosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase()),
        // TBD: If we need to rotate before moving.
        new MoveToCenterOfChargingStation(driveTrain, 45, 0.75, 0),
        new BalanceRobot(driveTrain, 0.5, 0)));

  }
}
