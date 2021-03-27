package frc.robot.pidcontroller;

import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class RamseteMaxVelocityAdjustment {

    private double m_leftVelocity;
    private double m_rightVelocity;
    private double m_accumulatedTimeAdjustment;
    private double m_maxVelocity;
    private final DifferentialDriveKinematics m_kinematics;
    private double m_previousCommandedSpeed;
    private double m_previousAdjustedSpeed;
    private double m_previousTime;


    public RamseteMaxVelocityAdjustment(double maxVelocity, DifferentialDriveKinematics kinematics) {
        m_accumulatedTimeAdjustment = 0;
        m_maxVelocity = maxVelocity;
        m_kinematics = kinematics;
        m_previousCommandedSpeed = 0;
        m_previousAdjustedSpeed = 0;
        m_previousTime = 0;
    }

    public double getAdjustedTime(double currentTime) {
        return currentTime - m_accumulatedTimeAdjustment;
    }

    public void update(double leftVelocity, double rightVelocity, double currentTime) {
        updateVelocities(leftVelocity, rightVelocity);
        updateAccumulatedTimeAdjustment(leftVelocity, rightVelocity, currentTime);
    }

    private void updateAccumulatedTimeAdjustment(double originalLeftVelocity, double originalRightVelocity, double currentTime) {
        setAccumulatedTimeAdjustment(currentTime);
        setPreviousSpeeds(originalLeftVelocity, originalRightVelocity);
        m_previousTime = currentTime;
    }

    private void setAccumulatedTimeAdjustment(double currentTime) {
        double deltaT = currentTime - m_previousTime;
        double adjustedDistance = deltaT * m_previousAdjustedSpeed;
        m_accumulatedTimeAdjustment += deltaT - (adjustedDistance / m_previousCommandedSpeed);
    }

    private void setPreviousSpeeds(double originalLeftVelocity, double originalRightVelocity) {
        DifferentialDriveWheelSpeeds newDifferentialDriveWheelSpeeds = new DifferentialDriveWheelSpeeds(m_leftVelocity, m_rightVelocity);
        ChassisSpeeds newChassisSpeeds = m_kinematics.toChassisSpeeds(newDifferentialDriveWheelSpeeds);
        DifferentialDriveWheelSpeeds originalDifferentialDriveWheelSpeeds = new DifferentialDriveWheelSpeeds(originalLeftVelocity, originalRightVelocity);
        ChassisSpeeds originalChassisSpeeds = m_kinematics.toChassisSpeeds(originalDifferentialDriveWheelSpeeds);
        m_previousCommandedSpeed = originalChassisSpeeds.vxMetersPerSecond;
        m_previousAdjustedSpeed = newChassisSpeeds.vxMetersPerSecond;
    }

    private void updateVelocities(double leftVelocity, double rightVelocity) {
        if(Math.abs(leftVelocity) <= m_maxVelocity && Math.abs(rightVelocity) <= m_maxVelocity){
            m_leftVelocity = leftVelocity;
            m_rightVelocity = rightVelocity;
            return;
        }

        if(Math.abs(leftVelocity) > m_maxVelocity && Math.abs(rightVelocity) > m_maxVelocity){
            adjustVelocitiesWhenBothAreGreaterThanMax(leftVelocity, rightVelocity);
            return;
        }

        if(Math.abs(leftVelocity) > m_maxVelocity) {
            double delta = Math.abs(leftVelocity) - Math.abs(rightVelocity);
            m_leftVelocity = m_maxVelocity * Math.signum(leftVelocity);
            m_rightVelocity = (Math.abs(rightVelocity) - delta) * Math.signum(rightVelocity);
            updateVelocities(m_leftVelocity, m_rightVelocity);
        }else {
            double delta = Math.abs(rightVelocity) - Math.abs(leftVelocity);
            m_rightVelocity = m_maxVelocity * Math.signum(rightVelocity);
            m_leftVelocity = (Math.abs(leftVelocity) - delta) * Math.signum(leftVelocity);
            updateVelocities(m_leftVelocity, m_rightVelocity);
        }
    }

    private void adjustVelocitiesWhenBothAreGreaterThanMax(double leftVelocity, double rightVelocity) {
        if(Math.abs(leftVelocity) > Math.abs(rightVelocity)) {
            m_leftVelocity = m_maxVelocity * Math.signum(leftVelocity);
            double delta = Math.abs(leftVelocity) - Math.abs(rightVelocity);
            m_rightVelocity = (m_maxVelocity - delta) * Math.signum(rightVelocity);
        }else {
            m_rightVelocity = m_maxVelocity * Math.signum(rightVelocity);
            double delta = Math.abs(rightVelocity) - Math.abs(leftVelocity);
            m_leftVelocity = (m_maxVelocity - delta) * Math.signum(leftVelocity);
        }
    }
    
}
