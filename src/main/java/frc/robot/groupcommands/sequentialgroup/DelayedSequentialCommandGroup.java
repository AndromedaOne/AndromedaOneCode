package frc.robot.groupcommands.sequentialgroup;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class DelayedSequentialCommandGroup extends SequentialCommandGroup {
    public DelayedSequentialCommandGroup(final Command... commands) {
        addCommands(new SpecialWaitCommand());
        addCommands(commands);
    }

    private class SpecialWaitCommand extends CommandBase {
        double m_autoDelay;
        public void initialize() {
            m_autoDelay = SmartDashboard.getNumber("Auto Delay", 0);
        }

        public void execute() {
            new WaitCommand(m_autoDelay);
        }

        public boolean isFinished() {
            return true;
        }

        public void end() {
            
        }
    }
}