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
	public double durationFL;
	public double durationFR;
	public double durationRL;
	public double durationRR;
//	private final double CIRCUMFERENCE = 25.1327;//Circumference of each wheel
//	private PIDTalon frontLeft = new PIDTalon(0, 0, 1, false), frontRight = new PIDTalon(2, 4, 5, true);
//	private Talon rearLeft = new Talon(1), rearRight = new Talon(3);
	private PIDTalon frontLeft = new PIDTalon(1, 0, 1,250, false), rearLeft = new PIDTalon(2, 2, 3,360, false), frontRight = new PIDTalon(3, 4, 5, 250, true), rearRight = new PIDTalon(4, 6, 7, 360, true);
	private RobotDrive drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	private Joystick controller = new Joystick(0), io = new Joystick(1);
	private JoystickButton buttonReset = new JoystickButton(controller, 2), buttonCenter = new JoystickButton(controller, 3);//B, X
//	private Encoder encFrontLeft = new Encoder(0, 1), encRearLeft = new Encoder(2, 3), encFrontRight = new Encoder(4, 5), encRearRight = new Encoder(6, 7);
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight() * Math.exp(-9);//These two values and calculations are from the Javadoc
	private final double SONAR_OFFSET = sonar.getOffset() * Math.exp(-9);//of 
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
//		encFrontLeft.setDistancePerPulse(CIRCUMFERENCE/250);//distance in inches per tick divided inches per second
//		encFrontRight.setDistancePerPulse(-CIRCUMFERENCE/250);//returns time
//		encRearLeft.setDistancePerPulse(CIRCUMFERENCE/360);
//		encRearRight.setDistancePerPulse(-CIRCUMFERENCE/360);
		new Thread(){
			@Override
			public void run(){
				while(true){
					SmartDashboard.putNumber("TimeFL", durationFL);
					SmartDashboard.putNumber("TimeFR", durationFR);
					SmartDashboard.putNumber("TimeRR", durationRR);
					SmartDashboard.putNumber("TimeRL", durationRL);
					SmartDashboard.putNumber("Sonar (in)", getSonar(true));
					SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
					/*SmartDashboard.putString("Front Left Encoder", encFrontLeft.getStopped() ? "Stopped" : encFrontLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Front Right Encoder", encFrontRight.getStopped() ? "Stopped" : encFrontRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putString("Rear Left Encoder", encRearLeft.getStopped() ? "Stopped" : encRearLeft.getDirection() ? "Backward" : "Forward");
					SmartDashboard.putString("Rear Right Encoder", encRearRight.getStopped() ? "Stopped" : encRearRight.getDirection() ? "Forward" : "Backward");
					SmartDashboard.putNumber("Front Left Encoder Value(in)", -encFrontLeft.getDistance());
					SmartDashboard.putNumber("Front Right Encoder Value(in)", -encFrontRight.getDistance());
					SmartDashboard.putNumber("Rear Left Encoder Value(in)", -encRearLeft.getDistance());
					SmartDashboard.putNumber("Rear Right Encoder Value(in)", -encRearRight.getDistance());*/
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
						/*encFrontLeft.reset();
						encFrontRight.reset();
						encRearRight.reset();
						encRearLeft.reset();*/
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
	}
	public void autonomous(){
		
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
	private double getSonar(boolean inches){return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 * (inches ? 1 : 2.54);}
	private double getTemp(boolean celsius){
		double c = ((temp.getVoltage()-2.5)*9 + 25);
		if(celsius) return c;
		return 1.8*c + 32;
	}
	private void changeX(){
		
	}
	private void changeY(){
		
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
				drive.arcadeDrive(inversion?-1.0:1.0, 0, false);
			}else if((inversion?xi-xf:xf-xi)<=36&&(inversion?xi-xf:xf-xi)>18){//if between 1.5ft to 3ft
				drive.arcadeDrive(inversion?-0.5:0.5,0,false);
			}else{
				drive.arcadeDrive(inversion?-0.2:0.2,0,false);
			}
			xi=avgDistance();
		}
		drive.arcadeDrive(0,0,false);//kill or stop
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
}
