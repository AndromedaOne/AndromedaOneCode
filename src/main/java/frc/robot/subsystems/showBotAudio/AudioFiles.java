// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotAudio;

// audio files on the PI
public enum AudioFiles {
  AirRaidSiren("airRaidSiren.wav"), CannonShot("cannonShot.wav"), CrazyTrain("CrazyTrain.wav"),
  DeadMansParty("DeadMansParty.wav"), DiveAlert("diveAlert.wav"),
  DroidPrepareToFire("droidPrepareToFire.wav"), DroidProblem("droidProblem.wav"),
  HellsBells("HellsBells.wav"), Kaboom("KaBoom.wav"), ManhattanProject("ManhattanProject.wav"),
  MeepMeep("meepMeep.wav"), Paranoimia("Paranoimia.wav"), SolarAnthem("SolarAnthem.wav"),
  TheBigMoney("TheBigMoney.wav"), TruckHorn("truckHorn.wav");

  private final String m_fileName;

  AudioFiles(String fileName) {
    m_fileName = fileName;
  }

  String getFileName() {
    return (m_fileName);
  }
}
