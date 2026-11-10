// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.targetDetection.TargetDetectionBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MoveUsingTargetDetection extends SequentialCommandGroup4905 {
  /** Creates a new MoveUsingTargetDetection. */
  public MoveUsingTargetDetection(DriveTrainBase drivetrain, double targetYaw, double maxOutput,
      TargetDetectionBase targetDetection) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new MoveUsingTargetDetectionInternal(drivetrain, targetYaw, maxOutput, targetDetection));
  }

  private class MoveUsingTargetDetectionInternal extends PIDCommand4905 {
    private DriveTrainBase m_drivetrain;
    private double m_targetYaw;
    private double m_maxOutput;
    private TargetDetectionBase m_targetDetection;

    public MoveUsingTargetDetectionInternal(DriveTrainBase drivetrain, double targetYaw,
        double maxOutput, TargetDetectionBase targetDetection) {
      super(new PIDController4905SampleStop("MoveUsingTargetDetection"),
          targetDetection.getYawAsDoubleSupplier(), () -> targetYaw, output -> {
            drivetrain.moveUsingGyro(0.0, targetYaw, false, 0.0);
          }

      );
      m_drivetrain = drivetrain;
      m_targetYaw = targetYaw;
      m_maxOutput = maxOutput;
      m_targetDetection = targetDetection;
      addRequirements(drivetrain.getSubsystemBase());
    }

    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();
      Trace.getInstance().logCommandInfo(this, "Target Yaw: " + m_targetYaw);
      getController().setP(pidConstantsConfig.getDouble("MoveUsingTargetYaw.kp"));
      getController().setI(pidConstantsConfig.getDouble("MoveUsingTargetYaw.ki"));
      getController().setD(pidConstantsConfig.getDouble("MoveUsingTargetYaw.kd"));
      getController()
          .setMinOutputToMove(pidConstantsConfig.getDouble("MoveUsingTargetYaw.minOutputToMove"));
      getController()
          .setTolerance(pidConstantsConfig.getDouble("MoveUsingTargetYaw.positionTolerance"));

      if (m_maxOutput != 0) {
        getController().setMaxOutput(m_maxOutput);
      } else if (pidConstantsConfig.hasPath("MoveUsingTargetYaw.maxOutput")) {
        getController().setMaxOutput(pidConstantsConfig.getDouble("MoveUsingTargetYaw.maxOutput"));
      }

    }

    public DoubleSupplier getSetPoint() {
      return () -> m_targetYaw;
    }
  }
}
