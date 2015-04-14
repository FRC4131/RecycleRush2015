package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * A collection of functions related to operator interface, primarily the joystick values.<br/>
 * Moving all of the OI code into one class allows us to quickly and easily change which button does which action.<br/>
 * When a controller is not plugged in, the functions will return the value that results in the safest action.
 * For example, motor control functions will return 0 when not connected, but "unlock drive" will return true.
 */
public class OI{
	//Buttons
	public static final int A=1, B=2, X=3, Y=4, LEFT_BUMPER=5, RIGHT_BUMPER=6, SCREEN_SELECT=7, MENU=8, LEFT_STICK=9, RIGHT_STICK=10;
	//Axes
	public static final int LEFT_X=0, LEFT_Y=1, LEFT_TRIGGER=2, RIGHT_TRIGGER=3, RIGHT_X=4, RIGHT_Y=5;
	//Controllers
	public final int DRIVE, OTHER;
	private static final double deadband = 0.09;
	private Joystick[] joysticks = new Joystick[DriverStation.kJoystickPorts];
	private boolean driverOrientation = false;//Previous button states, for toggle operations
	public OI(int drive, int other){
		DRIVE = drive;
		OTHER = other;
		for(int i=0;i<joysticks.length;i++) joysticks[i] = new Joystick(i);
	}
	public double x(){return getAxis(DRIVE, LEFT_X);}
	public double y(){return -getAxis(DRIVE, LEFT_Y);}
	public double rotation(){return getAxis(DRIVE, RIGHT_X);}
	public int lock(){return getPOV(DRIVE);}
	public double conveyorSpeed(){
		return getButton(OTHER, RIGHT_BUMPER) ? 0 : getAxis(OTHER, RIGHT_TRIGGER) - getAxis(OTHER, LEFT_TRIGGER);
	}
	public boolean conveyorOverride(){return !getButton(OTHER, RIGHT_BUMPER) && getButton(OTHER, MENU);}
	public double leftArm(){return getAxis(OTHER, LEFT_X);}
	public double rightArm(){return getAxis(OTHER, RIGHT_X);}
	public double leftArmWheels(){return removeDeadband(getAxis(OTHER, LEFT_Y));}
	public double rightArmWheels(){return removeDeadband(getAxis(OTHER, RIGHT_Y));}
	public boolean clawOpen(){return getButton(OTHER, RIGHT_BUMPER) && getButton(OTHER, Y);}
	public boolean clawClose(){return getButton(OTHER, RIGHT_BUMPER) && getButton(OTHER, A);}
	public double clawElevation(){return getButton(OTHER, RIGHT_BUMPER) ? getAxis(OTHER, LEFT_Y) : 0;}
	public double clawRotation(){return getButton(OTHER, RIGHT_BUMPER) ? getAxis(OTHER, LEFT_X) : 0;}
	public double elevator(){return getAxis(DRIVE, RIGHT_TRIGGER) - getAxis(DRIVE, LEFT_TRIGGER);}
	public boolean engageClamp(){return !getButton(OTHER, RIGHT_BUMPER) && getButton(OTHER, X);}
	public boolean disengageClamp(){return !getButton(OTHER, RIGHT_BUMPER) && getButton(OTHER, B);}
	public boolean unlock(){return getButton(DRIVE, LEFT_BUMPER, true);}
	public boolean driverOrientation(){
		boolean pressed = getButton(DRIVE, SCREEN_SELECT);
		boolean retVal = pressed && !driverOrientation;
		driverOrientation = pressed;
		return retVal;
	}
	public boolean resetSensors(){return getButton(DRIVE, B);}
	
	public boolean getButton(int stick, int button){return getButton(stick, button, false);}
	public boolean getButton(int stick, int button, boolean def){
		if(!isConnected(stick)) return def;
		return joysticks[stick].getRawButton(button);
	}
	public double getAxis(int stick, int axis){return getAxis(stick, axis, 0);}
	public double getAxis(int stick, int axis, double def){
		if(!isConnected(stick)) return def;
		return removeDeadband(joysticks[stick].getRawAxis(axis));
	}
	public int getPOV(int stick){return getPOV(stick, -1);}
	public int getPOV(int stick, int def){
		if(!isConnected(stick)) return def;
		return joysticks[stick].getPOV();
	}
	public boolean isConnected(int stick){return DriverStation.getInstance().getStickAxisCount(stick) > 0;}
	private static double removeDeadband(double raw){return (Math.abs(raw)) > deadband ? raw : 0;}
}
