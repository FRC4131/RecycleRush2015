package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private static final Point[] STARTING = {new Point(-22, 247), new Point(-26, 165), new Point(-17, 93)};
	private double x, y;
	private OI oi = new OI(0);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
	public Robot(){
		new Thread(this).start();
	}
	@Override
	public void autonomous(){
		Thread thread = new Thread(){public void run(){try{
			int location = DriverStation.getInstance().getLocation() - 1;
			x=STARTING[location].x;
			y=STARTING[location].y;
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
	}
	@Override
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			double x = oi.getX(), y = oi.getY(), rotation = oi.getRotation();
			if(oi.getPOV()>-1){
				int current = (int)constrain(sensors.gyroAngle());
				int target = oi.getPOV();
				int diff = target - current;
				if(Math.abs(diff) > 180) diff = 360 - diff;
				if(Math.abs(diff) > 45) rotation = Math.copySign(0.4, diff);
				else if(Math.abs(diff) > 5) rotation = Math.copySign(0.3, diff);
				else if(Math.abs(diff) > 1) rotation = Math.copySign(0.1, diff);
				else rotation = 0;
			}
			drive.drive(x, y, rotation, true);
			Timer.delay(0.005);
		}
	}
	@Override
	public void test(){
		while(isTest() && isEnabled()){
			drive.stop();
			Timer.delay(0.005);
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
			SmartDashboard.putNumber("Gyro 2", constrain((int)sensors.gyroAngle()));
			SmartDashboard.putNumber("Temp (C)", sensors.tempC());
			SmartDashboard.putNumber("Temp (F)", sensors.tempF());
			for(int i=0;i<drive.getMotors().length;i++){
				SmartDashboard.putNumber("Encoder V " + i, drive.getDistance(i));
				SmartDashboard.putNumber("Encoder R " + i, drive.getRate(i));
				SmartDashboard.putString("Encoder D " + i, drive.isStopped(i) ? "Stopped" : drive.getDirection(i) ? "Forward" : "Backward");
				SmartDashboard.putNumber("Current " + i, drive.getMotor(i).getOutputCurrent());
			}
			SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
			if(oi.getButton(Button.B)){
				sensors.reset();
				drive.reset();
			}
			Timer.delay(0.005);
		}
	}
	private void move(double inches) throws InterruptedException{
		double start = drive.getDistance(1);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(1) - start)) > 15){
			drive.drive(0, Math.copySign(0.3, inches), 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private void turn(double angle) throws InterruptedException{
		double start = sensors.gyroAngle();
		double diff;
		while(Math.abs(diff = angle - (sensors.gyroAngle() - start)) > 1){
			drive.drive(0, 0, Math.copySign(0.3, diff), false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private double constrain(double raw){return raw - 360*Math.floor(raw/360);}//Constrain to [0, 360)
	public static class Point{
		public final int x, y;
		public Point(int x, int y){this.x = x; this.y = y;}
	}
}
