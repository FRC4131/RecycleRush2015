package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new PIDTalon(1, 0, 1, 250, true, true), new PIDTalon(2, 2, 3, 360, true, true),
			new PIDTalon(3, 4, 5, 250, true, false), new PIDTalon(4, 6, 7, 305, true, false));
	private OI oi = new OI(0);
	public Robot(){
		new Thread(){
			@Override
			public void run(){
				while(true){
					sensors.run();
					if(oi.getButton(Button.B)){for(PIDTalon talon : drive.getMotors()) talon.reset();}
					for(PIDTalon talon : drive.getMotors()){
						SmartDashboard.putNumber("Enc V " + talon.getDeviceID(), talon.getDistance());
						SmartDashboard.putString("Enc D " + talon.getDeviceID(), talon.isStopped() ? "Stopped" : talon.getDirection() ? "Forward" : "Backward");
					}
					SmartDashboard.putNumber("Battery Voltage", DriverStation.getInstance().getBatteryVoltage());
					Timer.delay(0.005);
				}
			}
		}.start();
	}
	public void autonomous(){
		move(-25);
	}
	private void move(double inches){
		double start = drive.getMotor(0).getDistance();
		while(Math.abs(drive.getMotor(0).getDistance() - start) < Math.abs(inches)){
			drive.drive(0, Math.copySign(0.2, inches), 0);
			Timer.delay(0.005);
		}
		drive.drive(0, 0, 0);
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			/*if(buttonCenter.get()){
				double angle = -sensors.gyroAngle() % 360;
				if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
				
				if(Math.abs(angle)<1) drive.arcadeDrive(0, 0, false);//Stop
				else if(Math.abs(angle)<15) drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
				else drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
			}*/
			double x = oi.getX();//Round to nearest 0.05
			double y = oi.getY();
			double rotation = oi.getStrafe();
			if(oi.getButton(Button.X)){
				double angDif = -sensors.gyroAngle();
				if(angDif<1) rotation = 0;
				else if(angDif<15) rotation = Math.copySign(0.1, angDif);
				else rotation = Math.copySign(0.3, -sensors.gyroAngle() % 360);
			}
			drive.drive(x, y, rotation);
//			PIDTalon.equalize();
			Timer.delay(0.005);
		}
	}
	public void test(){
		int ticks = 0;
		while(isOperatorControl() && isEnabled()){
			drive(-0.2);
			if(ticks % 20 == 0) PIDTalon.equalize();
			ticks++;
			Timer.delay(0.005);
		}
	}
	private void drive(double value){
//		frontLeft.set(fl*13.67 / 22.80);
//		rearLeft.set(rl*17.17 / 22.80);
//		frontRight.set(fr*22.80 / 22.80);
//		rearRight.set(rr*11.25 / 22.80);
		for(PIDTalon talon : drive.getMotors()) talon.set(value);
	}
}
