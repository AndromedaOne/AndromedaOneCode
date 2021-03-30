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
    private boolean m_adjustedVelocities;

    

    public RamseteMaxVelocityAdjustment(double maxVelocity, DifferentialDriveKinematics kinematics) {
        m_accumulatedTimeAdjustment = 0;
        m_maxVelocity = maxVelocity;
        m_kinematics = kinematics;
        m_previousCommandedSpeed = 0;
        m_previousAdjustedSpeed = 0;
        m_previousTime = 0;
        m_adjustedVelocities = false;
    }

    public double getAdjustedTime(double currentTime) {
        // need to adjust the time here
        setAccumulatedTimeAdjustment(currentTime);
        m_previousTime = currentTime;
        return currentTime - m_accumulatedTimeAdjustment;
    }

    public void update(double leftVelocity, double rightVelocity) {
        updateVelocities(leftVelocity, rightVelocity);
        setPreviousVelocities(leftVelocity, rightVelocity);
    }

    private void setAccumulatedTimeAdjustment(double currentTime) {
        if (m_adjustedVelocities){
            double deltaT = currentTime - m_previousTime;
            double adjustedDistance = deltaT * m_previousAdjustedSpeed;
            if(Math.abs(m_previousCommandedSpeed) > 1.0e-5){
                m_accumulatedTimeAdjustment += deltaT - (adjustedDistance / m_previousCommandedSpeed);
            }
        }
    }

    private void setPreviousVelocities(double originalLeftVelocity, double originalRightVelocity) {
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
            m_adjustedVelocities = false;
            return;
        }
        m_adjustedVelocities = true;

        if(Math.abs(leftVelocity) > Math.abs(rightVelocity)) {
            double delta = Math.abs(leftVelocity) - Math.abs(rightVelocity);
            m_leftVelocity = m_maxVelocity * Math.signum(leftVelocity);
            if(rightVelocity > Math.abs(m_maxVelocity)){
                m_rightVelocity = getVelocityadjustedByDeltaOrToZero(m_maxVelocity * Math.signum(rightVelocity), delta);
            }else{
                m_rightVelocity = getVelocityadjustedByDeltaOrToZero(rightVelocity, delta);
            }
        }else {
            double delta = Math.abs(rightVelocity) - Math.abs(leftVelocity);
            m_rightVelocity = m_maxVelocity * Math.signum(rightVelocity);
            if(rightVelocity > Math.abs(m_maxVelocity)){
                m_leftVelocity = getVelocityadjustedByDeltaOrToZero(m_maxVelocity * Math.signum(leftVelocity), delta);
            }else{
                m_leftVelocity = getVelocityadjustedByDeltaOrToZero(leftVelocity, delta);
            }
        }
    }

    private double getVelocityadjustedByDeltaOrToZero(double velocity, double delta) {
        if(delta > Math.abs(velocity)) {
            return 0;
        }else{
            return (Math.abs(velocity) - delta) * Math.signum(velocity);
        }
    }

    public double getLeftVelocity() {
        return m_leftVelocity;
    }

    public double getRightVelocity() {
        return m_rightVelocity;
    }
    
}
