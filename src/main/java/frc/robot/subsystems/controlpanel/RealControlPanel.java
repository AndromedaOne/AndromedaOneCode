/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.controlpanel;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.TalonSRXController;

public class RealControlPanel extends ControlPanelBase {

  public DoubleSolenoid4905 extendSolenoid;
  public TalonSRXController controlPanelMotor;
  public ColorSensorV3 colorSensor;

  private boolean isOnboard;

  private Color blueTarget;
  private Color redTarget;
  private Color greenTarget;
  private Color yellowTarget;

  private ColorMatch matcher;

  
  /**
   * Creates a new RealControlPanel.
   */
  public RealControlPanel(String configString) {
    Config sensorConf = Config4905.getConfig4905().getSensorConfig().atPath("sensors." + configString);
    extendSolenoid = new DoubleSolenoid4905(sensorConf, null);
    controlPanelMotor = new TalonSRXController(sensorConf, null);
    isOnboard = sensorConf.getBoolean("isOnboard");
    
    if (isOnboard) {
        colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    } else {
        colorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }

    matcher = new ColorMatch();
    // Intialize Colors
    Config colorConf = sensorConf.atPath("colors");
    redTarget = new Color(colorConf.getInt("redTarget.red"), colorConf.getInt("redTarget.green"), colorConf.getInt("redTarget.blue"));
    blueTarget = new Color(colorConf.getInt("blueTarget.red"), colorConf.getInt("blueTarget.green"), colorConf.getInt("blueTarget.blue"));
    greenTarget = new Color(colorConf.getInt("greenTarget.red"), colorConf.getInt("greenTarget.green"), colorConf.getInt("greenTarget.blue"));
    yellowTarget = new Color(colorConf.getInt("yellowTarget.red"), colorConf.getInt("yellowTarget.green"), colorConf.getInt("yellowTarget.blue"));

    matcher.addColorMatch(redTarget);
    matcher.addColorMatch(blueTarget);
    matcher.addColorMatch(greenTarget);
    matcher.addColorMatch(yellowTarget);


  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

@Override
public void extendSystem() {
	// TODO Auto-generated method stub
	extendSolenoid.extendPiston();
}

@Override
public void retractSystem() {
	// TODO Auto-generated method stub
	extendSolenoid.retractPiston();
}

@Override
public void rotateOneTime() {
	// TODO Auto-generated method stub
  Color detectedColor = colorSensor.getColor();
  ColorMatchResult initialColor = matcher.matchClosestColor(detectedColor);

  
}

@Override
public void rotateToColor() {
	// TODO Auto-generated method stub
	
}
}
