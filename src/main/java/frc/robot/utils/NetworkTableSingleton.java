// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.DoubleTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;
import edu.wpi.first.networktables.StringTopic;

/** Add your docs here. */
public class NetworkTableSingleton {
  // using member variables because it saves on memory i think
  private NetworkTableInstance m_instance;
  private NetworkTableSingleton m_networkTable;
  private NetworkTable m_tempTable;
  private DoubleTopic m_dTempTopic;
  private DoublePublisher m_dTempPub;
  private DoubleSubscriber m_dTempSub;
  private StringTopic m_sTempTopic;
  private StringPublisher m_sTempPub;
  private StringSubscriber m_sTempSub;

  private NetworkTableSingleton() {
    m_instance = NetworkTableInstance.getDefault();
  }

  public NetworkTableSingleton getInstance() {
    if (m_networkTable == null) {
      m_networkTable = new NetworkTableSingleton();
    }
    return m_networkTable;
  }

  // creating tables/topics is not necessary

  // add: BOOLEANS, COMMANDS, ints, enums

  public void setDoubleTopic(String topicName, String tableName, double value) {
    m_tempTable = m_instance.getTable(tableName);
    m_dTempTopic = m_tempTable.getDoubleTopic(topicName);
    m_dTempPub = m_dTempTopic.publish();
    m_dTempPub.set(value);
  }

  public void setDoubleTopic(String topicName, double value) {
    setDoubleTopic(topicName, "default", value);
  }

  // error check the topic has been created
  public double getDoubleTopic(String topicName, String tableName) {
    m_tempTable = m_instance.getTable(tableName);
    m_dTempTopic = m_tempTable.getDoubleTopic(topicName);
    m_dTempSub = m_dTempTopic.subscribe(0);
    return m_dTempSub.get();
  }

  public double getDoubleTopic(String topicName) {
    return getDoubleTopic(topicName, "default");
  }

  public void setStringTopic(String topicName, String tableName, String value) {
    m_tempTable = m_instance.getTable(tableName);
    m_sTempTopic = m_tempTable.getStringTopic(topicName);
    m_sTempPub = m_sTempTopic.publish();
    m_sTempPub.set(value);
  }

  public void setStringTopic(String topicName, String value) {
    setStringTopic(topicName, "default", value);
  }

  public String getStringTopic(String topicName, String tableName) {
    m_tempTable = m_instance.getTable(tableName);
    m_sTempTopic = m_tempTable.getStringTopic(topicName);
    m_sTempSub = m_sTempTopic.subscribe("");
    return m_sTempSub.get();
  }

  public String getStringTopic(String topicName) {
    return getStringTopic(topicName, "default");
  }

  // need to figure out how to make getEnumTopic
  public void setEnumTopic(String topicName, String tableName, Enum value) {
    setStringTopic(topicName, tableName, value.toString());
  }

  // doesnt work because generic publishers cannot do anything
  /*
   * public void setGenericTopic(String topicName, String tableName) { m_tempTable
   * = m_instance.getTable(tableName); Topic gTopic =
   * m_tempTable.getTopic(topicName); Publisher pub =
   * gTopic.genericPublish("double");
   * 
   * }
   */
}
