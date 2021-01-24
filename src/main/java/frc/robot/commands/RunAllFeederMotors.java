package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.feeder.FeederBase;

public class RunAllFeederMotors extends CommandBase{

    private FeederBase m_feederBase;
    
    public RunAllFeederMotors(FeederBase feederBase) {
        m_feederBase = feederBase;

        addRequirements(m_feederBase);
    }

    @Override
    public void execute() {
        m_feederBase.runBothStages(0.3, 0.3);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_feederBase.stopBothStages();
    }
}
