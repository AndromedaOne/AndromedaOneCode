package pathgeneratortests;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DummyTurnCommand extends CommandBase{
    private double m_angle; 
    
    public DummyTurnCommand(double angle) {
        m_angle = angle;
    }

    public double getAngle() {
        return m_angle;
    }

    public String toString() {
        return "Turn to angle: " + m_angle;
    }

}
