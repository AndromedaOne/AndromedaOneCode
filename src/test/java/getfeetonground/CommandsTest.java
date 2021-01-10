package getfeetonground;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class CommandsTest {
  protected FeederBase m_feeder;
  protected BallFeederSensorBase m_ballFeederSensorBase;
  protected Timer m_timer;
  protected DefaultFeederCommand m_defaultFeederCommand;
  protected ShooterBase shooterBase;

  @BeforeEach
  public void setUpFeederSensor() {
    m_feeder = mock(FeederBase.class);
    m_ballFeederSensorBase = mock(BallFeederSensorBase.class);
    when(m_ballFeederSensorBase.getNumberOfPowerCellsInFeeder()).thenReturn(2);
    m_timer = mock(Timer.class);
    shooterBase = mock(ShooterBase.class);
    m_defaultFeederCommand = new DefaultFeederCommand(m_feeder, m_ballFeederSensorBase, m_timer, shooterBase);
    m_defaultFeederCommand.initialize();
  }

  @Test
  public void defaultFeederCommand() throws Exception {

    assertEquals(2, m_defaultFeederCommand.callAndReturnSomething());

  }

  @Test
  public void shooterHoodClosed() {
      verify(shooterBase).closeShooterHood();
  }

  @Test
  public void emptyFeederToOneBallDroppedIn() {
    // Stage 1 and stage 2 of feeder should be running until the ball reaches the first part of stage 2
    setBallFeederSensors(new boolean[]{false, false, false, false, false, false, false, false});
    for(int x=0; x < 3; x++) {
      m_defaultFeederCommand.execute();
    }
    setBallFeederSensors(new boolean[]{true, false, false, false, false, false, false, false});
    
  }

  public void setBallFeederSensors(boolean[] sensorReadings) {
    when(m_ballFeederSensorBase.isThereBall()).thenReturn(sensorReadings);
  }

}
