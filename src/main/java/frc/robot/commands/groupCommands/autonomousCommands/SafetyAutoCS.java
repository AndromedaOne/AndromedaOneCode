// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SAMgripperCommands.OpenCloseGripper;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.driveTrainCommands.MoveWithoutPID;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SafetyAutoCS extends SequentialCommandGroup {
  /** Creates a new SafetyAutoCS. */
  public SafetyAutoCS() {
    final double distanceToMove = 146;
    final double maxOutPut = 0.5;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    Gyro4905 m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    // This assumes robot faces south. We will place cube extending backwards, drive
    // over the
    // charging station leaving the community zone, and drive back onto the station
    // to engage.
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToMove, maxOutPut);
    m_gyro.setInitialZangleOffset(0);
    addCommands(
    addCommands(moveCommand, new SequentialCommandGroup4905(
        new MoveWithoutPID(driveTrain, -45, 0.75, 0), new BalanceRobot(driveTrain, 0.5, 0)));
        new SequentialCommandGroup4905(
            new MiddleScorePosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase(), true, true, true),
            new OpenCloseGripper(subsystemsContainer.getGripper()),
            new StowPosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase()),
            moveCommand),
  }
}
