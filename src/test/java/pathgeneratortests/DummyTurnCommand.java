package pathgeneratortests;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DummyTurnCommand extends CommandBase{
    private double m_angle; 
    public static final double TOLERANCE = DummyMoveCommand.TOLERANCE;
    
    public DummyTurnCommand(double angle) {
        m_angle = angle;
    }

    @Override
    public boolean equals(Object obj) {
        DummyTurnCommand dummyTurnCommand;
        try{
            dummyTurnCommand = (DummyTurnCommand) obj;
            return Math.abs(m_angle - dummyTurnCommand.m_angle) <= TOLERANCE;
        }catch(ClassCastException c) {
            return super.equals(obj);
        }
    }

    public String toString() {
        return "Turn to angle: " + m_angle;
    }

}
