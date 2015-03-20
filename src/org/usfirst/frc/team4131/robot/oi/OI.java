package org.usfirst.frc.team4131.robot.oi;

import static org.usfirst.frc.team4131.robot.oi.GameController.*;

import org.usfirst.frc.team4131.robot.commands.*;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI{
	private GameController drive = new GameController(0), aux = new GameController(1);
	private Button clawOpen = new JoystickButton(aux, Y), clawClose = new JoystickButton(aux, A),
			engageClamp = new JoystickButton(aux, X), disengageClamp = new JoystickButton(aux, B), lock = new POV(drive),
			unlock = new JoystickButton(drive, LEFT_BUMPER), driverOriented = new JoystickButton(drive, SCREEN_SELECT),
			resetSensors = new JoystickButton(drive, B);
	//button.whenPressed(Command command): run the command once when pressed
	//button.whileHeld(Command command): run the command while the button is held and interrupt it when it's released
	//button.whenReleased(Command command): run the command once when released
	public OI(){
		clawOpen.whenPressed(new ClawCommand(false));
		clawClose.whenPressed(new ClawCommand(true));
		engageClamp.whenPressed(new ClampCommand(true));
		disengageClamp.whenPressed(new ClampCommand(false));
		lock.whenPressed(new LockCommand(drive.getPOV()));
		unlock.whenPressed(new LockCommand(-1));
		driverOriented.whenPressed(new DriverOrientationCommand());
		resetSensors.whenPressed(new SensorResetCommand());
	}
	public double x(){return removeDeadband(drive.getRawAxis(LEFT_X)) * 1.25;}
	public double y(){return -removeDeadband(drive.getRawAxis(LEFT_Y));}
	public double rotation(){return removeDeadband(drive.getRawAxis(RIGHT_X));}
	public int lock(){return drive.getPOV();}
	public double leftArm(){return removeDeadband(aux.getRawAxis(LEFT_X));}
	public double rightArm(){return removeDeadband(aux.getRawAxis(RIGHT_X));}
	public double leftArmWheels(){return removeDeadband(aux.getRawAxis(LEFT_Y));}
	public double rightArmWheels(){return removeDeadband(aux.getRawAxis(RIGHT_Y));}
	public double clawElevation(){return removeDeadband(aux.getRawAxis(LEFT_Y));}
	public double elevator(){return removeDeadband(drive.getRawAxis(RIGHT_TRIGGER) - drive.getRawAxis(LEFT_TRIGGER));}
	
	private static double removeDeadband(double raw){return (Math.abs(raw)) > 0.09 ? raw : 0;}
}

