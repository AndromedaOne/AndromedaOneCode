/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.pidcontroller;

/**
 * Add your docs here.
 */
public class PIDController4905SampleStop extends PIDController4905 {
  private int m_counter = 0;
  private final int numberOfSamplesOnTarget = 8;

  public PIDController4905SampleStop(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove, FeedForward feedForward) {
    super(controllerName, Kp, Ki, Kd, minOutputToMove, feedForward);
  }

  public PIDController4905SampleStop(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove) {
    super(controllerName, Kp, Ki, Kd, minOutputToMove);
    m_counter = 0;
  }

  public PIDController4905SampleStop(String controllerName) {
    this(controllerName, 0, 0, 0, 0);
  }

  public boolean atSetpoint() {
    if (Math.abs(getPositionError()) < getPositionTolerance()) {
      m_counter++;
    } else {
      m_counter = 0;
    }
    return m_counter >= numberOfSamplesOnTarget;
  }

  @Override
  public void reset() {
    m_counter = 0;
    super.reset();
  }
}
