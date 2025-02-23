// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import java.util.EnumMap;

import frc.robot.Config4905;

/** Add your docs here. */
public class SBSDArmSetpoints {
  public enum ArmSetpoints {
    CLIMBER_POSITION, CORAL_LOAD, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4
  }

  private static EnumMap<ArmSetpoints, ArmAndEndEffectorSetpoints> m_setpointsMap = new EnumMap<>(
      ArmSetpoints.class);
  private static SBSDArmSetpoints m_instance = new SBSDArmSetpoints();
  private static boolean m_hasLoadedConfig = false;

  private SBSDArmSetpoints() {
    setArmAndEndEffectorSetpoints(false);
  }

  private static class ArmAndEndEffectorSetpoints {
    private double armAngleInDeg = 0;
    private double endEffectorAngleInDeg = 0;

    public ArmAndEndEffectorSetpoints(ArmSetpoints armSetpoint, boolean fromConfig) {
      if (!fromConfig) {
        switch (armSetpoint) {
        case CLIMBER_POSITION:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        case CORAL_LOAD:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        case LEVEL_1:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        case LEVEL_2:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        case LEVEL_3:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        case LEVEL_4:
          armAngleInDeg = 0;
          endEffectorAngleInDeg = 0;
          break;

        default:
          throw new RuntimeException("Invalid setpoint: " + armSetpoint);
        }
      } else {
        switch (armSetpoint) {
        case CLIMBER_POSITION:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig()
              .getDouble("climberPosition");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("climberPosition");
          break;

        case CORAL_LOAD:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("coralLoad");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("coralLoad");
          break;

        case LEVEL_1:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_1");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("level_1");
          break;

        case LEVEL_2:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_2");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("level_2");
          break;

        case LEVEL_3:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_3");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("level_3");
          break;

        case LEVEL_4:
          armAngleInDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_4");
          endEffectorAngleInDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
              .getDouble("level_4");
          break;

        default:
          throw new RuntimeException("Invalid setpoint: " + armSetpoint);
        }
      }
    }
  }

  private static void setArmAndEndEffectorSetpoints(boolean fromConfig) {
    m_setpointsMap.put(ArmSetpoints.CLIMBER_POSITION,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.CLIMBER_POSITION, fromConfig));
    m_setpointsMap.put(ArmSetpoints.CORAL_LOAD,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.CORAL_LOAD, fromConfig));
    m_setpointsMap.put(ArmSetpoints.LEVEL_1,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.LEVEL_1, fromConfig));
    m_setpointsMap.put(ArmSetpoints.LEVEL_2,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.LEVEL_2, fromConfig));
    m_setpointsMap.put(ArmSetpoints.LEVEL_3,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.LEVEL_3, fromConfig));
    m_setpointsMap.put(ArmSetpoints.LEVEL_4,
        new ArmAndEndEffectorSetpoints(ArmSetpoints.LEVEL_4, fromConfig));
  }

  public static void setUpSetpointsFromConfig() {
    if (!m_hasLoadedConfig) {
      m_hasLoadedConfig = true;
      setArmAndEndEffectorSetpoints(true);
    }
  }

  public double getArmAngleInDeg(ArmSetpoints level) {
    return m_setpointsMap.get(level).armAngleInDeg;
  }

  public double getEndEffectorAngleInDeg(ArmSetpoints level) {
    return m_setpointsMap.get(level).endEffectorAngleInDeg;
  }

  public interface ArmSetpointsSupplier {
    public SBSDArmSetpoints.ArmSetpoints getAsArmSetpoints();

  }

  public static SBSDArmSetpoints getInstance() {
    return m_instance;
  }
}
