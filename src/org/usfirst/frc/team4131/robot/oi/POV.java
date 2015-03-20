package org.usfirst.frc.team4131.robot.oi;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

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
}
