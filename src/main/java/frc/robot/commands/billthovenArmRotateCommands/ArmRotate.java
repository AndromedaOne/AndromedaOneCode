package frc.robot.commands.billthovenArmRotateCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.commands.billthovenClimberCommands.BillClimberSingleton;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class ArmRotate extends SequentialCommandGroup4905 {
  public ArmRotate(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
      boolean useSmartDashboard, boolean engagePneumaticBrake, boolean rotateWhileClimb) {
    addCommands(new RotateArmInternal(armRotate, angle, needToEnd, useSmartDashboard,
        engagePneumaticBrake, rotateWhileClimb));
  }

  public ArmRotate(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd) {
    this(armRotate, angle, needToEnd, false, true, false);
  }

  public ArmRotate(BillArmRotateBase armRotate, boolean rotateWhileClimb, DoubleSupplier angle,
      boolean needToEnd) {
    this(armRotate, angle, needToEnd, false, true, rotateWhileClimb);
  }

  public ArmRotate(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
      boolean engagePneumaticBrake) {
    this(armRotate, angle, needToEnd, false, engagePneumaticBrake, false);
  }

  private class RotateArmInternal extends PIDCommand4905 {
    private BillArmRotateBase m_armRotate;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = false;
    private RotateFeedForward m_feedForward = new RotateFeedForward();
    private InterpolatingMap m_kMapUp;
    private InterpolatingMap m_pMapUp;
    private InterpolatingMap m_kMapDown;
    private InterpolatingMap m_pMapDown;
    private boolean m_engagePneumaticBrake;
    private double m_count = 0;
    private boolean m_rotateWhileClimb = false;

    public RotateArmInternal(BillArmRotateBase armRotate, DoubleSupplier angle, boolean needToEnd,
        boolean useSmartDashboard, boolean engagePneumaticBrake, boolean rotateWhileClimb) {

      super(new PIDController4905SampleStop("ArmRotate"), armRotate::getAngle, angle, output -> {
        armRotate.rotate(output);
      }, armRotate.getSubsystemBase());
      m_armRotate = armRotate;
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;
      m_engagePneumaticBrake = engagePneumaticBrake;
      m_rotateWhileClimb = rotateWhileClimb;
      addRequirements(armRotate.getSubsystemBase());

      loadMaps();

      SmartDashboard.putNumber("Rotate PID Arm Angle Setpoint", 300);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      Config pidConstantsConfig = Config4905.getConfig4905().getArmRotateConfig();
      super.initialize();
      m_count = 0;
      getController().setMaxOutput(1);
      if (m_useSmartDashboard) {
        loadMaps();
        System.out.println("pMapDown: " + m_pMapDown.getInterpolatingMap().toString());
        System.out.println("pMapUp: " + m_pMapUp.getInterpolatingMap().toString());
      }
      if ((getSetpoint().getAsDouble() - m_armRotate.getAngle()) > 0) {
        getController().setP(m_pMapUp.getInterpolatedValue(m_armRotate.getAngle()));
        getController().setI(pidConstantsConfig.getDouble("ArmRotate.Ki"));
        getController().setD(pidConstantsConfig.getDouble("ArmRotate.Kd"));
      } else {
        getController().setP(m_pMapDown.getInterpolatedValue(m_armRotate.getAngle()));
        getController().setI(pidConstantsConfig.getDouble("ArmRotate.Ki"));
        getController().setD(pidConstantsConfig.getDouble("ArmRotate.Kd"));
      }
      getController().setMinOutputToMove(pidConstantsConfig.getDouble("ArmRotate.minOutputToMove"));
      getController().setTolerance(pidConstantsConfig.getDouble("ArmRotate.tolerance"));
      getController().setFeedforward(m_feedForward);
      if (m_useSmartDashboard) {
        setSetpoint(() -> SmartDashboard.getNumber("Rotate PID Arm Angle Setpoint", 300));
      }

      Trace.getInstance().logCommandInfo(this, "Rotate Arm to: " + getSetpoint().getAsDouble());
      if (!BillClimberSingleton.getInstance().getClimberEnabled() || m_rotateWhileClimb) {
        m_armRotate.disengageArmBrake();
      }
    }

    @Override
    public void execute() {
      if (BillClimberSingleton.getInstance().getClimberEnabled() && !m_rotateWhileClimb) {
        return;
      }
      if (!m_needToEnd && isOnTarget() && m_engagePneumaticBrake) {
        m_armRotate.engageArmBrake();
        return;
      } else if (m_engagePneumaticBrake && isOnTarget()) {
        m_armRotate.engageArmBrake();
        m_count++;
      } else if ((getSetpoint().getAsDouble() - m_armRotate.getAngle()) < 0) {
        // going up
        getController().setP(m_pMapUp.getInterpolatedValue(m_armRotate.getAngle()));
        m_feedForward.setConstant(m_kMapUp.getInterpolatedValue(m_armRotate.getAngle()));
      } else if ((getSetpoint().getAsDouble() - m_armRotate.getAngle()) > 0) {
        // going down
        getController().setP(m_pMapDown.getInterpolatedValue(m_armRotate.getAngle()));
        m_feedForward.setConstant(m_kMapDown.getInterpolatedValue(m_armRotate.getAngle()));
      }
      super.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      super.end(interrupted);
      Trace.getInstance().logCommandInfo(this, "Ending Angle: " + m_armRotate.getAngle());
      if (m_engagePneumaticBrake) {
        m_armRotate.engageArmBrake();
      }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      if (m_needToEnd && isOnTarget() && m_count >= 10) {
        return true;
      }
      return false;
    }

    public boolean isOnTarget() {
      return getController().atSetpoint();
    }

    private class RotateFeedForward implements FeedForward {
      private double m_constant = 0;

      public RotateFeedForward() {

      }

      @Override
      public double calculate() {
        return m_constant;
      }

      public void setConstant(double constant) {
        m_constant = constant;
      }

    }

    private void loadMaps() {
      m_kMapUp = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
          "armKValuesUp");

      m_pMapUp = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
          "armPValuesUp");

      m_kMapDown = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
          "armKValuesDown");

      m_pMapDown = new InterpolatingMap(Config4905.getConfig4905().getArmRotateConfig(),
          "armPValuesDown");
    }
  }
}
