package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI{
	private static final double deadband = 0.09;
	private Joystick drive, other;
	public OI(int drive, int other){
		this.drive = new Joystick(drive);
		this.other = new Joystick(other);
	}
	public double getX(){return Math.abs(drive.getRawAxis(0)) > deadband ? drive.getRawAxis(0) : 0 ;}
	public double getY(){return Math.abs(drive.getRawAxis(1)) > deadband ? -drive.getRawAxis(1) : 0 ;}
	public int getPOV(){return drive.getPOV();}
	public double getRotation(){return Math.abs(drive.getRawAxis(4)) > deadband ? drive.getRawAxis(4) : 0 ;}
	public boolean getButton(boolean controller, Button button){return (controller ? drive : other).getRawButton(button.button);}
	public double getConveyorSpeed(){
		if(other.getRawButton(6)) return 0;
		double value = other.getRawAxis(2) - other.getRawAxis(3);
		return Math.abs(value) > deadband ? value : 0;
	}
	public static enum Button{
		A(1), B(2), X(3), Y(4), LEFT_BUMPER(5), RIGHT_BUMPER(6), SCREEN_SELECT(7), MENU(8), LEFT_STICK(9), RIGHT_STICK(10);
		public final int button;
		private Button(int button){this.button = button;}
	}
}
