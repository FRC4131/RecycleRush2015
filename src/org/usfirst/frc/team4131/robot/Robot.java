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
		SmartDashboard.putString("Status", "Running");
		Thread thread = new Thread(){
			public void run(){try{
				/*move(-36);
				turn(-90);
				move(-36);
				turn(90);
				move(-24);
				turn(-90);
				move(-60);
				turn(-65);
				move(-54);*/
				int index = 0;
				go(36, 0, index++);
				go(-36, 0, index++);
				go(24, 0, index++);
				go(-60, 0, index++);
				go(52, -30.5, index++);
				drive.stop();
			}catch(InterruptedException ex){drive.stop(); SmartDashboard.putString("Status", "Interrupted");}}//If autonomous ends, InterruptedException is thrown.
		};
		thread.start();
		while(isEnabled() && isAutonomous()) Timer.delay(0.005);
		if(thread.isAlive()) thread.interrupt();
		drive.stop();
	}
	public void operatorControl(){
		drive.stop();//Prevent the last command of autonomous from setting the motors at a non-zero value.
		while(isOperatorControl() && isEnabled()){
			double x = oi.getX();//Round to nearest 0.05
			double y = oi.getY();
			double rotation = oi.getRotation();
			if(oi.getPOV() > -1){//+Pad pressed
				double angle = sensors.gyroAngle();
				while(angle<0) angle+=360;
				double angDif = oi.getPOV()-sensors.gyroAngle();
				if(Math.abs(angDif)<1) rotation = 0;
				else if(Math.abs(angDif)<15) rotation = Math.copySign(0.1, -angDif);
				else rotation = Math.copySign(0.3, -angDif);
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
	private void move(double inches) throws InterruptedException{
		double start = drive.getMotor(0).getDistance();
		inches = Math.copySign(Math.abs(inches) - 6, inches);
		while(Math.abs(drive.getMotor(0).getDistance() - start) < Math.abs(inches)){
			drive.drive(0, Math.copySign(0.2, inches), 0, false);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private void turn(int degrees) throws InterruptedException{
		int start = (int)sensors.gyroAngle();
		degrees = (int)Math.copySign(Math.abs(degrees) -5, degrees);
		double diff = degrees;
		while(Math.abs(diff) > 1){
			diff = degrees - (sensors.gyroAngle() - start);
			SmartDashboard.putNumber("Diff", diff);
			drive.drive(0, 0, Math.copySign(0.2, diff), false);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	//Coordinates are in inches, and relative to the robot's position.
	private void go(double x, double y, int index) throws InterruptedException{
		double angle = Math.toDegrees(Math.atan(x/y));
		double distance = -Math.sqrt(x*x + y*y);
		SmartDashboard.putNumber("Angle " + index, angle);
		SmartDashboard.putNumber("Distance " + index, distance);
		turn((int)angle);
		move(distance);
	}
}
