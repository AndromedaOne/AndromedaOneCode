package frc.robot.actuators;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.lib.config.Constants;
import frc.robot.lib.config.SwerveModuleConstants;
import frc.robot.utils.CANCoderUtil;
import frc.robot.utils.CANCoderUtil.CCUsage;

public class SwerveModule {
  public int moduleNumber;
  private Rotation2d lastAngle;
  private Rotation2d angleOffset;
  private CANSparkMax angleMotor;
  private CANSparkMax driveMotor;

  private RelativeEncoder driverEncoder;
  private RelativeEncoder intergratedAngleEncoder;
  private CANCoder angleEncoder;
  private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(
      Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

  public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants) {
    this.moduleNumber = moduleNumber;
    angleOffset = moduleConstants.angleOffset;

    angleEncoder = new CANCoder(moduleConstants.canCoderID);
    configAngleEncoder();

    angleMotor = new CANSparkMax(moduleConstants.driveMotorID, MotorType.kBrushless);
    /* Left off here */

    lastAngle = getState().angle;
  }

  private void configAngleEncoder() {
    angleEncoder.configFactoryDefault();
    CANCoderUtil.setCANCoderBusUsage(angleEncoder, CCUsage.kMinimal);
    angleEncoder.configAllSettings(null);
  }

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(intergratedAngleEncoder.getPosition());
  }

  public SwerveModuleState getState() {
    return new SwerveModuleState(driverEncoder.getVelocity(), getAngle());
  }

}
