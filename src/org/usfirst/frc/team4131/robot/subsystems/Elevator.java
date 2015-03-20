package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem{
	private SpeedController chains;
	public Elevator(int chains){
		super();
		this.chains = new CANTalon(chains);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultElevatorCommand());}
	public void set(double speed){chains.set(speed);}
	public double get(){return chains.get();}
}
