package getfeetonground;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class CommandsTest {
  static FeederBase m_feeder;
  static BallFeederSensorBase m_ballFeederSensorBase;
  static Timer m_timer;
  static DefaultFeederCommand m_defaultFeederCommand;
  static ShooterBase shooterBase;

  @BeforeAll
  public static void setUpFeederSensor() {
    m_feeder = mock(FeederBase.class);
    m_ballFeederSensorBase = mock(BallFeederSensorBase.class);
    when(m_ballFeederSensorBase.getNumberOfPowerCellsInFeeder()).thenReturn(2);
    m_timer = mock(Timer.class);
    shooterBase = mock(ShooterBase.class);
    m_defaultFeederCommand = new DefaultFeederCommand(m_feeder, m_ballFeederSensorBase, m_timer, shooterBase);
  }

  @BeforeEach
  public void intializeFeederCommand() {
    m_defaultFeederCommand.initialize();
  }

  @Test
  public void defaultFeederCommand() throws Exception {

    assertEquals(2, m_defaultFeederCommand.callAndReturnSomething());

  }

  /*@Test
  public void shooterHoodClosed() {
      verify(shooterBase).closeShooterHood();
  }*/

}
