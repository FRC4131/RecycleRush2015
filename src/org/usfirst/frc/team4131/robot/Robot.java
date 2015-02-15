package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.OI.Button;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private OI oi = new OI(0);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
	public Robot(){
		new Thread(this).start();
	}
	@Override
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			double x = oi.getX(), y = oi.getY(), rotation = oi.getRotation();
			if(oi.getPOV()>-1){
				int current = (int)sensors.gyroAngle();
				while(current>360) current -= 360;
				while(current<0) current += 360;
				int target = oi.getPOV();
				double diff = target - current;
				if(Math.abs(diff) > 90) rotation = Math.copySign(0.5, diff);
				else if(Math.abs(diff) > 45) rotation = Math.copySign(0.3, diff);
				else if(Math.abs(diff) > 15) rotation = Math.copySign(0.2, diff);
				else if(Math.abs(diff) > 1) rotation = Math.copySign(0.1, diff);
				else rotation = 0;
			}
			drive.drive(x, y, rotation, true);
			if(oi.getButton(Button.B)) sensors.reset();
			Timer.delay(0.005);
		}
	}
	@Override
	public void autonomous(){
		
	}
	@Override
	public void test(){
		while(isTest() && isEnabled()){
			Button[] buttons = new Button[]{Button.X, Button.A, Button.Y, Button.B};
			for(int i=0;i<buttons.length;i++) drive.getMotor(i).set(oi.getButton(buttons[i]) ? -0.2 : 0);
			Timer.delay(0.005);
		}
	}
	@Override
	public void run(){
		while(true){
			SmartDashboard.putNumber("AccX", sensors.getAcceleration('x'));
			SmartDashboard.putNumber("AccY", sensors.getAcceleration('y'));
			SmartDashboard.putNumber("AccZ", sensors.getAcceleration('z'));
			SmartDashboard.putNumber("Sonar (in)", sensors.sonarIn());
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
			Timer.delay(0.005);
		}
	}
}
