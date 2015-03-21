package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.SpeedController;

public class PseudoMotor implements SpeedController{
	private double value;
	@Override public void pidWrite(double output){value = output;}
	@Override public double get(){return value;}
	@Override public void set(double speed, byte syncGroup){value = speed;}
	@Override public void set(double speed){value = speed;}
	@Override public void disable(){value = 0;}
}
