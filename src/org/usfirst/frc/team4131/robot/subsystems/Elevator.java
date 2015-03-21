package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	private CANTalon chainsL, chainsR;
	private double offsetL = 0, offsetR = 0;
	private Encoder encL, encR;
	public Elevator(int chainsL, int chainsR, int encL1, int encL2, int encR1, int encR2){
		super();
		this.chainsL = new CANTalon(chainsL);
		this.chainsR = new CANTalon(chainsR);
		this.encL = new Encoder(encL1, encL2);
		this.encL.setDistancePerPulse(360/7);
		this.encR = new Encoder(encR1, encR2);
		this.encR.setDistancePerPulse(360/7);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
//	/*
	public void set(double speed){chainsL.set(speed); chainsR.set(-speed);}
//	*/
	/*public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (cl + cr)/2;//Target encoder value; halfway in the middle
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Set to given value, with small adjustment to make the chains level out
		chainsL.set(vl); chainsR.set(vr);
	}*/
	/*public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (speed > 0 ? Math.max(cl, cr) : Math.min(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
		chainsL.set(vl); chainsR.set(vr);
	}*/
	public void setL(double speed){chainsL.set(speed);}
	public void setR(double speed){chainsR.set(speed);}
	public double get(){return (chainsL.get() + chainsR.get()) / 2;}
	public double getL(){return chainsL.get();}
	public double getR(){return chainsR.get();}
	public double getEncL(){return encL.get() - offsetL;}
	public double getEncR(){return encR.get() - offsetR;}
	public double getCurrentL(){return chainsL.getOutputCurrent();}
	public double getCurrentR(){return chainsR.getOutputCurrent();}
	public void resetL(){offsetL = encL.getDistance();}
	public void resetR(){offsetR = encR.getDistance();}
}
