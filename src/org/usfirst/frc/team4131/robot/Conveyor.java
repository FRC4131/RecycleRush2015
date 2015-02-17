package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Conveyor{
	private Relay conveyor, wheels;
	private double value = 0;
	public Conveyor(int conveyor, int wheels){
		this.conveyor = new Relay(conveyor);
		this.wheels = new Relay(wheels);
	}
	public void set(double value){
		this.value = value;
		if(value < 0){
			conveyor.set(Value.kReverse);
			wheels.set(Value.kReverse);
		}else if(value > 0){
			conveyor.set(Value.kForward);
			wheels.set(Value.kForward);
		}else{
			conveyor.set(Value.kOff);
			wheels.set(Value.kOff);
		}
	}
	public double get(){return value;}
}
