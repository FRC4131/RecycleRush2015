package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private RobotDrive drive = new RobotDrive(0, 1);
	private Joystick cont1 = new Joystick(0);//Game controllers
	private Encoder encLeft = new Encoder(0, 1), encRight = new Encoder(2, 3);
	private AnalogInput sonar = new AnalogInput(0);
	private Accelerometer accel = new BuiltInAccelerometer();
	public Robot(){}
	public void autonomous(){
		
	}
	public void operatorControl(){
		boolean driveType = cont1.getAxisCount()==6;//True means Xbox One controller, with variable-input triggers on axes 2 and 3.
													//False means Logitech controller with buttons instead of triggers.
		while(isOperatorControl() && isEnabled()){
			if(driveType){
				drive.arcadeDrive((1-cont1.getRawAxis(3)) * cont1.getRawAxis(1), -cont1.getRawAxis(4));//Y axis of left stick, X of right
			}else{
				drive.arcadeDrive(cont1.getRawAxis(1), -cont1.getRawAxis(3));
			}
			SmartDashboard.putString("Left Encoder", encLeft.getStopped() ? "Stopped" : !encLeft.getDirection() ? "Forward" : "Backward");
			SmartDashboard.putString("Right Encoder", encRight.getStopped() ? "Stopped" : encRight.getDirection() ? "Forward" : "Backward");
			SmartDashboard.putNumber("Left Encoder Value", -encLeft.getDistance());
			SmartDashboard.putNumber("Right Encoder Value", encRight.getDistance());
			SmartDashboard.putNumber("Sonar (cm)", sonar.getVoltage());//0.009766
			SmartDashboard.putNumber("Accel X", accel.getX());
			SmartDashboard.putNumber("Accel Y", accel.getY());
			SmartDashboard.putNumber("Accel Z", accel.getZ());
			Timer.delay(0.005);
		}
	}
	public void test(){
		while(isTest() && isEnabled()){
			drive.arcadeDrive((1-cont1.getRawAxis(3)) * cont1.getRawAxis(1), -cont1.getRawAxis(4));
			Timer.delay(0.005);
		}
	}
}
