package frc.robot.utils;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;

public class CANCoderUtil {
  public enum CCUsage {
    kAll, kSensorDataOnly, kFaultsOnly, kMinimal
  }

  public static void setCANCoderBusUsage(CANCoder canCoder, CCUsage usage) {
    if (usage == CCUsage.kAll) {
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 10);
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 10);
    } else if (usage == CCUsage.kSensorDataOnly) {
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 10);
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 100);
    } else if (usage == CCUsage.kFaultsOnly) {
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 100);
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 10);
    } else if (usage == CCUsage.kMinimal) {
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, 100);
      canCoder.setStatusFramePeriod(CANCoderStatusFrame.VbatAndFaults, 100);
    }
  }
}
