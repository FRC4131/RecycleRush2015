package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultClawElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClawElevator extends Subsystem{
	private final SpeedController elevator;
//	private final DigitalInput limit;
	public ClawElevator(int elevator/*, int limit*/){
		this.elevator = new CANTalon(elevator);
//		this.limit = new DigitalInput(limit);
	}
	@Override protected void initDefaultCommand(){setDefaultCommand(new DefaultClawElevatorCommand());}
	public void setElevation(double target){
		double diff = target - get();
		if(diff > 1) elevator.set(0.7);
		else if(diff < -1) elevator.set(-0.7);
		else elevator.set(0);
	}
	public void set(double speed){
		/*if(limit.get()) stop(); else*/ elevator.set(speed);
	}
	public void stop(){elevator.set(0);}
	public double get(){return elevator.get();}
//	public boolean getLimit(){return limit.get();}
}
