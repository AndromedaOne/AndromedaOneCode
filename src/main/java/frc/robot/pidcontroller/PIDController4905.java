package frc.robot.pidcontroller;

public class PIDController4905 extends PIDControllerProposed {
  private double m_minOutputToMove;

  public PIDController4905(double Kp, double Ki, double Kd, double minOutputToMove) {
    super(Kp, Ki, Kd);
    m_minOutputToMove = minOutputToMove;
  }

  @Override
  public double calculate(double measurement) {
    double output = super.calculate(measurement);
    output = output * (1 - m_minOutputToMove) + m_minOutputToMove;
    return output;
  }
}