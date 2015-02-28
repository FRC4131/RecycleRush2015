package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A collection of functions related to operator interface, primarily the joystick values.
 * 
 */
public class OI{
	public static final int A=1, B=2, X=3, Y=4, LEFT_BUMPER=5, RIGHT_BUMPER=6, SCREEN_SELECT=7, MENU=8, LEFT_STICK=9, RIGHT_STICK=10;
	public static final int LEFT_X=0, LEFT_Y=1, LEFT_TRIGGER=2, RIGHT_TRIGGER=3, RIGHT_X=4, RIGHT_Y=5;
	private static final double deadband = 0.09;
	private boolean toggleOrientation = false;//Whether the button is pressed
	private Joystick drive, other;
	public OI(int drive, int other){
		this.drive = new Joystick(drive);
		this.other = new Joystick(other);
	}
	public double getX(){return removeDeadband(drive.getRawAxis(LEFT_X) * 1.25);}
	public double getY(){return -removeDeadband(drive.getRawAxis(LEFT_Y));}
	public int getPOV(){return drive.getPOV();}
	public double getRotation(){return removeDeadband(drive.getRawAxis(RIGHT_X));}
	public boolean getButton(boolean controller, int button){return (controller ? drive : other).getRawButton(button);}
	public double getConveyorSpeed(){
		return other.getRawButton(RIGHT_BUMPER) ? 0 : removeDeadband(other.getRawAxis(RIGHT_TRIGGER) - other.getRawAxis(LEFT_TRIGGER));
	}
	public double leftArm(){return removeDeadband(other.getRawAxis(LEFT_X));}
	public double rightArm(){return removeDeadband(other.getRawAxis(RIGHT_X));}
	public double leftArmWheels(){return removeDeadband(other.getRawAxis(LEFT_Y));}
	public double rightArmWheels(){return removeDeadband(other.getRawAxis(RIGHT_Y));}
	public Boolean getClaw(){
		if(other.getRawButton(RIGHT_BUMPER)){
			if(other.getRawButton(Y)) return Boolean.TRUE;
			if(other.getRawButton(A)) return Boolean.FALSE;
		}
		return null;
	}
	public double getClawElevation(){return other.getRawButton(RIGHT_BUMPER) ? other.getRawAxis(LEFT_Y) : 0;}
	public double getClawRotation(){return other.getRawButton(RIGHT_BUMPER) ? other.getRawAxis(LEFT_X) : 0;}
	public double getElevator(){return removeDeadband(drive.getRawAxis(RIGHT_TRIGGER) - drive.getRawAxis(LEFT_TRIGGER));}
	public boolean engageClamp(){return !other.getRawButton(RIGHT_BUMPER) && other.getRawButton(X);}
	public boolean disengageClamp(){return !other.getRawButton(RIGHT_BUMPER) && other.getRawButton(B);}
	public boolean unlock(){return drive.getRawButton(LEFT_BUMPER);}
	public boolean toggleDriverOrientation(){
		boolean pressed = drive.getRawButton(SCREEN_SELECT);
		boolean retVal = pressed && !toggleOrientation;
		toggleOrientation = pressed;
		return retVal;
	}
	public boolean resetSensors(){return drive.getRawButton(B);}
	private double removeDeadband(double raw){return (Math.abs(raw)) > deadband ? raw : 0;}
}
