// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.limeLightCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.sensors.SensorsContainer;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TestMidNodeTurnToFaceCommand extends SequentialCommandGroup {
  /** Creates a new TestMidNodeTurnToFaceCommand. */
  public TestMidNodeTurnToFaceCommand() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    SensorsContainer m_sensorscontainer = Robot.getInstance().getSensorsContainer();

    m_sensorscontainer.getLimeLight().enableLED();
    m_sensorscontainer.getLimeLight().updateSmartDashboardReadings();
    Trace.getInstance().logCommandInfo(this, "Horizonal Degrees to Target:"
        + m_sensorscontainer.getLimeLight().horizontalDegreesToTarget());
    Trace.getInstance().logCommandInfo(this, "Vertical Radians to Target:"
        + m_sensorscontainer.getLimeLight().verticalRadiansToTarget());
    Trace.getInstance().logCommandInfo(this,
        "Distance to Target:" + m_sensorscontainer.getLimeLight().distanceToPowerPort());
    DoubleSupplier xyz = Robot.getInstance().getSensorsContainer()
        .getLimeLight()::horizontalDegreesToTarget;
    addCommands(new TurnToFaceCommand(xyz));
    Trace.getInstance().logCommandInfo(this, "Horizonal Degrees to Target:"
        + m_sensorscontainer.getLimeLight().horizontalDegreesToTarget());
    Trace.getInstance().logCommandInfo(this, "Vertical Radians to Target:"
        + m_sensorscontainer.getLimeLight().verticalRadiansToTarget());
    Trace.getInstance().logCommandInfo(this,
        "Distance to Target:" + m_sensorscontainer.getLimeLight().distanceToPowerPort());
    m_sensorscontainer.getLimeLight().updateSmartDashboardReadings();
    m_sensorscontainer.getLimeLight().disableLED();
  }
}
