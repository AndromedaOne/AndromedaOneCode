package frc.robot.groupcommands.parallelgroup;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class ShooterParallelSetShooterVelocity extends ParallelCommandGroup {

  /**
   * This should only be run on a while held Because there is no isFinished
   * 
   * @param shooter
   * @param seriesRPM
   * @param shooterRPM
   */
  public ShooterParallelSetShooterVelocity(ShooterBase shooter, double seriesRPM, double shooterRPM) {

    addCommands(new RunShooterWheelVelocity(shooter, shooterRPM), new RunShooterSeriesVelocity(shooter, seriesRPM));

    addRequirements(shooter);
  }

  public ShooterParallelSetShooterVelocity(ShooterBase shooter, double seriesRPM, DoubleSupplier setpointSupplier) { 
    addCommands(new RunShooterWheelVelocity(shooter, setpointSupplier), new RunShooterSeriesVelocity(shooter, seriesRPM));

    addRequirements(shooter);
  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }

}