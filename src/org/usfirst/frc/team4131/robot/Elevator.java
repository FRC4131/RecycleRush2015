package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Elevator{
	private DoubleSolenoid clamps;
	private Talon chains;
	public Elevator(int pcm, int clamp, int unclamp, int chains){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chains = new Talon(chains);
	}
	public void engage(){clamps.set(Value.kForward);}
	public void disengage(){clamps.set(Value.kReverse);}
	public void lift(){chains.set(0.1);}
	public void drop(){chains.set(-0.1);}
	public void stop(){chains.set(0);}
}
