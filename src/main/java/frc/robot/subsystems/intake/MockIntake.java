package frc.robot.subsystems.intake;

public class MockIntake extends IntakeBase {
  private double m_previousSpeed = 0;

  public void runIntake(double speed) {
    if (speed != m_previousSpeed) {
      System.out.println("Running intake at speed :" + speed);
      m_previousSpeed = speed;
    }
  }

  public void stopIntake() {
    System.out.println("Stopping intake.");
    m_previousSpeed = 0;
  }

  @Override
  public void deployIntake() {
    // TODO Auto-generated method stub
    System.out.println("deploy intake");
  }

  @Override
  public void retractIntake() {
    // TODO Auto-generated method stub
    System.out.println("retract intake");
  }
}