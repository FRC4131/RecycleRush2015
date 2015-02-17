package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private OI oi = new OI(0, 1);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 8, 9, 6, 7});//LF, LB, RF, RB
	private Conveyor conveyor = new Conveyor(0, 1);
	private Pneumatics pneumatics = new Pneumatics(6);
	public Robot(){new Thread(this).start();}
	
	/*public void autonomous(){
		Thread thread = new Thread(){public void run(){try{
			drive.unlock();
			switch(DriverStation.getInstance().getLocation()){
				case(1):
					//Left side of the field
					break;
				case(2):
					//Middle of the field
					break;
				case(3):
					//Right side of the field
					break;
			}
			move(75);
			turn(-90);
			move(49);
			turn(90);
			move(53);
			turn(-58);
			move(62);
			turn(-72);
			move(51);
			turn(50);
			move(43);
			turn(-90);
			move(42);
			turn(-85);
			move(61);
			drive.stop();
		}catch(InterruptedException ex){}catch(Throwable ex){
			String string = ex.getMessage() + "\r\n";
			for(StackTraceElement element : ex.getStackTrace()) string = string.concat(element.toString());
			DriverStation.reportError(string, false);
		}}};
		thread.start();
		while(isEnabled() && isAutonomous()) Timer.delay(0.005);
		if(thread.isAlive()) thread.interrupt();
		drive.stop();
	}*/
	@Override
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			if(oi.getButton(true, Button.LEFT_BUMPER)) drive.unlock(); else if(oi.getPOV()>-1) drive.lock(oi.getPOV());
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), false);
			conveyor.set(oi.getConveyorSpeed());
			SmartDashboard.putString("Conveyor", oi.getConveyorSpeed() + "-" + conveyor.get());
			SmartDashboard.putNumber("Controller X", oi.getX());
			SmartDashboard.putNumber("Controller Y", oi.getY());
			SmartDashboard.putNumber("Controller Rotation", oi.getRotation());
			Timer.delay(0.005);
		}
	}
	@Override
	public void autonomous(){
		while(isAutonomous() && isEnabled()){
			drive.unlock();
			Button[] buttons = new Button[]{Button.Y, Button.X, Button.B, Button.A};
			for(int i=0;i<buttons.length;i++) drive.getMotor(i).set(oi.getButton(true, buttons[i]) ? -0.2 : 0);
			Timer.delay(0.005);
			pneumatics.loop();
		}
	}
	@Override
	public void run(){
		while(true){
			SmartDashboard.putNumber("AccX", sensors.getAcceleration('x'));
			SmartDashboard.putNumber("AccY", sensors.getAcceleration('y'));
			SmartDashboard.putNumber("AccZ", sensors.getAcceleration('z'));
			SmartDashboard.putNumber("Sonar", sensors.sonarIn());
			SmartDashboard.putNumber("Gyro", sensors.gyroAngle());
			SmartDashboard.putNumber("Temp (C)", sensors.tempC());
			SmartDashboard.putNumber("Temp (F)", sensors.tempF());
			for(int i=0;i<drive.getMotors().length;i++){
				SmartDashboard.putNumber("Encoder V " + i, drive.getDistance(i));
				SmartDashboard.putNumber("Encoder R " + i, drive.getRate(i));
				SmartDashboard.putString("Encoder D " + i, drive.isStopped(i) ? "Stopped" : drive.getDirection(i) ? "Forward" : "Backward");
				SmartDashboard.putNumber("Current " + i, drive.getMotor(i).getOutputCurrent());
			}
			SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
			if(oi.getButton(true, Button.B)){
				sensors.reset();
				drive.reset();
			}
			Timer.delay(0.005);
		}
	}
	private void move(double inches) throws InterruptedException{
		SmartDashboard.putString("Phase", "Move " + inches);
		double start = drive.getDistance(1);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(1) - start)) > 15){
			drive.drive(0, Math.copySign(0.3, inches), 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private void turn(int angle) throws InterruptedException{
		SmartDashboard.putString("Phase", "Turn " + angle);
		drive.lock(angle - (int)sensors.gyroAngle());
		while(Math.abs(angle - sensors.gyroAngle()) > 1){
			drive.drive(0, 0, 0, false);//Let the rotation lock have its way
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
		drive.unlock();
	} 
}
