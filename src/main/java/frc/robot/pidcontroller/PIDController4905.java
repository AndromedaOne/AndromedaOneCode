package frc.robot.pidcontroller;

import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class PIDController4905 extends PIDControllerProposed {
  private double m_minOutputToMove;
  private double m_minOutputToMoveAbs;
  private String m_controllerName;
  private double m_maxOutput = Double.POSITIVE_INFINITY;

  public PIDController4905(String controllerName, double Kp, double Ki, double Kd, double minOutputToMove) {
    super(Kp, Ki, Kd);
    m_controllerName = controllerName;
    m_minOutputToMove = minOutputToMove;
  }

  @Override
  public double calculate(double measurement) {
    m_minOutputToMoveAbs = Math.abs(m_minOutputToMove);
    double preCalculationOutput = super.calculate(measurement);
    double output = preCalculationOutput;
    if (preCalculationOutput < 0) {
      output = preCalculationOutput * (1 - m_minOutputToMoveAbs) - m_minOutputToMoveAbs;
      if (output < -m_maxOutput) {
        output = -m_maxOutput;
      }
    } else if (preCalculationOutput > 0) {
      output = preCalculationOutput * (1 - m_minOutputToMoveAbs) + m_minOutputToMoveAbs;
      if (output > m_maxOutput) {
        output = m_maxOutput;
      }
    }
    Trace.getInstance().addTrace(true, m_controllerName, new TracePair<Double>("pError", super.getPError()),
        new TracePair<Double>("iError", super.getIError()), new TracePair<Double>("dError", super.getDError()),
        new TracePair<Double>("Output", output * 5000), new TracePair<Double>("preCalculationOutput", preCalculationOutput),
        new TracePair<Double>("Measurement", measurement), new TracePair<Double>("Setpoint", super.getSetpoint()),
        new TracePair<Double>("Velocity Error", super.getVelocityError()));
    return output;
  }

  public void setMinOutputToMove(double minOutputToMove) {
    m_minOutputToMove = minOutputToMove;
  }

  public void setMaxOutput(double maxOutput) {
    m_maxOutput = maxOutput;
  }
}