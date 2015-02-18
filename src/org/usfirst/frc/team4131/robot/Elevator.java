package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Talon;

public class Elevator{
	private DoubleSolenoid clamps;
	private Talon chains;
	private DigitalInput switch1, switch2;
	public Elevator(int pcm, int clamp, int unclamp, int chains, int switch1, int switch2){
		this.clamps = new DoubleSolenoid(pcm, clamp, unclamp);
		this.chains = new Talon(chains);
		this.switch1 = new DigitalInput(switch1);
		this.switch2 = new DigitalInput(switch2);
	}
	public void engage(){clamps.set(Value.kForward);}
	public void disengage(){clamps.set(Value.kReverse);}
	public void lift(){chains.set(-0.2);}
	public void drop(){chains.set(0.2);}
	public void stop(){chains.set(0);}
	public boolean getSwitch1(){return !switch1.get();}
	public boolean getSwitch2(){return !switch2.get();}
	public double getElevator(){return chains.get();}
	public Boolean getClamps(){
		if(clamps.get().equals(Relay.Value.kForward)) return Boolean.TRUE;
		else if(clamps.get().equals(Relay.Value.kReverse)) return Boolean.FALSE;
		else return null;
	}
}
