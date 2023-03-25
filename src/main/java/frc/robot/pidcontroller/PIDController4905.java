package frc.robot.pidcontroller;

import edu.wpi.first.math.MathUtil;
import frc.robot.rewrittenWPIclasses.PIDControllerProposed;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class PIDController4905 extends PIDControllerProposed {
  private double m_minOutputToMove;
  private String m_controllerName;
  private double m_maxOutput = 1.0;
  private FeedForward m_feedForward;

  public PIDController4905(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove, FeedForward feedForward) {
    super(Kp, Ki, Kd);
    m_controllerName = controllerName;
    m_minOutputToMove = Math.abs(minOutputToMove);
    m_feedForward = feedForward;
  }

  public PIDController4905(String controllerName, double Kp, double Ki, double Kd,
      double minOutputToMove) {
    this(controllerName, Kp, Ki, Kd, minOutputToMove, () -> 0);
  }

  public PIDController4905(String controllerName) {
    this(controllerName, 0, 0, 0, 0, () -> 0);
  }

  @Override
  public double calculate(double measurement) {
    double preCalculationOutput = super.calculate(measurement);
    double feedForward = m_feedForward.calculate();
    double output = preCalculationOutput + feedForward;
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
        new TracePair<Double>("FeedForward", feedForward),
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

  public void setFeedforward(FeedForward feedForward) {
    m_feedForward = feedForward;
  }
}