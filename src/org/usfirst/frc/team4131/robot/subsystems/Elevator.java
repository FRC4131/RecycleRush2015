package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	private CANTalon chainsL, chainsR;
	private DigitalInput limitL, limitR;
	private Encoder encL, encR;
	public static final int ENCODER_MAX = Integer.MAX_VALUE;//Not yet known
	public Elevator(int chainsL, int chainsR, int limitL, int limitR, int encL1, int encL2, int encR1, int encR2){
		super();
		this.chainsL = new CANTalon(chainsL);
		this.chainsR = new CANTalon(chainsR);
		this.limitL = new DigitalInput(limitL);
		this.limitR = new DigitalInput(limitR);
		this.encL = new Encoder(encL1, encL2); this.encL.setDistancePerPulse(1);
		this.encR = new Encoder(encR1, encR2); this.encL.setDistancePerPulse(1);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
	public void setL(double speed){
		if((speed < 0 && getLimitL()) || (speed > 0 && getEncoderL() > ENCODER_MAX)) stopL();
		else chainsL.set(-speed);
	}
	public void setR(double speed){
		if((speed < 0 && getLimitR()) || (speed > 0 && getEncoderR() > ENCODER_MAX)) stopR();
		else chainsR.set(speed);
	}
	public void set(double speed){setL(speed); setR(speed);}
	public void stopL(){chainsL.set(0);}
	public void stopR(){chainsR.set(0);}
	public void stop(){stopL(); stopR();}
	public double get(){return (chainsL.get() + chainsR.get()) / 2;}
	public double getL(){return -chainsL.get();}
	public double getR(){return chainsR.get();}
	public double getCurrentL(){return chainsL.getOutputCurrent();}
	public double getCurrentR(){return chainsR.getOutputCurrent();}
	public boolean getLimitL(){return limitL.get();}
	public boolean getLimitR(){return limitR.get();}
	public int getEncoderL(){return encL.get();}
	public int getEncoderR(){return encR.get();}
	public void resetL(){encL.reset();}
	public void resetR(){encR.reset();}
}
