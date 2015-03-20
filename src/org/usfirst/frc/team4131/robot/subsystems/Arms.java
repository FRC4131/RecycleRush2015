package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultArmsCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arms extends Subsystem{
	private SpeedController left, right;
	public Arms(int left, int right){
		this.left = new CANTalon(left);
		this.right = new CANTalon(right);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultArmsCommand());}
	public void set(double left, double right){
		this.left.set(left);
		this.right.set(right);
	}
	public double getL(){return left.get();}
	public double get(){return right.get();}
}

