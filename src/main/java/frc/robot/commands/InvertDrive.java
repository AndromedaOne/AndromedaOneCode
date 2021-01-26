package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.oi.DriveController;

public class InvertDrive extends CommandBase{
    private DriveController m_driveController;
    public InvertDrive(DriveController driveController){
        m_driveController = driveController;
    }
    @Override
    public void initialize(){
        m_driveController.invertJoySticks();
        System.out.println("invertDrive");
    }
    @Override
    public boolean isFinished(){
        return true;
    }
}