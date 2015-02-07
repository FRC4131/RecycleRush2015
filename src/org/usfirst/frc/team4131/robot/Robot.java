package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
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
	public volatile double xAccel, yAccel;
	public volatile double xSpeed, ySpeed;
	public volatile double xDist, yDist;
	private PIDTalon frontLeft = new PIDTalon(1, 0, 1, 250, true), rearLeft = new PIDTalon(2, 2, 3, 360, true), frontRight = new PIDTalon(3, 4, 5, 250, false), rearRight = new PIDTalon(4, 6, 7, 290, false);
	private RobotDrive drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	private Joystick controller = new Joystick(0), io = new Joystick(1);
	private JoystickButton buttonReset = new JoystickButton(controller, 2), buttonCenter = new JoystickButton(controller, 3);//B, X
//	private Encoder encFrontLeft = new Encoder(0, 1), encRearLeft = new Encoder(2, 3), encFrontRight = new Encoder(4, 5), encRearRight = new Encoder(6, 7);
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight() * Math.exp(-9);//These two values and calculations are from the Javadoc
	private final double SONAR_OFFSET = sonar.getOffset() * Math.exp(-9);//of AnalogInput.getVoltage().
	private Accelerometer accel = new BuiltInAccelerometer();
	private ADXL345_SPI accel2 = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k16G);//x is side to side, y is forward and back, and z is verticallity
	private Gyro gyro = new Gyro(1);
	private AnalogInput temp = new AnalogInput(2);
