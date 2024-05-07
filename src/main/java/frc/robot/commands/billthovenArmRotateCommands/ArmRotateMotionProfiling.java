package frc.robot.commands.billthovenArmRotateCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.Config4905;
import frc.robot.commands.billthovenClimberCommands.BillClimberSingleton;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class ArmRotateMotionProfiling extends SequentialCommandGroup4905 {
  public ArmRotateMotionProfiling(BillArmRotateBase armRotate, DoubleSupplier angle,
      boolean needToEnd, boolean useSmartDashboard, boolean engagePneumaticBrake,
      boolean rotateWhileClimb) {
    addCommands(new RotateArmInternalProfiled(armRotate, angle, needToEnd, useSmartDashboard,
        engagePneumaticBrake, rotateWhileClimb));
  }

  public ArmRotateMotionProfiling(BillArmRotateBase armRotate, DoubleSupplier angle,
      boolean needToEnd) {
    this(armRotate, angle, needToEnd, false, true, false);
  }

  public ArmRotateMotionProfiling(BillArmRotateBase armRotate, boolean rotateWhileClimb,
      DoubleSupplier angle, boolean needToEnd) {
    this(armRotate, angle, needToEnd, false, true, rotateWhileClimb);
  }

  public ArmRotateMotionProfiling(BillArmRotateBase armRotate, DoubleSupplier angle,
      boolean needToEnd, boolean engagePneumaticBrake) {
    this(armRotate, angle, needToEnd, false, engagePneumaticBrake, false);
  }

  private class RotateArmInternalProfiled extends ProfiledPIDCommand {
    private BillArmRotateBase m_armRotate;
    private Double m_goal = 0.0;
    private boolean m_needToEnd = false;
    private boolean m_useSmartDashboard = true;
    private InterpolatingMap m_kMap;
    private InterpolatingMap m_pMap;
    private boolean m_engagePneumaticBrake; 
    private boolean m_rotateWhileClimb = false;
    private static Config pidConstantsConfig = Config4905.getConfig4905().getArmRotateConfig();
    private static TrapezoidProfile.Constraints m_constraints = new TrapezoidProfile.Constraints(
        pidConstantsConfig.getDouble("ArmRotate.KmaxVelocity"),
        pidConstantsConfig.getDouble("ArmRotate.KmaxAcceleration"));
    private static ProfiledPIDController m_controller = new ProfiledPIDController(
        pidConstantsConfig.getDouble("ArmRotate.Kp"), pidConstantsConfig.getDouble("ArmRotate.Ki"),
        pidConstantsConfig.getDouble("ArmRotate.Kd"), m_constraints);
    private static ArmFeedforward m_feedForward = new ArmFeedforward(
        pidConstantsConfig.getDouble("ArmRotate.Ks"), pidConstantsConfig.getDouble("ArmRotate.Kg"),
        pidConstantsConfig.getDouble("ArmRotate.Kv"));

    public RotateArmInternalProfiled(BillArmRotateBase armRotate, DoubleSupplier goal,
        boolean needToEnd, boolean useSmartDashboard, boolean engagePneumaticBrake,
        boolean rotateWhileClimb) {

      super(m_controller, armRotate::getAngle, goal, (output, setpoint) -> {
        armRotate.rotate(output);
      }, armRotate.getSubsystemBase());
      m_armRotate = armRotate;
      m_goal = goal.getAsDouble();
      m_needToEnd = needToEnd;
      m_useSmartDashboard = useSmartDashboard;
      m_engagePneumaticBrake = engagePneumaticBrake;
      m_rotateWhileClimb = rotateWhileClimb;
      addRequirements(armRotate.getSubsystemBase());

      if (m_useSmartDashboard) {
        SmartDashboard.putNumber("Rotate Arm P-value", 0);
        SmartDashboard.putNumber("Rotate Arm I-value", 0);
        SmartDashboard.putNumber("Rotate Arm D-value", 0);
        SmartDashboard.putNumber("Rotate Arm S-value", 0);
        SmartDashboard.putNumber("Rotate Arm G-value", 0);
        SmartDashboard.putNumber("Rotate Arm V-value", 0);
        SmartDashboard.putNumber("Rotate Arm Max Velocity-value", 0);
        SmartDashboard.putNumber("Rotate Arm Max Acceleration-value", 0);
        SmartDashboard.putNumber("Rotate Arm Feed Forward", 0);
        SmartDashboard.putNumber("Rotate PID Arm Angle Setpoint", 300);
      }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

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

      getController().setTolerance(pidConstantsConfig.getDouble("ArmRotate.tolerance"));
      if (m_useSmartDashboard) {
        // m_feedForward.setConstant(SmartDashboard.getNumber("Rotate Arm Feed Forward",
        // 0));
        // SetGoal(() -> SmartDashboard.getNumber("Rotate PID Arm Angle Setpoint",
        // 300));
      }

      Trace.getInstance().logCommandInfo(this, "Rotate Arm to: " + m_goal);
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
      } else if (m_engagePneumaticBrake && isOnTarget()) {
        m_armRotate.engageArmBrake();
      } else if (!m_useSmartDashboard) {
        getController().setP(m_pMap.getInterpolatedValue(m_armRotate.getAngle()));
        // m_feedForward.setConstant(m_kMap.getInterpolatedValue(m_armRotate.getAngle()));
        super.execute();
      } else {
        super.execute();
      }
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
      if (m_needToEnd && isOnTarget()) {
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
        return m_armRotate.getAngle() * m_constant;
      }

      public void setConstant(double constant) {
        m_constant = constant;
      }

    }
  }
}
