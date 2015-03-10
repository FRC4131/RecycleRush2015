package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

/**
 * A claw on the back of the robot to pick up the trash can.
 * There are two metal arms of the claw, controlled by one DoubleSolenoid.
 * That whole assembly rotates by a Talon (with an encoder attached: 7 pulses per revolution, geared 72:1).
 * That panel is moved up and down a metal frame with a chain, connected to a Relay.
 * The frame extends and retracts (DoubleSolenoid) from the back of the robot to prevent the plate from colliding with the electronics.
 */
public class Claw{
	private final double POT_MULT, POT_OFFSET;
	private DoubleSolenoid claw, elbow;
	private SpeedController elevator, wrist;
	private AnalogInput pot;
	private Encoder encoder;
	public Claw(int pcm, int clawOpen, int clawClose, int elbowIn, int elbowOut, int elevator, int wrist, int pot, int enc1, int enc2){
		claw = new DoubleSolenoid(pcm, clawOpen, clawClose);
		elbow = new DoubleSolenoid(pcm, elbowIn, elbowOut);
		this.elevator = new Talon(elevator);
		this.wrist = new Talon(wrist);
		this.pot = new AnalogInput(pot);
		POT_MULT = this.pot.getLSBWeight() * Math.exp(-9);
		POT_OFFSET = this.pot.getOffset() * Math.exp(-9);
		encoder = new Encoder(enc1, enc2);
	}
	public void open(){claw.set(Value.kForward);}
	public void close(){claw.set(Value.kReverse);}
	public void rotate(double speed){
		if (encoder.getDistance()>504 || encoder.getDistance()<=0){
			wrist.set(0);
		}
		wrist.set(speed);
	}
	public void elevate(double speed){
		if((speed < 0 && getElevation() > 1) || (speed > 0 && getElevation() > 60) || speed==0) elevator.set(speed);//Physical limits
		//TODO find actual height of claw
		elbow.set(getElevation() < 1 ? Value.kForward : Value.kReverse);
	}
	private double getElevation(){return pot.getVoltage() * POT_MULT - POT_OFFSET;}//Helper for applying the multiplier and offset values
}
