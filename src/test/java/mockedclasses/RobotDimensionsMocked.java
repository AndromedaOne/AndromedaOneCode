package mockedclasses;

import static org.mockito.Mockito.*;

import frc.robot.utils.RobotDimensions;

public class RobotDimensionsMocked {
    private static RobotDimensions mockedRobotDimensions;

    public static double MOCKED_ROBOT_LENGTH = 10.0;
    public static double MOCKED_ROBOT_WIDTH = 7.0;
    public static double MOCKED_ROBOT_BUMPER_DEPTH = 1.0;
    
    public static RobotDimensions getMockedRobotDimensions() {
        mockedRobotDimensions = mock(RobotDimensions.class);
        
        when(mockedRobotDimensions.getLength()).thenReturn(MOCKED_ROBOT_LENGTH);
        when(mockedRobotDimensions.getWidth()).thenReturn(MOCKED_ROBOT_WIDTH);
        when(mockedRobotDimensions.getBumperThickness()).thenReturn(MOCKED_ROBOT_BUMPER_DEPTH);

        return mockedRobotDimensions;
    }
}