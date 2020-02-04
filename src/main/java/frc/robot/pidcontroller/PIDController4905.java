package frc.robot.pidcontroller;

public class PIDController4905 extends PIDControllerProposed {
  private double m_minOutputToMove;
  private double m_minOutputToMoveAbs;

  public PIDController4905(double Kp, double Ki, double Kd, double minOutputToMove) {
    super(Kp, Ki, Kd);
    m_minOutputToMove = minOutputToMove;
    m_minOutputToMoveAbs = Math.abs(minOutputToMove);
  }

  @Override
  public double calculate(double measurement) {
    double output = super.calculate(measurement);
    if (m_minOutputToMove < 0) {
      output = output * (1 - m_minOutputToMoveAbs) - m_minOutputToMoveAbs;
    } else if (m_minOutputToMove > 0) {
      output = output * (1 - m_minOutputToMoveAbs) + m_minOutputToMoveAbs;
    }
    return output;
  }
}