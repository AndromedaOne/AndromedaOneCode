package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.utils.InterpolatingMap;

public class RunShooterWheelVelocity extends PIDCommand4905 {
  private SimpleMotorFeedforward m_feedForward;
  private static double m_computedFeedForward = 0;
  private ShooterBase m_shooter;
  private double m_target = 0;
  private static Config m_pidConfig;
  private static Config m_shooterConfig;
  private final double kControllerScale;
  private static double manuelShooterAdjustment = 0;
  private double scaledManualShooterAdjustment;
  private static boolean resettingManualShooterAdjustment = false;
  private double m_initialSetpoint;

  public static void increaseManuelShooterAdjustment(double amountToIncrease) {
    manuelShooterAdjustment += amountToIncrease;
  }

  public static void resetManuelShooterAdjustment() {
    resettingManualShooterAdjustment = true;
  }

  /**
   * @param shooter
   * @param m_subsystemController Requires a controller to allow the subsystem
   *                              driver to tune the PID setpoint via the
   *                              controller
   * @param setpoint
   */
  public RunShooterWheelVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(createPIDController(),
        // Measurement
        shooter::getShooterWheelVelocity,
        // Setpoint
        0,
        // Output
        output -> {
          shooter.setShooterWheelPower(output + m_computedFeedForward);
        });

    getController().setTolerance(m_pidConfig.getDouble("runshooterwheelvelocity.tolerance"));

    kControllerScale = Config4905.getConfig4905().getCommandConstantsConfig()
        .getDouble("RunShooterWheelVelocity.shooterwheeljoystickscale");
    m_shooter = shooter;
    m_target = setpoint;
    m_setpoint = this::getSetpoint;
    m_initialSetpoint = setpoint;
  }

  @Override
  public void initialize() {
    super.initialize();
    m_feedForward = createFeedForward();
    getController().setP(m_pidConfig.getDouble("runshooterwheelvelocity.p"));
    getController().setI(m_pidConfig.getDouble("runshooterwheelvelocity.i"));
    getController().setD(m_pidConfig.getDouble("runshooterwheelvelocity.d"));
    System.out.println(
        " - Shooter Setpoint: " + m_target + "\nShooter P = " + m_pidConfig.getDouble("runshooterwheelvelocity.p"));
  }

  @Override
  public void execute() {
    // This adjusts the setpoint while the PID is running to allow the
    // Subsystems driver to tune the rpm on the fly
    if (resettingManualShooterAdjustment) {
      m_target = m_initialSetpoint;
      manuelShooterAdjustment = 0;
      resettingManualShooterAdjustment = false;
    }
    scaledManualShooterAdjustment = manuelShooterAdjustment * kControllerScale;
    m_target += scaledManualShooterAdjustment;
    double maxRPM = 4900;
    double minRPM = 0;
    if (m_target > maxRPM) {
      m_target = maxRPM;
    } else if (m_target < minRPM) {
      m_target = minRPM;
    }
    m_shooter.setShooterPIDIsReady(getController().atSetpoint());
    m_computedFeedForward = m_feedForward.calculate(m_target);
    super.execute();
    SmartDashboard.putNumber("Shooter Wheel Velocity Setpoint", m_target);
    SmartDashboard.putNumber("Manual Shooter Adjustment", manuelShooterAdjustment);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupt) {
    m_shooter.setShooterWheelPower(0);
  }

  private static PIDController4905 createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterwheelvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterwheelvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterwheelvelocity.d");

    return new PIDController4905("ShooterWheelPID", kp, ki, kd, 0);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = 0;
    InterpolatingMap kMap = new InterpolatingMap(Config4905.getConfig4905().getCommandConstantsConfig(),
        "shooterTargetRPMAndKValues");
    double kv = kMap.getInterpolatedValue(m_target);

    return new SimpleMotorFeedforward(ks, kv);
  }

  public double getSetpoint() {
    return m_target;
  }

  public static double getManualShooterAdjustment() {
    return manuelShooterAdjustment;
  }
}