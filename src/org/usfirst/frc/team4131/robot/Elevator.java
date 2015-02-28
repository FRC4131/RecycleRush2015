package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Victor;

public class Elevator{
	private DoubleSolenoid clamps;
	private Victor chains;
	private AnalogInput pot;
	public Elevator(int pcm, int clamp, int unclamp, int chains, int pot){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chains = new Victor(chains);
		this.pot = new AnalogInput(pot);
	}
	public void engage(){clamps.set(Value.kForward);}
	public void disengage(){clamps.set(Value.kReverse);}
	public void setElevator(double speed){chains.set(speed);}
	public double getPot(){
		SmartDashboard.putNumber("Pot Mult", pot.getLSBWeight() * Math.exp(-9));
		SmartDashboard.putNumber("Pot Offset", pot.getLSBWeight() * Math.exp(-9));
		return pot.getVoltage();
	}
	public Boolean getElevator(){
		if(chains.get() > 0) return Boolean.TRUE;
		if(chains.get() < 0) return Boolean.FALSE;
		return null;
	}
	public Boolean getClamps(){
		switch(clamps.get().value){
			case(Value.kForward_val): return Boolean.TRUE;
			case(Value.kReverse_val): return Boolean.FALSE;
			default: return null;
		}
	}
}
