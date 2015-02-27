package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Talon;

public class Arms{
//	private final double POT_MULT1, POT_OFFSET1, POT_MULT2, POT_OFFSET2;
	private Talon arm1, arm2;
	private Relay wheel1, wheel2;
//	private AnalogInput pot1, pot2;
	public Arms(int arm1, int arm2, int wheel1, int wheel2/*, int pot1, int pot2*/){
		this.arm1 = new Talon(arm1);
		this.arm2 = new Talon(arm2);
		this.wheel1 = new Relay(wheel1);
		this.wheel2 = new Relay(wheel2);/*
		this.pot1 = new AnalogInput(pot1);
		this.pot2 = new AnalogInput(pot2);
		POT_MULT1 = this.pot1.getLSBWeight() * Math.exp(-9);
		POT_OFFSET1 = this.pot1.getOffset() * Math.exp(-9);
		POT_MULT2 = this.pot2.getLSBWeight() * Math.exp(-9);
		POT_OFFSET2 = this.pot2.getOffset() * Math.exp(-9);*/
	}
	public void squeeze(double left, double right){
		arm1.set(left/2);
		arm2.set(right/2);
	}
	public void rollIn(double left, double right){
		wheel1.set(left > 0 ? Value.kForward : left < 0 ? Value.kReverse : Value.kOff);
		wheel2.set(right < 0 ? Value.kForward : right > 0 ? Value.kReverse : Value.kOff);
	}/*
	public double leftRot(){return pot1.getVoltage() * POT_MULT1 + POT_OFFSET1;}
	public double rightRot(){return pot2.getVoltage() * POT_MULT2 + POT_OFFSET2;}*/
}
