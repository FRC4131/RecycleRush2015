package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Victor;

public class Elevator{
	private final double POT_MULT, POT_OFFSET;
	private DoubleSolenoid clamps;
	private Victor chains;
	private AnalogInput pot;
	public Elevator(int pcm, int clamp, int unclamp, int chains, int pot){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chains = new Victor(chains);
		this.pot = new AnalogInput(pot);
		POT_MULT = this.pot.getLSBWeight() * Math.exp(-9);
		POT_OFFSET = this.pot.getOffset() * Math.exp(-9);
	}
	public void engageClamp(){clamps.set(Value.kForward);}
	public void disengageClamp(){clamps.set(Value.kReverse);}
	public void setElevator(double speed){chains.set(speed);}
	public double getPot(){
		return (pot.getVoltage() * POT_MULT - POT_OFFSET) - 743 * 27/3.5;//Map potentiometer to [0, 27] (the height of the elevator)
	}
	public double getChains(){return chains.get();}
	public Boolean getClamps(){
		switch(clamps.get().value){
			case(Value.kForward_val): return Boolean.TRUE;
			case(Value.kReverse_val): return Boolean.FALSE;
			default: return null;
		}
	}
}
