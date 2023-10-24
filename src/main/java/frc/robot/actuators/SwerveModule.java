package main.java.frc.robot.actuators;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;


public class SwerveModule {
  public int moduleNumber;
  private Rotation2d lastAngle;
  private Rotation2d angleOffset;
  private SparkMaxController angleMotor;
  private SparkMaxController driveMotor;

  private RelativeEncoder driverEncoder;
  private RelativeEncoder intergratedAngleEncoder;
  private CANCoder angleEncoder;
  private final SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

  public SwerveModule(int moduleNumber) {
    this.moduleNumber = moduleNumber;
    lastAngle = getState().angle;
  }
  
}
