package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Clamps extends Subsystem{
	private DoubleSolenoid clamps;
	public Clamps(int engage, int disengage){
		clamps = new DoubleSolenoid(Robot.PCM_ID, engage, disengage);
	}
	@Override protected void initDefaultCommand(){}//Handled by OI
	public void set(boolean engaged){clamps.set(engaged ? Value.kForward : Value.kReverse);}
	public boolean get(){return clamps.get().equals(Value.kForward);}
}
