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
        new TracePair("pError x10", super.getPError() * 10),
        new TracePair("iError x10", super.getIError() * 10),
        new TracePair("dError * 10", super.getDError() * 10),
        new TracePair("FeedForward * 10", feedForward), new TracePair("Output", output * 10),
        new TracePair("preCalculationOutput * 10", preCalculationOutput * 10),
        new TracePair("Measurement", measurement), new TracePair("Setpoint", super.getSetpoint()));
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