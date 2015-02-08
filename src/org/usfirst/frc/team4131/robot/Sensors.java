package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.ADXL345_SPI;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Sensors implements Runnable{
	private final double SONAR_MULT, SONAR_OFFSET;//These two values and calculations are from the Javadoc of AnalogInput.getVoltage().
	private ADXL345_SPI accEx;
//	private BuiltInAccelerometer accIn = new BuiltInAccelerometer();
	private AnalogInput sonar, temp;
	private Gyro gyro;
	public Sensors(int accel, int sonar, int gyro, int temp){
		switch(accel){
			case(0): accEx = new ADXL345_SPI(SPI.Port.kOnboardCS0, Range.k16G); break;
			case(1): accEx = new ADXL345_SPI(SPI.Port.kOnboardCS1, Range.k16G); break;
			case(2): accEx = new ADXL345_SPI(SPI.Port.kOnboardCS2, Range.k16G); break;
			case(3): accEx = new ADXL345_SPI(SPI.Port.kOnboardCS3, Range.k16G); break;
			case(4): accEx = new ADXL345_SPI(SPI.Port.kMXP, Range.k16G); break;
		}
		this.sonar = new AnalogInput(sonar);
		SONAR_MULT = this.sonar.getLSBWeight() * Math.exp(-9);
		SONAR_OFFSET = this.sonar.getOffset() * Math.exp(-9);
		this.gyro = new Gyro(gyro);
		this.temp = new AnalogInput(temp);
//		new Thread(this).start();
	}
	public double getAcceleration(char axis){
		accEx.updateTable();
		switch(axis){
			case('x'): case('X'): return accEx.getX();
			case('y'): case('Y'):return accEx.getY();
			case('z'): case('Z'):return accEx.getZ();
			default: return 0;
		}
	}
	public double sonarIn(){return (sonar.getVoltage() * SONAR_MULT - SONAR_OFFSET) * 0.28;}
	public double sonarCm(){return 2.54 * sonarIn();}
	public double gyroAngle(){return gyro.getAngle();}
	public double tempC(){return (temp.getVoltage()-2.5)*9 + 25;}
	public double tempF(){return 1.8 * tempC() + 32;}
	@Override
	public void run(){
		SmartDashboard.putNumber("AccX", getAcceleration('x'));
		SmartDashboard.putNumber("AccY", getAcceleration('y'));
		SmartDashboard.putNumber("AccZ", getAcceleration('z'));
		SmartDashboard.putNumber("Sonar (in)", sonarIn());
		SmartDashboard.putNumber("Gyro", gyroAngle());
		SmartDashboard.putNumber("Temp (C)", tempC());
		SmartDashboard.putNumber("Temp (F)", tempF());
	}
}
