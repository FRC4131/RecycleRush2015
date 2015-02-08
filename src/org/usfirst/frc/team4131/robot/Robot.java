package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
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
					if(oi.getButton(Button.B)){
						sensors.reset();
						for(PIDTalon talon : drive.getMotors()){
							talon.reset();
							talon.set(0);
						}
					}
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
		Thread thread = new Thread(){
			@Override
			public void run(){
				move(-36);
				turn(-90);
				move(-36);
				turn(90);
				move(-24);
				turn(-90);
				move(-60);
				turn(-65);
				move(-54);
			}
		};
		thread.start();
		while(isEnabled() && isAutonomous()) Timer.delay(0.005);
		if(thread.isAlive()) thread.stop();
		drive.stop();
	}
	public void operatorControl(){
		drive.stop();//Prevent the last command of autonomous from setting the motors at a non-zero value.
		while(isOperatorControl() && isEnabled()){
			double x = oi.getX();//Round to nearest 0.05
			double y = oi.getY();
			double rotation = oi.getRotation();
			if(oi.getButton(Button.X)){
				double angDif = -sensors.gyroAngle();
				if(angDif<1) rotation = 0;
				else if(angDif<15) rotation = Math.copySign(0.1, angDif);
				else rotation = Math.copySign(0.3, -sensors.gyroAngle() % 360);
			}
			drive.drive(x, y, rotation, true);
//			PIDTalon.equalize();
			Timer.delay(0.005);
		}
	}
	public void test(){
		while(isTest() && isEnabled()){
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), true);
			Timer.delay(0.005);
		}
	}
	private void move(double inches){
		inches = Math.copySign(Math.abs(inches)-9, inches);
		double start = drive.getMotor(0).getDistance();
		while(Math.abs(drive.getMotor(0).getDistance() - start) < Math.abs(inches)){
			drive.drive(0, Math.copySign(0.2, inches), 0, false);
			Timer.delay(0.005);
		}
	}
	private void turn(int degrees){
		degrees = (int)Math.copySign(Math.abs(degrees)-10, degrees);//Sign-independent version of 'degrees-=10'. It usually overshoots.
		double start = sensors.gyroAngle();
		while(Math.abs(sensors.gyroAngle() - start) < Math.abs(degrees)){
			drive.drive(0, 0, Math.copySign(0.2, degrees), false);
			Timer.delay(0.005);
		}
	}
	//Coordinates are in inches, and relative to the robot's position.
	private void go(double x, double y){
		double ratio = y/x;
		drive.drive(0.2, 0.2*ratio, 0, false);
	}
}
