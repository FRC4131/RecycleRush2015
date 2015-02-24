package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Talon;

public class Arms {
	private Talon arm1, arm2;
	private Talon wheel1, wheel2;
	public Arms(int arm1, int arm2, int wheel1, int wheel2) {
		this.arm1 = new Talon(arm1);
		this.arm2 = new Talon(arm2);
		this.wheel1 = new Talon(wheel1);
		this.wheel2 = new Talon(wheel2);
	}
	public void squeeze(double left, double right){
		arm1.set(left);
		arm2.set(-right);
	}
	public void rollIn(double left, double right){
		wheel1.set(left);
		wheel2.set(right);
	}
}