//	private DigitalInput button = new DigitalInput(6);
	public Robot(){
		drive.setInvertedMotor(MotorType.kFrontLeft, false);
		drive.setInvertedMotor(MotorType.kFrontRight, false);
		drive.setInvertedMotor(MotorType.kRearLeft, false);
		drive.setInvertedMotor(MotorType.kRearRight, false);
		new Thread(){
			@Override
			public void run(){
				while(true){
					SmartDashboard.putNumber("Sonar (in)", getSonar(true));
					SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
					SmartDashboard.putString("Front Left Encoder", frontLeft.isStopped() ? "Stopped" : frontLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Front Right Encoder", frontRight.isStopped() ? "Stopped" : frontRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putString("Rear Left Encoder", rearRight.isStopped() ? "Stopped" : rearLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Rear Right Encoder", rearRight.isStopped() ? "Stopped" : rearRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putNumber("Front Left Encoder Value(in)", -frontLeft.getDistance());
					SmartDashboard.putNumber("Front Right Encoder Value(in)", -frontRight.getDistance());
					SmartDashboard.putNumber("Rear Left Encoder Value(in)", -rearLeft.getDistance());
					SmartDashboard.putNumber("Rear Right Encoder Value(in)", -rearRight.getDistance());
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
					if(buttonReset.get()){
						frontLeft.reset();
						rearLeft.reset();
						frontRight.reset();
						rearRight.reset();
					}
					SmartDashboard.putNumber("Battery Voltage", DriverStation.getInstance().getBatteryVoltage());
					Timer.delay(0.005);
				}
			}
		}.start();
		threadAccel.start();
	}
	public void autonomous(){
//		moveStraight(2);
		move(1);
	}
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			if(buttonCenter.get()){
				double angle = -gyro.getAngle() % 360;
				if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
				
				if(Math.abs(angle)<1) drive.arcadeDrive(0, 0, false);//Stop
				else if(Math.abs(angle)<15) drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
				else drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
			}
			double x = controller.getRawAxis(0) - (controller.getRawAxis(0) % 0.1);//Round to nearest 0.1
			double y = controller.getRawAxis(1) - (controller.getRawAxis(1) % 0.1);
			double turn = controller.getRawAxis(4) - (controller.getRawAxis(4) % 0.1);
			drive.mecanumDrive_Cartesian(x, y, turn, (int)gyro.getAngle());
			PIDTalon.equalize();
			SmartDashboard.putBoolean("Button", io.getRawButton(1));
			Timer.delay(0.005);
		}
	}
	public void test(){
		int ticks = 0;
		while(isOperatorControl() && isEnabled()){
			drive(-0.2);
			if(ticks % 20 == 0) PIDTalon.equalize();
			ticks++;
			Timer.delay(0.005);
		}
	}
	@Override
	public void disabled(){
		while(isDisabled()){
			SmartDashboard.putNumber("Accel X", accel.getX());
			SmartDashboard.putNumber("Accel Y", accel.getY());
			SmartDashboard.putNumber("Accel Z", accel.getZ());
			SmartDashboard.putNumber("Auto - Accel X", xAccel);
			SmartDashboard.putNumber("Auto - Speed X", xSpeed);
			SmartDashboard.putNumber("Auto - Dist X", xDist);
			SmartDashboard.putNumber("Auto - Accel Y", yAccel);
			SmartDashboard.putNumber("Auto - Speed Y", ySpeed);
			SmartDashboard.putNumber("Auto - Dist Y", yDist);
			Timer.delay(0.005);
		}
	}
	private double getSonar(boolean inches){return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 * (inches ? 1 : 2.54);}
	private double getTemp(boolean celsius){
		double c = ((temp.getVoltage()-2.5)*9 + 25);
		if(celsius) return c;
		return 1.8*c + 32;
	}
	private void changeX(){
		
	}
	private void changeY(double feet){
		double y = yDist;//Starting y position
		while(Math.abs(yDist - y) < Math.abs(feet)){
			drive.mecanumDrive_Cartesian(0, Math.copySign(0.2, feet), 0, gyro.getAngle());
			Timer.delay(0.005);
		}
	}
	private double avgDistance(){
		return (frontLeft.getDistance()+frontRight.getDistance()+rearRight.getDistance()+rearLeft.getDistance())/4.0; 
	}
	private void rotateTo(double angle){
		boolean incomplete = true;
		if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
		while(incomplete){
			if(Math.abs(angle)<1){
				drive.arcadeDrive(0, 0, false);//Stop
				incomplete=false;
			}else if(Math.abs(angle)<15)drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
			else drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
		}
	}
	private void moveStraight(double feet){
		boolean inversion=false;
		if(feet<0)inversion=true;
		double xi = avgDistance();
		final double xf = xi+(feet*12);//converting feet to in.
		while(inversion?xf<xi:xi<xf){
			if((inversion?xi-xf:xf-xi)>36){//if three feet or more, move fast
				drive.arcadeDrive(inversion?-0.5:0.5, 0, false);
			}else if((inversion?xi-xf:xf-xi)<=36&&(inversion?xi-xf:xf-xi)>18){//if between 1.5ft to 3ft
				drive.arcadeDrive(inversion?-0.3:0.3,0,false);
			}else{
				drive.arcadeDrive(inversion?-0.2:0.2,0,false);
			}
			xi=avgDistance();
		}
		drive.arcadeDrive(0, 0, false);//kill or stop
	}
	private void move(double feet){
		double start = avgDistance();
		while(Math.abs(avgDistance()) < Math.abs(feet*12-start)){
			PIDTalon.equalize();
			drive.drive(Math.copySign(0.2, feet), 0);
			Timer.delay(0.005);
		}
		drive.drive(0, 0);
	}
	private void addRotate(double angle){
		boolean incomplete = true;
		angle+=gyro.getAngle();
		if(Math.abs(angle)>180) angle = Math.copySign(Math.abs(angle)-180, -angle);//-270 becomes 90, 315 becomes -45
		while(incomplete){
			if(Math.abs(angle)<1){
				drive.arcadeDrive(0, 0, false);//Stop
				incomplete=false;
			}else if(Math.abs(angle)<15)drive.arcadeDrive(0, Math.copySign(0.1, angle), false);//Move slowly
			else drive.arcadeDrive(0, Math.copySign(0.3, angle), false);//Move quickly
		}
	}
	private void drive(double value){
//		frontLeft.set(fl*13.67 / 22.80);
//		rearLeft.set(rl*17.17 / 22.80);
//		frontRight.set(fr*22.80 / 22.80);
//		rearRight.set(rr*11.25 / 22.80);
		frontLeft.set(value);
		rearLeft.set(value);
		frontRight.set(value);
		rearRight.set(value);
	}
	Thread threadAccel = new Thread(){
		public void run(){
			double deadband = 0.09;
			while(true){
				accel2.updateTable();
				if(Math.abs(accel2.getY())>=deadband || Math.abs(accel2.getX())>=deadband){
					double angle=Math.toRadians(gyro.getAngle());//Angle, in radians, that we have turned
					//absX is the speed at which the robot is moving sideways, relative to the drive table (gyro.getAngle()==0)
					xAccel = Math.sin(angle) * accel2.getX()//Motion from moving sideways relative to the robot
							+ Math.sin(Math.PI/2 - angle) * accel2.getY();//Motion from moving forward relative to the robot
					xSpeed = Math.copySign(xSpeed + xAccel, xAccel);//The speed that the robot is moving sideways relative to the drive table
					xDist += xSpeed;//The distance the robot has traveled sideways relative to the drive table
					
					//absY is the speed at which the robot is moving forward, relative to the drive table (gyro.getAngle()==0)
					yAccel = Math.cos(angle) * accel2.getY()//Motion from moving forward relative to the robot
							+ Math.cos(Math.PI/2 - angle) * accel2.getX();//Motion from moving sideways relative to the robot
					ySpeed = Math.copySign(ySpeed + yAccel, yAccel);//The speed that the robot is moving sideways relative to the drive table
					yDist += ySpeed;
				}
				Timer.delay(0.005);
			}
		}
	};
}
