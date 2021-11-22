package frc.robot.utils;

import java.util.Iterator;
import java.util.List;

import com.typesafe.config.Config;

import frc.robot.lib.interpolate.InterpolatingDouble;
import frc.robot.lib.interpolate.InterpolatingTreeMap;

public class InterpolatingMap {
  private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> m_interpolatingMap = new InterpolatingTreeMap<>();
  private List<Double> m_interpolatingMapList;

  public InterpolatingMap(Config config, String nameOfMap) {
    if (!config.hasPath(nameOfMap)) {
      m_interpolatingMap.put(new InterpolatingDouble(1.0), new InterpolatingDouble(1.0));
      return;
    }

    m_interpolatingMapList = config.getDoubleList(nameOfMap);

    Iterator<Double> it = m_interpolatingMapList.iterator();

    while (it.hasNext()) {
      double nextValue = it.next();
      if (!it.hasNext()) {
        System.err.println("WARN: Uneven Number of " + nameOfMap + " Map Values in Config");
        break;
      }
      m_interpolatingMap.put(new InterpolatingDouble(nextValue), new InterpolatingDouble(it.next()));
    }
  }

  public InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> getInterpolatingMap() {
    return m_interpolatingMap;
  }

  /**
   * This method returns the interpolated value based off the interpolating map
   * inside the config
   * 
   * @param lookupValue Lookup value in the map
   * @return interpolated value
   */
  public double getInterpolatedValue(double lookupValue) {
    return m_interpolatingMap.getInterpolated(new InterpolatingDouble(lookupValue)).value;
  }
}