package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Talon;

public class Conveyor{
	private Talon motor;
	public Conveyor(int motor){
		this.motor = new Talon(motor);
	}
	public void set(double value){
		motor.set(value);
	}
	public double get(){return motor.get();}
}
