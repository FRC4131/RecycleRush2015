package org.usfirst.frc.team4131.robot.oi;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

public class POV extends Button{
	private Joystick stick;
	private int value;
	public POV(Joystick stick){
		super();
		this.stick = stick;
	}
	@Override
	public boolean get(){
		int prevValue = value;
		value = stick.getPOV();
		return value!=prevValue && value!=-1;
	}
	public POV onPress(Command command){whenPressed(command); return this;}
	public POV whilePressed(Command command){whilePressed(command); return this;}
	public POV onRelease(Command command){whenReleased(command); return this;}
}
