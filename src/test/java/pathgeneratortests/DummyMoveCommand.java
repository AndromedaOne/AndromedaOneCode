package pathgeneratortests;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DummyMoveCommand extends CommandBase{
    private double m_distance;
    private double m_angle; 
    public static final double TOLERANCE = 0.01;
    
    public DummyMoveCommand(double distance, double angle) {
        m_distance = distance;
        m_angle = angle;
    }

    @Override
    public boolean equals(Object obj) {
        DummyMoveCommand dummyMoveCommand;
        try{
            dummyMoveCommand = (DummyMoveCommand) obj;
            return Math.abs(m_distance - dummyMoveCommand.m_distance) <= TOLERANCE && Math.abs(m_angle - dummyMoveCommand.m_angle) <= TOLERANCE;
        } catch(ClassCastException c) {
            return super.equals(obj);
        }
    }

    public String toString() {
        return "Move distance: " + m_distance + " at angle: " + m_angle;
    }

}
