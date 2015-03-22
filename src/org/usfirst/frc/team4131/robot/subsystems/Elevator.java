package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.Robot;
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
		this.encL.setDistancePerPulse(1);
		this.encR = new Encoder(encR1, encR2);
		this.encR.setDistancePerPulse(1);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
	public void set(double speed, boolean sync){
		if(sync && Math.abs(getEncL() - getEncR()) < 2000){
			double cl = getEncL(), cr = getEncR();//Current encoder values
			double t = (speed > 0 ? Math.min(cl, cr) : Math.max(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
			double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
			chainsL.set(vl); chainsR.set(vr);
		}else{
			chainsL.set(-speed);
			chainsR.set(speed);
		}
	}
	/*
	public void set(double speed){chainsL.set(-speed); chainsR.set(speed);}
	*/
	/*public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (cl + cr)/2;//Target encoder value; halfway in the middle
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Set to given value, with small adjustment to make the chains level out
		chainsL.set(vl); chainsR.set(vr);
	}*/
	/*
	public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (speed > 0 ? Math.max(cl, cr) : Math.min(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
		chainsL.set(vl); chainsR.set(vr);
	}
	*/
	/*
	public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (speed > 0 ? Math.min(cl, cr) : Math.max(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
		chainsL.set(vl); chainsR.set(vr);
	}
	 */
	public void setL(double speed){
		chainsL.set(-speed);
		if(speed!=0) Robot.log(this, "Set left to " + (-speed));
	}
	public void setR(double speed){
		chainsR.set(speed);
		if(speed!=0) Robot.log(this, "Set right to " + speed);
	}
	public void stopL(){chainsL.set(0);}
	public void stopR(){chainsR.set(0);}
	public void stop(){stopL(); stopR();}
	public double get(){return (chainsL.get() + chainsR.get()) / 2;}
	public double getL(){return -chainsL.get();}
	public double getR(){return chainsR.get();}
	public double getEncL(){return encL.getDistance() - offsetL;}
	public double getEncR(){return encR.getDistance() - offsetR;}
	public double getEncRawL(){return encL.getDistance();}
	public double getEncRawR(){return encR.getDistance();}
	public double getCurrentL(){return chainsL.getOutputCurrent();}
	public double getCurrentR(){return chainsR.getOutputCurrent();}
	public void resetL(){
		offsetL = encL.getDistance();
		System.out.println("Resetting left elevator to " + offsetL);
	}
	public void resetR(){
		offsetR = encR.getDistance();
		System.out.println("Resetting right elevator to " + offsetR);
	}
	
	//Defensive Cheaty Hack Methods
	public void moveLeftDown(){
		chainsL.set(0.5);
		Robot.log(this, "Hax McCheatiness (Left Side)");
	}
	public void moveRightDown(){
		chainsR.set(-0.5);
		Robot.log(this, "Hax McCheatiness (Right Side)");
	}
}
