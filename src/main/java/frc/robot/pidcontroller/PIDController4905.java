package frc.robot.pidcontroller;

import edu.wpi.first.math.MathUtil;
import frc.robot.rewrittenWPIclasses.PIDControllerProposed;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class PIDController4905 extends PIDControllerProposed {
  private double m_minOutputToMove;
  private String m_controllerName;
  private double m_maxOutput = 1.0;

  public PIDController4905(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove) {
    super(Kp, Ki, Kd);
    m_controllerName = controllerName;
    m_minOutputToMove = Math.abs(minOutputToMove);
  }

  @Override
  public double calculate(double measurement) {
    double preCalculationOutput = super.calculate(measurement);
    double output = preCalculationOutput;
    if ((preCalculationOutput < 0) && (Math.abs(preCalculationOutput) < m_minOutputToMove)) {
      output = -m_minOutputToMove;
    } else if ((preCalculationOutput > 0) && (preCalculationOutput < m_minOutputToMove)) {
      output = m_minOutputToMove;
    }
    output = MathUtil.clamp(output, -m_maxOutput, m_maxOutput);
    Trace.getInstance().addTrace(true, m_controllerName,
        new TracePair<Double>("pError", super.getPError()),
        new TracePair<Double>("iError", super.getIError()),
        new TracePair<Double>("dError", super.getDError()),
        new TracePair<Double>("Output", output * 1000),
        new TracePair<Double>("preCalculationOutput", preCalculationOutput * 1000),
        new TracePair<Double>("Measurement", measurement),
        new TracePair<Double>("Setpoint", super.getSetpoint()));
    return output;
  }

  public void setMinOutputToMove(double minOutputToMove) {
    m_minOutputToMove = minOutputToMove;
  }

  public void setMaxOutput(double maxOutput) {
    m_maxOutput = maxOutput;
  }
}