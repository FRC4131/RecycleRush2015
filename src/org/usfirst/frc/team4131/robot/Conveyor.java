package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Conveyor{
	private SpeedController left, right, conveyor;
	public Conveyor(int left, int right, int conveyor){
		this.left = new Victor(left);
		this.right = new Talon(right);
		this.conveyor = new Talon(conveyor);
	}
	public void set(double value){
		left.set(0.38*value);
		right.set(0.38*value);
		conveyor.set(value);
	}
	public double get(){return conveyor.get();}
}
