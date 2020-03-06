package frc.robot.sensors.gyro;

public class MockNavXGyroSensor extends NavXGyroSensor {

    @Override
    public double getZAngle() {
        return 0;
    }

    @Override
    public double getXAngle() {
        return 0;
    }

    @Override
    public double getYAngle() {
        return 0;
    }

    @Override
    public double getCompassHeading() {
        return 0;
    }

    @Override
    public void updateSmartDashboardReadings() {
    }

}