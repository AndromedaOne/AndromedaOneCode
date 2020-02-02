package frc.robot.subsystems.intake;

public class MockIntake extends IntakeBase {
  public void runIntake(double speed) {
    System.out.println("Running intake at speed :" + speed);
  }

  public void stopIntake() {
    System.out.println("Stopping intake.");
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