package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class Elevator{
	private SpeedController chainsL, chainsR;
	private Encoder encL, encR;
	private DoubleSolenoid clamps;
	public Elevator(int pcm, int clamp, int unclamp, int chainsL, int chainsR, int encL1, int encL2, int encR1, int encR2){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chainsL = new Talon(chainsL);
		this.chainsR = new Talon(chainsR);
		this.encL = new Encoder(encL1, encL2);
		this.encR = new Encoder(encR1, encR2);
	}
	public void engageClamp(){clamps.set(Value.kForward);}
	public void disengageClamp(){clamps.set(Value.kReverse);}
	//Alternative I: No leveling: just go
	/*public void setElevator(double speed){chainsL.set(speed); chainsR.set(speed);}*/
	//Alternative II: Slow down the higher side and speed up the lower side
	/*public void setElevator(double v){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (cl + cr)/2;//Target encoder value; halfway in the middle
		double vl = v + 0.1*(t - cl), vr = v + 0.1*(t-cr);//Set to given value, with small adjustment to make the chains level out
		chainsL.set(vl); chainsR.set(vr);
	}*/
	//Alternative III: Leave the higher side alone and speed up the lower side
	public void setElevator(double v){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (v > 0 ? Math.max(cl, cr) : Math.min(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
		chainsL.set(v + 0.1*(t - cl)); chainsR.set(v + 0.1*(t - cr));//Input value with small adjustment for leveling
	}
	
	public double getEncL(){return 1 * (encL.get() - 0);}//Map to [0, 27] (height of elevator)
	public double getEncR(){return 1 * (encR.get() - 0);} 
	public Boolean getElevator(){
		if(chainsL.get() > 0) return Boolean.TRUE;
		if(chainsL.get() < 0) return Boolean.FALSE;
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
