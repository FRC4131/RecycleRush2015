package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous{
	private double start;
	private final String[] COMMANDS = {"m66"};
	private Robot robot;
	private Sensors sensors;
	private DriveBase drive;
	public Autonomous(Robot robot, Sensors sensors, DriveBase drive){this.robot = robot; this.sensors = sensors; this.drive = drive;}
	public void run(){
		for(String command : COMMANDS){
			char cmd = command.charAt(0);
			double val;
			try{val = Double.valueOf(command.substring(1));}catch(NumberFormatException ex){continue;}
			boolean firstTime = true;
			while(cmd=='m' ? move(firstTime, val) :
				cmd=='t' ? turnBy(val) :
				cmd=='T' ? turnTo(val) :
				cmd=='s' ? strafe(firstTime, val) : false){
				firstTime = false;
				if(!robot.isAutonomous() || !robot.isEnabled()) return;
				Timer.delay(0.005);
			}
		}
	}
	/*private void runRaw() throws InterruptedException{
		drive.unlock();
		drive.strafe(75);
		drive.move(49);
		drive.strafe(44);
		drive.turnTo(24);//arctan(23/52)
		drive.move(Math.sqrt(23*23 + 52*52));
		drive.turnTo(-39);//arctan(-39/48)
		drive.move(Math.sqrt(39*39 + 48*48));
		drive.turnTo(0);
		drive.move(43);
		drive.strafe(-42);
		drive.move(-61);
		drive.stop();
	}*/
	private boolean move(boolean firstTime, double inches){
		SmartDashboard.putString("Phase", "Move " + inches);
		if(firstTime) start = drive.getDistance(1);
		double diff = inches - (drive.getDistance(1) - start);
		drive.drive(0, Math.copySign(0.6, inches), 0, false);
		SmartDashboard.putNumber("Diff", diff);
		return Math.abs(diff) > 1;
	}
	private boolean turnBy(double angle){
		return turnTo(angle + sensors.gyroAngle());
	}
	private boolean turnTo(double angle){
		SmartDashboard.putString("Phase", "Turn to " + angle);
		drive.lock(angle);
		drive.drive(0, 0, 0, false);//Let the rotation lock have its way
		return Math.abs(angle - sensors.gyroAngle()) > 1;
	}
	private boolean strafe(boolean firstTime, double inches){
		SmartDashboard.putString("Phase", "Strafe " + inches);
		drive.lock(sensors.gyroAngle());//Because our center of gravity is behind the center of our bot's volume, the back wheels
		//Do more of the work than the front. While strafing, because the front wheels are against the back, the robot will turn, because
		//the back wheels have more influence over the robot's direction. By locking the direction, it counteracts this turn.
//		unlock();
		inches *= 3;
		if(firstTime) start = drive.getDistance(3);
		double diff = inches - (drive.getDistance(3) - start);
		drive.drive(Math.copySign(1, inches), 0, Math.copySign(0.1, inches), false);
		SmartDashboard.putNumber("Diff", diff);
		drive.unlock();
		return inches - (drive.getDistance(3) - start) < 1;
	}
}
