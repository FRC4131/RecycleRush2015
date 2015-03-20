package org.usfirst.frc.team4131.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Claw extends Subsystem{
	public static final double HEIGHT = 66;//Height, in inches, of the claw
	private final DoubleSolenoid claw;
	public Claw(int pcm, int open, int close){
		super();
		this.claw = new DoubleSolenoid(pcm, open, close);
	}
	protected void initDefaultCommand(){}//Handled by OI
	public void setOpen(boolean open){claw.set(open ? Value.kForward : Value.kReverse);}
}