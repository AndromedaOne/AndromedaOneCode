// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limeLightCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.sensors.SensorsContainer;
import frc.robot.telemetries.Trace;

public class TestMidCubeTurnToFaceCommand extends SequentialCommandGroup {
  /** Creates a new TestMidCubeTurnToFaceCommand. */
  public TestMidCubeTurnToFaceCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    int currentPipeline = 1;
    SensorsContainer m_sensorscontainer = Robot.getInstance().getSensorsContainer();

    m_sensorscontainer.getLimeLight().setPipeline(currentPipeline);
    m_sensorscontainer.getLimeLight().enableLED();
    m_sensorscontainer.getLimeLight().updateSmartDashboardReadings();
    Trace.getInstance().logInfo("The pipeline number is " + currentPipeline);
    Trace.getInstance().logCommandInfo(this, "Horizonal Degrees to Target:"
        + m_sensorscontainer.getLimeLight().horizontalDegreesToTarget());
    Trace.getInstance().logCommandInfo(this, "Vertical Radians to Target:"
        + m_sensorscontainer.getLimeLight().verticalRadiansToTarget());
    Trace.getInstance().logCommandInfo(this,
        "Distance to Target:" + m_sensorscontainer.getLimeLight().distanceToNode());
    DoubleSupplier zyx = Robot.getInstance().getSensorsContainer()
        .getLimeLight()::horizontalDegreesToTarget;
    System.out.println(this + "Test mid-Horizonal Degrees to target"
        + m_sensorscontainer.getLimeLight().horizontalDegreesToTarget());
    System.out.println(this + "Test mid-Vertical Radians to target"
        + m_sensorscontainer.getLimeLight().verticalRadiansToTarget());
    System.out.println(
        this + "Test mid-Distance to target" + m_sensorscontainer.getLimeLight().distanceToNode());
    addCommands(new TurnToFaceCommand(zyx));
    System.out.println(this + "Horizonal Degrees to target"
        + m_sensorscontainer.getLimeLight().horizontalDegreesToTarget());
    System.out.println(this + "Vertical Radians to target"
        + m_sensorscontainer.getLimeLight().verticalRadiansToTarget());
    System.out
        .println(this + "Distance to target" + m_sensorscontainer.getLimeLight().distanceToNode());
    m_sensorscontainer.getLimeLight().updateSmartDashboardReadings();
    m_sensorscontainer.getLimeLight().disableLED();
  }

  // Called when the command is initially scheduled.

}
