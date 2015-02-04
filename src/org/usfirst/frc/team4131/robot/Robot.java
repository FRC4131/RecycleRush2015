package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot{
	protected static DriveBase drive = new DriveBase(new PIDTalon(1, 0, 1, false), new PIDTalon(2, 2, 3, false),
			new PIDTalon(3, 4, 5, true), new PIDTalon(4, 6, 7, true));//Front left, rear left, front right, rear right
	protected static Sensors sensors = new Sensors(SPI.Port.kOnboardCS0, 0, 1, 2);
	protected static OI oi = new OI(0, 1);
	public Robot(){
//		encFrontLeft.setDistancePerPulse(CIRCUMFERENCE/250);//distance in inches per tick divided inches per second
//		encFrontRight.setDistancePerPulse(-CIRCUMFERENCE/250);//returns time
//		encRearLeft.setDistancePerPulse(CIRCUMFERENCE/360);
//		encRearRight.setDistancePerPulse(-CIRCUMFERENCE/360);
	}
	public void autonomous(){
		
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			if(oi.getButton('x')){
				double angle = -sensors.getAngle() % 360;
				if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
				if(Math.abs(angle)<1) drive.drive(0, 0, 0);//Stop
				else if(Math.abs(angle)<15) drive.drive(0, 0, Math.copySign(0.1, angle));//Move slowly
				else drive.drive(0, 0, Math.copySign(0.3, angle));//Move quickly
			}
			double x = oi.getStrafe() - (oi.getStrafe() % 0.1);//Round to nearest 0.1
			double y = oi.getSpeed() - (oi.getSpeed() % 0.1);
			double turn = oi.getRotation() - (oi.getRotation() % 0.1);
			drive.drive(x, y, turn);
			PIDTalon.equalize();
			Timer.delay(0.005);
		}
	}
	public void test(){
		while(isTest() && isEnabled()){
			drive.drive(-0.2, 0);
			Timer.delay(0.005);
		}
	}
}
