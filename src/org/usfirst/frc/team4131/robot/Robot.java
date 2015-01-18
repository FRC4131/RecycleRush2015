package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private RobotDrive drive = new RobotDrive(0, 1);
	private Joystick cont = new Joystick(0);
	private JoystickButton buttonReset = new JoystickButton(cont, 2);//B
	private Encoder encLeft = new Encoder(0, 1), encRight = new Encoder(2, 3);
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight()*Math.exp(-9);
	private final double SONAR_OFFSET = sonar.getOffset()*Math.exp(-9);
	private Accelerometer accel = new BuiltInAccelerometer();
	private ADXL345_SPI accel2 = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k16G);
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
					Timer.delay(0.005);
				}
			}
		}.start();
	}
	public void autonomous(){
		while(isAutonomous() && isEnabled()){
			if(getSonar(true)>24) drive.drive(0.1, 0);
			else drive.drive(0, 0);
			SmartDashboard.putNumber("Sonar (in)", getSonar(true));
			SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
			Timer.delay(0.005);
		}
	}
	public void operatorControl(){
		boolean driveType = cont.getAxisCount()==6;//True means Xbox One controller, with variable-input triggers on axes 2 and 3.
													//False means Logitech controller with buttons instead of triggers.
		while(isOperatorControl() && isEnabled()){
			if(driveType) drive.arcadeDrive(cont.getRawAxis(1), cont.getRawAxis(4));//Y axis of left stick, X of right
			else drive.arcadeDrive(cont.getRawAxis(1), cont.getRawAxis(2));
			if(buttonReset.get()){
				encLeft.reset();
				encRight.reset();
			}
			Timer.delay(0.005);
		}
	}
	private double getSonar(boolean inches){
		return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 *(inches ? 1 : 2.54);
	}
	public void test(){
		while(isTest() && isEnabled()){
			drive.arcadeDrive(-0.5, 0);
			Timer.delay(0.005);
		}
	}
}
