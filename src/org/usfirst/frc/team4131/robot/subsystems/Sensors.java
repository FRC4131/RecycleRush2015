package org.usfirst.frc.team4131.robot.subsystems;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Sensors extends Subsystem{
	private Gyro gyro;
	public Sensors(int gyro){
		super();
		this.gyro = new Gyro(gyro);
	}
	protected void initDefaultCommand(){}//No commands to run on this thing; just read sensor values.
	public double gyroAngle(){return gyro.getAngle();}
	public double gyroRate(){return gyro.getRate();}
}
