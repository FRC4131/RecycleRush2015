package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors{
	private ADXL345_SPI accEx;
	private BuiltInAccelerometer accIn = new BuiltInAccelerometer();
	private AnalogInput sonar = new AnalogInput(0);
	private final double SONAR_MULT = sonar.getLSBWeight() * Math.exp(-9);//These two values and calculations are from the Javadoc
	private final double SONAR_OFFSET = sonar.getOffset() * Math.exp(-9);//of getVoltage().
	private Gyro gyro = new Gyro(1);
	private AnalogInput temp = new AnalogInput(2);
	private UpdateThread dashboard = new UpdateThread();
	public Sensors(SPI.Port accel, int sonar, int gyro, int temp){
		this.accEx = new ADXL345_SPI(accel, Accelerometer.Range.k16G);
		this.sonar = new AnalogInput(sonar);
		this.gyro = new Gyro(gyro);
		this.temp = new AnalogInput(temp);
		dashboard.start();
	}
	public double getAngle(){return gyro.getAngle();}
	public double getAcceleration(boolean external, char axis){
		Accelerometer accel = external ? this.accEx : accIn;
		switch(axis){
			case('x'): return accel.getX();
			case('y'): return accel.getY();
			case('z'): return accel.getZ();
			default: return 0;
		}
	}
	public double getSonar(boolean inches){return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28 * (inches ? 1 : 2.54);}
	public double getTemp(boolean celsius){
		double c = ((temp.getVoltage()-2.5)*9 + 25);
		if(celsius) return c;
		return 1.8*c + 32;
	}
	private class UpdateThread extends Thread{
		@Override
		public void run(){
			while(true){
				SmartDashboard.putNumber("Sonar (in)", getSonar(true));
				SmartDashboard.putNumber("Sonar (cm)", getSonar(false));
				SmartDashboard.putNumber("Accel X", accEx.getX());
				SmartDashboard.putNumber("Accel Y", accEx.getY());
				SmartDashboard.putNumber("Accel Z", accEx.getZ());
				accEx.updateTable();
				SmartDashboard.putNumber("Accel 2 X", accIn.getX());
				SmartDashboard.putNumber("Accel 2 Y", accIn.getY());
				SmartDashboard.putNumber("Accel 2 Z", accIn.getZ());
				SmartDashboard.putNumber("Gyro", gyro.getAngle());
				SmartDashboard.putNumber("Temperature (C)", getTemp(true));
				SmartDashboard.putNumber("Temperature (F)", getTemp(false));
				SmartDashboard.putNumber("Battery Voltage", DriverStation.getInstance().getBatteryVoltage());
				SmartDashboard.putBoolean("Button", Robot.oi.getIOSwitch(2));
				Timer.delay(0.005);
			}
		
		}
	}
}
