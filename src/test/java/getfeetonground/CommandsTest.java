package getfeetonground;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.sensors.ballfeedersensor.BallFeederSensorBase;
import frc.robot.subsystems.feeder.FeederBase;

public class CommandsTest {
    
    @Test
    public void defaultFeederCommand() throws Exception{

        FeederBase feeder = mock(FeederBase.class);
        BallFeederSensorBase ballFeederSensorBase = mock(BallFeederSensorBase.class);
        when(ballFeederSensorBase.getNumberOfPowerCellsInFeeder()).thenReturn(2);
        Timer timer = mock(Timer.class);

        DefaultFeederCommand defaultFeederCommand = new DefaultFeederCommand(feeder, ballFeederSensorBase, timer);

        assertEquals(2, defaultFeederCommand.callAndReturnSomething());
        System.out.println("Didn't blow up!");

    }

}



