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
  int counter = 0;
  final int numberOfSamplesOnTarget = 4;

  public PIDController4905SampleStop(String controllerName, double Kp, double Ki, double Kd, double minOutputToMove) {
    super(controllerName, Kp, Ki, Kd, minOutputToMove);
    counter = 0;
  }

  public boolean atSetpoint() {
    if (Math.abs(getPositionError()) < getPositionTolerence()) {
      counter++;
    } else {
      counter = 0;
    }
    return counter >= numberOfSamplesOnTarget;
  }

}
