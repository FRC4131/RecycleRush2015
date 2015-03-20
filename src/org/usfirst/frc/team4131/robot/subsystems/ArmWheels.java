package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultArmWheelsCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArmWheels extends Subsystem{
	private SpeedController left, right;
	public ArmWheels(int left, int right){
		super();
		this.left = new CANTalon(left);
		this.right = new CANTalon(right);
	}
	@Override protected void initDefaultCommand(){setDefaultCommand(new DefaultArmWheelsCommand());}
	public void set(double left, double right){
		this.left.set(left);
		this.right.set(right);
	}
	public double getL(){return left.get();}
	public double getR(){return right.get();}
}
