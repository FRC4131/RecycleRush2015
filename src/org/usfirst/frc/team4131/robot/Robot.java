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
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot{
	private double wheelCirc=25.1327;
	private Talon frontLeft = new Talon(0), backLeft = new Talon(1), frontRight = new Talon(2), backRight = new Talon(3);
	private RobotDrive drive = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
	private Joystick controller = new Joystick(0);
	private JoystickButton buttonReset = new JoystickButton(controller, 2), buttonCenter = new JoystickButton(controller, 3), 
			buttonApproach = new JoystickButton(controller, 4);//B, X and Y
	private Encoder encFrontLeft = new Encoder(0, 1), encFrontRight = new Encoder(2, 3), encRearLeft=new Encoder(6, 7), encRearRight=new Encoder(8, 9);
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight()*Math.exp(-9);
	private final double SONAR_OFFSET = sonar.getOffset()*Math.exp(-9);
	private Accelerometer accel = new BuiltInAccelerometer();
	private ADXL345_SPI accel2 = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k16G);
	private Gyro gyro = new Gyro(1);
	private AnalogInput temp = new AnalogInput(2);
//	private DigitalInput button = new DigitalInput(6);
	public Robot(){
		drive.setInvertedMotor(MotorType.kFrontLeft, false);
		drive.setInvertedMotor(MotorType.kFrontRight, false);
		drive.setInvertedMotor(MotorType.kRearLeft, false);
		drive.setInvertedMotor(MotorType.kRearRight, false);
		encFrontLeft.setDistancePerPulse(wheelCirc/250);
		encFrontRight.setDistancePerPulse(-wheelCirc/250);
		encRearLeft.setDistancePerPulse(wheelCirc/360);
		encRearRight.setDistancePerPulse(-wheelCirc/360);
		new Thread(){
			@Override
			public void run(){
				while(true){
					SmartDashboard.putNumber("Sonar (in)", getSonar(true));
					SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
					SmartDashboard.putString("Front Left Encoder", encFrontLeft.getStopped() ? "Stopped" : encFrontLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Front Right Encoder", encFrontRight.getStopped() ? "Stopped" : encFrontRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putString("Rear Left Encoder", encRearLeft.getStopped() ? "Stopped" : encRearLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Rear Right Encoder", encRearRight.getStopped() ? "Stopped" : encRearRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putNumber("Front Left Encoder Value(in)", -encFrontLeft.getDistance());
					SmartDashboard.putNumber("Front Right Encoder Value(in)", -encFrontRight.getDistance());
					SmartDashboard.putNumber("Rear Left Encoder Value(in)", -encRearLeft.getDistance());
					SmartDashboard.putNumber("Rear Right Encoder Value(in)", -encRearRight.getDistance());
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
//					SmartDashboard.putBoolean("Microswitch", !button.get());//Pulled high
					if(buttonReset.get()){
						encFrontLeft.reset();
						encFrontRight.reset();
						encRearRight.reset();
						encRearLeft.reset();
						gyro.reset();
					}
					Timer.delay(0.005);
				}
			}
		}.start();
	}
	public void autonomous(){
		while(isAutonomous() && isEnabled()){
			Timer.delay(0.005);
		}
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			if(buttonCenter.get()){
				double angle = -gyro.getAngle() % 360;
				if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
				if(Math.abs(angle)<1){
//					drive.arcadeDrive(0, 0, false);//Stop
				}else if(Math.abs(angle)<15){
//					drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
				}else{
//					drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
				}
			}
//			drive.mecanumDrive_Cartesian(controller.getRawAxis(0), controller.getRawAxis(1), controller.getRawAxis(4), (int)gyro.getAngle());
			Timer.delay(0.005);
		}
	}
	private double getSonar(boolean inches){
		return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 * (inches ? 1 : 2.54);
	}
	private double getTemp(boolean celsius){
		double c = ((temp.getVoltage()-2.5)*9 + 25);
		if(celsius) return c;
		return 1.8*c + 32;
	}
	public void test(){
		double xifl = encFrontLeft.getDistance(), xifr=encFrontRight.getDistance(), xirl=encRearLeft.getDistance(), xirr=encRearRight.getDistance();
		while(isTest() && isEnabled()){
			double xffl = encFrontLeft.getDistance(), xffr=encFrontRight.getDistance(), xfrl=encRearLeft.getDistance(), xfrr=encRearRight.getDistance();
			if(xffl-xifl<25.1327) frontLeft.set(-0.1);
			if(!(xffl-xifl<25.1327)) frontLeft.set(0);
			if(xffr-xifr<25.1327) frontRight.set(-0.1);
			if(!(xffr-xifr<25.1327)) frontRight.set(0);
			if(xfrl-xirl<25.1327) backLeft.set(-0.1);
			if(!(xfrl-xirl<25.1327)) backLeft.set(0);
			if(xfrr-xirr<25.1327) backRight.set(-0.1);
			if(!(xfrr-xirr<25.1327)) backRight.set(0);
			Timer.delay(0.005);
		}
	}
}
