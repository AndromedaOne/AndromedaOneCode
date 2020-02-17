package frc.robot.sensors.limelightcamera;

public class MockLimeLightCamera extends LimeLightCameraBase {

  @Override
  public double horizontalRadiansToTarget() {

    return 0;
  }

  @Override
  public double verticalRadiansToTarget() {

    return 0;
  }

  @Override
  public double distanceToTarget(double targetHeight) {

    return 0;
  }

  @Override
  public double distanceToPowerPort() {

    return 0;
  }

  @Override
  public void setPipeline(int pipelineNumber) {

  }

  @Override
  public void updateSmartDashboardReadings() {

  }

}