package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private RobotDrive drive = new RobotDrive(0, 1);
	private Joystick controller = new Joystick(0);
	private Encoder encLeft = new Encoder(0, 1), encRight = new Encoder(2, 3);
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight()*Math.exp(-9);
	private final double SONAR_OFFSET = sonar.getOffset()*Math.exp(-9);
	private Accelerometer accel = new BuiltInAccelerometer();
	private ADXL345_SPI accel2 = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k16G);
	private Gyro gyro = new Gyro(1);
	private AnalogInput temp = new AnalogInput(2);
	public Robot(){
		drive.setInvertedMotor(MotorType.kRearLeft, true);
		drive.setInvertedMotor(MotorType.kRearRight, false);
		new Thread(){
			@Override
			public void run(){
				while(true){
					SmartDashboard.putNumber("Sonar (in)", getSonar(true));
					SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
					SmartDashboard.putString("Left Encoder", encLeft.getStopped() ? "Stopped" : encLeft.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putString("Right Encoder", encRight.getStopped() ? "Stopped" : encRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putNumber("Left Encoder Value", encLeft.getDistance());
					SmartDashboard.putNumber("Right Encoder Value", encRight.getDistance());
					SmartDashboard.putNumber("Accel X", accel.getX());
					SmartDashboard.putNumber("Accel Y", accel.getY());
					SmartDashboard.putNumber("Accel Z", accel.getZ());
					accel2.updateTable();
					SmartDashboard.putNumber("Accel 2 X", accel2.getX());
					SmartDashboard.putNumber("Accel 2 Y", accel2.getY());
					SmartDashboard.putNumber("Accel 2 Z", accel2.getZ());
					SmartDashboard.putNumber("Gyro", gyro.getAngle());
					SmartDashboard.putNumber("Temperature (C)", getTemp(true));
					SmartDashboard.putNumber("Temperature (F)", getTemp(false));
					if(controller.getRawButton(controller.getAxisCount()==6 ? 2 : 3)){//B button on either controller
						encLeft.reset();
						encRight.reset();
						gyro.reset();
					}
					Timer.delay(0.005);
				}
			}
		}.start();
	}
	public void autonomous(){
		while(isAutonomous() && isEnabled()){
			/*if(getSonar(true)>24) drive.drive(0.1, 0);
			else drive.drive(0, 0);
			SmartDashboard.putNumber("Sonar (in)", getSonar(true));
			SmartDashboard.putNumber("Sonar (cm)", getSonar(false));*/
			
			double angle = -gyro.getAngle() % 360;
			if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
			if(Math.abs(angle)<1){
				drive.arcadeDrive(0, 0, false);//Stop
			}else if(Math.abs(angle)<15){
				drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
			}else{
				drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
			}
			Timer.delay(0.005);
		}
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			if(controller.getAxisCount()==6) drive.arcadeDrive(controller.getRawAxis(1), controller.getRawAxis(4));//Y axis of left stick, X of right
			else drive.arcadeDrive(controller.getRawAxis(1), controller.getRawAxis(2));
			Timer.delay(0.005);
		}
	}
	private double getSonar(boolean inches){
		return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 *(inches ? 1 : 2.54);
	}
	private double getTemp(boolean celsius){
		double c = ((temp.getVoltage()-2.5)*9 + 25);
		if(celsius) return c;
		return 1.8*c + 32;
	}
	public void test(){
		while(isTest() && isEnabled()){
			drive.arcadeDrive(-0.5, 0);
			Timer.delay(0.005);
		}
	}
}
