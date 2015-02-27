package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous{
	private Robot robot;
	private DriveBase drive;
	public Autonomous(Robot robot, DriveBase drive){this.robot = robot; this.drive = drive;}
	public void run(){
		Thread thread = new Thread(){
			public void run(){
				try{
					runRaw();
				}catch(InterruptedException ex){};
			}
		};
		thread.start();
		while(robot.isAutonomous() && robot.isEnabled()) Timer.delay(0.005);
		thread.interrupt();
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
	private void runRaw() throws InterruptedException{
		drive.strafe(130);
	}
}
