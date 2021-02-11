/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.telemetries.Trace;

/**
 * Do NOT add any static variables to this class, or any initialization at all.
 * Unless you know what you are doing, do not modify this file except to change
 * the parameter class to the startRobot call.
 */
public final class Main {
  private Main() {
  }

  /**
   * Main initialization function. Do not perform any initialization here.
   *
   * <p>
   * If you change your main robot class, change the parameter type.
   */
  public static void main(String... args) {
    System.out.println("OS is " + System.getProperty("os.name"));
    if (Files.exists(Paths.get("/home/lvusers"))) {
      System.out.println("home/lvusers exists...");
    } else {
      System.out.println("home/lvusers does not exist");
    }
    String rootDir = System.getProperty("user.dir");
    System.out.println("current dir = " + rootDir);
    File currentDir = new File(rootDir + "/src/main/deploy");
    String[] files = currentDir.list();
    for (String file : files) {
      System.out.println(file);
    }
    String tmpDir = System.getProperty("java.io.tmpdir");
    System.out.println("TEMP dir: " + tmpDir);
    Trace.getInstance();
    RobotBase.startRobot(Robot::getInstance);
  }
}
