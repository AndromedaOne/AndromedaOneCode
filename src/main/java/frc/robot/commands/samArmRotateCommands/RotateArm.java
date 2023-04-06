// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmRotateCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class RotateArm extends SequentialCommandGroup4905 {
  public RotateArm(SamArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
      boolean useSmartDashboard) {
    addCommands(new WaitForRetract(armRotate),
        new RotateArmInternal(armRotate, angle, needToEnd, useSmartDashboard));
  }

  public RotateArm(SamArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd) {
    this(armRotate, angle, needToEnd, false);
  }

  private class WaitForRetract extends CommandBase {
    private SamArmRotateBase m_armRotate;

    public WaitForRetract(SamArmRotateBase armRotate) {
      m_armRotate = armRotate;
      addRequirements(armRotate);
    }

    public void execute() {
      m_armRotate.engageArmBrake();
    }

    public void end(boolean interrupted) {
      m_armRotate.disengageArmBrake();
    }

    public boolean isFinished() {
      return Robot.getInstance().getSubsystemsContainer().getArmExtRetBase().getPosition() < 30;
    }
  }

  private class RotateArmInternal extends PIDCommand4905 {

    private SamArmRotateBase m_armRotate;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = false;
    private FeedForward m_feedForward = new RotateFeedForward();
    private InterpolatingMap m_kMap;

    public RotateArmInternal(SamArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
        boolean useSmartDashboard) {

      super(new PIDController4905SampleStop("ArmRotate"), armRotate::getAngle, angle, output -> {
        armRotate.rotate(output);
      }, armRotate);
      m_armRotate = armRotate;
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;

      m_kMap = new InterpolatingMap(Config4905.getConfig4905().getSamArmRotateConfig(),
          "armKValues");

      if (useSmartDashboard) {
        SmartDashboard.putNumber("Rotate Arm P-value", 0);
        SmartDashboard.putNumber("Rotate Arm I-value", 0);
        SmartDashboard.putNumber("Rotate Arm D-value", 0);
        SmartDashboard.putNumber("Rotate Arm Feed Forward", 0);
        SmartDashboard.putNumber("Rotate Arm Angle", 180);
      }
    }

    public RotateArmInternal(SamArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd) {
      this(armRotate, angle, needToEnd, false);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getCommandConstantsConfig();
      super.initialize();

      if (m_useSmartDashboard) {
        getController().setP(SmartDashboard.getNumber("Rotate Arm P-value", 0));
        getController().setI(SmartDashboard.getNumber("Robot Arm I-value", 0));
        getController().setD(SmartDashboard.getNumber("Robot Arm D-value", 0));
      } else {
        getController().setP(pidConstantsConfig.getDouble("ArmRotate.Kp"));
        getController().setI(pidConstantsConfig.getDouble("ArmRotate.Ki"));
        getController().setD(pidConstantsConfig.getDouble("ArmRotate.Kd"));
      }
      getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmRotate.minOutputToMove"));
      getController().setTolerance(pidConstantsConfig.getDouble("ArmRotate.tolerance"));
      if (m_useSmartDashboard) {
        getController()
            .setFeedforward(() -> SmartDashboard.getNumber("Rotate Arm Feed Forward", 0));
        setSetpoint(() -> SmartDashboard.getNumber("Rotate Arm Angle", 0));
      } else {
        getController().setFeedforward(m_feedForward);
      }

      Trace.getInstance().logCommandInfo(this, "Rotate Arms to: " + m_setpoint.getAsDouble());
      m_armRotate.disengageArmBrake();
      Trace.getInstance().logCommandStart(this);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      super.end(interrupted);
      Trace.getInstance().logCommandStop(this);
      Trace.getInstance().logCommandInfo(this, "Ending Angle: " + m_armRotate.getAngle());
      m_armRotate.engageArmBrake();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      if (m_needToEnd && isOnTarget()) {
        return true;
      }
      return false;
    }

    public boolean isOnTarget() {
      return getController().atSetpoint();
    }

    private class RotateFeedForward implements FeedForward {

      @Override
      public double calculate() {
        return m_kMap.getInterpolatedValue(m_armRotate.getAngle());
      }

    }
  }
}
