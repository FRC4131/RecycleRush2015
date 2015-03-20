package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	private SpeedController chainsL, chainsR;
	private Encoder encL, encR;
	public Elevator(int chainsL, int chainsR, int encL1, int encL2, int encR1, int encR2){
		super();
		this.chainsL = new CANTalon(chainsL);
		this.chainsR = new CANTalon(chainsR);
		this.encL = new Encoder(encL1, encL2);
		this.encR = new Encoder(encR1, encR2);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
	/*public void set(double speed){chainsL.set(speed); chainsR.set(-speed);}*/
	/*public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (cl + cr)/2;//Target encoder value; halfway in the middle
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Set to given value, with small adjustment to make the chains level out
		chainsL.set(vl); chainsR.set(vr);
	}*/
	public void set(double speed){
		double cl = getEncL(), cr = getEncR();//Current encoder values
		double t = (speed > 0 ? Math.max(cl, cr) : Math.min(cl, cr));//Target is the value closest to where it needs to be: highest if heading up, lowest if heading down.
		double vl = speed + 0.1*(t - cl), vr = speed + 0.1*(t-cr);//Input value with small adjustment for leveling
		chainsL.set(vl); chainsR.set(vr);
	}
	public double get(){return chainsL.get();}
	public double getEncL(){return 1 * encL.get() - 0;}//Map to [0, 27]
	public double getEncR(){return 1 * encR.get() - 0;}
	public void reset(){
		encL.reset();
		encR.reset();
	}
}
