/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.pidcontroller;

/** @formatter:off
 * creates a PID controller that determines when the controller is on target by
 * taking a number of consecutive samples. when the number of consecutive
 * samples that are onTarget (within the tolerance of the setPoint) is equal to
 * or greater than the number of consecutive samples that are on target for
 * isFinished to be true, the controller will return onTarget is true. the default
 * number of consecutive samples on target is set in the static final member. to
 * override this, simply use
 * {@link #setNumberOfConsecutiveSamplesOnTargetForIsFinishedTrue(int) setNumbConsecSamples}
 * @formatter:on
 */
public class PIDController4905SampleStop extends PIDController4905 {
  private int m_currentNumberOfConsecutiveSamplesOnTarget = 0;
  private static final int s_defaultNumberOfConsecutiveSamplesOnTargetForIsFinishedTrue = 8;
  private int m_numberOfConsecutiveSamplesOnTargetForIsFinishedTrue = s_defaultNumberOfConsecutiveSamplesOnTargetForIsFinishedTrue;

  public PIDController4905SampleStop(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove, int numberOfConsecutiveSamplesOnTargetForIsFinishedTrue,
      FeedForward feedForward) {
    super(controllerName, Kp, Ki, Kd, minOutputToMove, feedForward);
    m_numberOfConsecutiveSamplesOnTargetForIsFinishedTrue = numberOfConsecutiveSamplesOnTargetForIsFinishedTrue;
  }

  public PIDController4905SampleStop(String controllerName) {
    this(controllerName, 0, 0, 0, 0, s_defaultNumberOfConsecutiveSamplesOnTargetForIsFinishedTrue,
        () -> 0);
  }

  public boolean atSetpoint() {
    if (Math.abs(getPositionError()) < getPositionTolerance()) {
      m_currentNumberOfConsecutiveSamplesOnTarget++;
    } else {
      m_currentNumberOfConsecutiveSamplesOnTarget = 0;
    }
    return m_currentNumberOfConsecutiveSamplesOnTarget >= m_numberOfConsecutiveSamplesOnTargetForIsFinishedTrue;
  }

  @Override
  public void reset() {
    m_currentNumberOfConsecutiveSamplesOnTarget = 0;
    super.reset();
  }

  public void setNumberOfConsecutiveSamplesOnTargetForIsFinishedTrue(int value) {
    m_numberOfConsecutiveSamplesOnTargetForIsFinishedTrue = value;
  }
}
