package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Talon;

public class Elevator{
	private DoubleSolenoid clamps;
	private Relay chains;
	private DigitalInput switch1, switch2;
	public Elevator(int pcm, int clamp, int unclamp, int chains, int switch1, int switch2){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chains = new Relay(chains);
		this.switch1 = new DigitalInput(switch1);
		this.switch2 = new DigitalInput(switch2);
	}
	public void engage(){clamps.set(Value.kForward);}
	public void disengage(){clamps.set(Value.kReverse);}
	public void lift(){chains.set(Relay.Value.kForward);}
	public void drop(){chains.set(Relay.Value.kReverse);}
	public void stop(){chains.set(Relay.Value.kOff);}
	public boolean getSwitch1(){return !switch1.get();}
	public boolean getSwitch2(){return !switch2.get();}
	public Boolean getElevator(){
		switch(chains.get()){
			case kForward: return Boolean.TRUE;
			case kReverse: return Boolean.FALSE;
			default: return null;
		}
	}
	public Boolean getClamps(){
		switch(clamps.get().value){
			case(Value.kForward_val): return Boolean.TRUE;
			case(Value.kReverse_val): return Boolean.FALSE;
			default: return null;
		}
	}
}
