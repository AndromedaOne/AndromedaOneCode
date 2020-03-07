package frc.robot.sensors.limelightcamera;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;

public class LimeLEDRegistrar {
    List<Command> commandsThatUseLimeLEDs;
    private static LimeLEDRegistrar instance;

    private LimeLEDRegistrar() {

    }

    public static synchronized LimeLEDRegistrar getInstance() {
        if(instance == null) {
            instance = new LimeLEDRegistrar();
        }
        return instance;
    }

    public synchronized void addCommands(Command... commands) {
        for(Command command : commands) {
            commandsThatUseLimeLEDs.add(command);
        }
    }

    public synchronized boolean isLimeLedCommandRunning() {
        return commandsThatUseLimeLEDs.stream().anyMatch(c -> c.isScheduled());
    }

}