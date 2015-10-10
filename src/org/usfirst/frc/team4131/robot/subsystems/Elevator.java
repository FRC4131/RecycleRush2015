package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.Robot;
import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	private CANTalon chainsL, chainsR;
//	private double offsetL = 0, offsetR = 0;
	private DigitalInput limitL, limitR;
	public Elevator(int chainsL, int chainsR, int limitL, int limitR){
		super();
		this.chainsL = new CANTalon(chainsL);
		this.chainsR = new CANTalon(chainsR);
		this.limitL = new DigitalInput(limitL);
		this.limitR = new DigitalInput(limitR);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
	/*public void set(double speed, boolean sync){
		if(sync && Math.abs(getEncL() - getEncR()) < 2000){
			double cl = getEncL(), cr = getEncR();//Current encoder values
			double t = (speed > 0 ? Math.min(cl, cr) : Math.max(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
			double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
			chainsL.set(vl); chainsR.set(vr);
		}else{
			chainsL.set(-speed);
			chainsR.set(speed);
		}
	}*/
	public void set(double speed){
		chainsL.set(-speed);
		chainsR.set(speed);
	}
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
	public double getCurrentL(){return chainsL.getOutputCurrent();}
	public double getCurrentR(){return chainsR.getOutputCurrent();}
	/*public void resetL(){
		offsetL = encL.getDistance();
		System.out.println("Resetting left elevator to " + offsetL);
	}
	public void resetR(){
		offsetR = encR.getDistance();
		System.out.println("Resetting right elevator to " + offsetR);
	}*/
	public boolean getLimitL(){return limitL.get();}
	public boolean getLimitR(){return limitR.get();}
	
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
