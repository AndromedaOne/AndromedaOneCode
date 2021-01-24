package frc.robot.utils;

// A robot section for waypoints to move is a part of the robot that you want waypoints to end at. For example you may want the front bumper of the robot to move to the waypoint 3,3 so you would create a WaypointWithRobotSectionToMove and pass in the front bumper section as the RobotSectionsForWaypointsToMove.
public enum RobotSectionsForWaypointsToMove {
  FRONTOFFRAME, BACKOFFRAME, FRONTOFBUMPER, BACKOFBUMPER, CENTER
}
