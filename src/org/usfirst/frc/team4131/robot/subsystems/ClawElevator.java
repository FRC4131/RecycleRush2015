package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultClawElevatorCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClawElevator extends Subsystem{
	private final SpeedController elevator;
	private final AnalogInput pot;
	private final double potOffset, potMult;
	public ClawElevator(int elevator, int pot){
		this.elevator = new CANTalon(elevator);
		this.pot = new AnalogInput(pot);
		potOffset = this.pot.getOffset() * Math.exp(-9);
		potMult = this.pot.getLSBWeight() * Math.exp(-9);
	}
	@Override protected void initDefaultCommand(){setDefaultCommand(new DefaultClawElevatorCommand());}
	public void set(double target){
		double diff = target - get();
		if(diff > 1) elevator.set(0.7);
		else if(diff < -1) elevator.set(-0.7);
		else elevator.set(0);
	}
	public double get(){return pot.getVoltage() * potMult - potOffset;}
	
}
