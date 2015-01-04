package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private RobotDrive drive = new RobotDrive(0, 1);
	private Joystick cont1 = new Joystick(0), cont2 = new Joystick(1);//Game controllers
	private JoystickButton a = new JoystickButton(cont2, 1);
	public Robot(){}
	public void autonomous(){
		
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			drive.arcadeDrive(cont1.getRawAxis(5), cont1.getRawAxis(4));//Y and X axes for right thumbstick
			if(a.get()){
				SmartDashboard.putString("Button", "Pressed");
			}else{
				SmartDashboard.putString("Button", "Released");
			}
			Timer.delay(0.005);
		}
	}
	public void test(){
		while(isTest() && isEnabled()){
			
			Timer.delay(0.005);
		}
	}
}
