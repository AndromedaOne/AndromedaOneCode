package frc.robot.utils;

import frc.robot.Config4905;
import com.typesafe.config.Config;

public class RobotDimensions {
    private static RobotDimensions instance;
    private double m_length;
    private double m_width;
    private double m_bumperThickness;
    
    private RobotDimensions() {
        Config robotDimensions = Config4905.getConfig4905().getRobotDimensionsConfig();
        m_length = robotDimensions.getDouble("Length");
        m_width = robotDimensions.getDouble("Width");
        m_bumperThickness = robotDimensions.getDouble("BumperThickness");

    }

    public static RobotDimensions getInstance() {
        if(instance == null) {
            instance = new RobotDimensions();
        }
        return instance;
    }

    public double getLength() {
        return m_length;
    }

    public double getWidth() {
        return m_width;
    }

    public double getBumperThickness() {
        return m_bumperThickness;
    }

}
