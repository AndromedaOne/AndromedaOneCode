package frc.robot.commands.billthovenArmRotateCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class ArmRotate extends SequentialCommandGroup4905 {
  public ArmRotate(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
      boolean useSmartDashboard) {
    addCommands(new RotateArmInternal(armRotate, angle, needToEnd, useSmartDashboard));
  }

  public ArmRotate(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd) {
    this(armRotate, angle, needToEnd, false);
  }

  private class RotateArmInternal extends PIDCommand4905 {
    private BillArmRotateBase m_armRotate;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = false;
    private FeedForward m_feedForward = new RotateFeedForward();
    private InterpolatingMap m_kMap;

    public RotateArmInternal(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
        boolean useSmartDashboard) {

      super(new PIDController4905SampleStop("ArmRotate"), armRotate::getAngle, angle, output -> {
        armRotate.rotate(output);
      }, armRotate.getSubsystemBase());
      m_armRotate = armRotate;
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;

      m_kMap = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(), "armKValues");

      if (m_useSmartDashboard) {
        SmartDashboard.putNumber("Rotate Arm P-value", 0);
        SmartDashboard.putNumber("Rotate Arm I-value", 0);
        SmartDashboard.putNumber("Rotate Arm D-value", 0);
        SmartDashboard.putNumber("Rotate Arm Feed Forward", 0);
        SmartDashboard.putNumber("Rotate PID Arm Angle Setpoint", 50);
      }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getArmRotateConfig();
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
        setSetpoint(() -> SmartDashboard.getNumber("Rotate PID Arm Angle Setpoint", 50));
      } else {
        getController().setFeedforward(m_feedForward);
      }

      Trace.getInstance().logCommandInfo(this, "Rotate Arms to: " + m_setpoint.getAsDouble());
      m_armRotate.disengageArmBrake();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      super.end(interrupted);
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
