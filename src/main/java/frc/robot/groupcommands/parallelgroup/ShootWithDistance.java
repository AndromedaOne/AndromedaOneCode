package frc.robot.groupcommands.parallelgroup;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShootWithDistance extends ParallelCommandGroup {

  /**
   * This command will shoot all balls inside the feeder with a distance to shoot
   * to
   * 
   * @param shooter
   * @param feeder
   * @param distance in inches
   */
  public ShootWithDistance(ShooterBase shooter, FeederBase feeder, double distance) {
    double rpm = shooter.getShooterMap().getInterpolatedRPM(distance);
    addCommands(new ShootWithRPM(shooter, feeder, rpm));
  }

  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public boolean isFinished() {
    return super.isFinished();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    System.out.println("In the end for shoot with distance!!!");
    // CommandScheduler.getInstance().schedule(new DefaultFeederCommand());
  }
}