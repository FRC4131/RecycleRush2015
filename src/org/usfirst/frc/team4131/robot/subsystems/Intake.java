package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultIntakeCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem{
	private CANTalon left, right;
	public Intake(int left, int right){
		super();
		this.left = new CANTalon(left);
		this.right = new CANTalon(right);
	}
	@Override protected void initDefaultCommand(){setDefaultCommand(new DefaultIntakeCommand());}
	public void set(double value){
		this.left.set(value);
		this.right.set(value);
	}
	public double get(){return right.get();}
	public void stop(){left.set(0); right.set(0);}
	public double getCurrentL(){return left.getOutputCurrent();}
	public double getCurrentR(){return right.getOutputCurrent();}
}
