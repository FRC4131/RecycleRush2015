
package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot{
	private OI oi = new OI(0);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, 1, 2, 3, 4);
	public Robot(){
		
	}
	@Override
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), true);
			if(oi.getButton(Button.B)) sensors.reset();
			Timer.delay(0.005);
		}
	}
	@Override
	public void test(){
		while(isTest() && isEnabled()){
			drive.drive(0, 0.2, 0, false);
			Timer.delay(0.005);
		}
	}
}
