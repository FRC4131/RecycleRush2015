package org.usfirst.frc.team4131.robot.oi;

import org.usfirst.frc.team4131.robot.commands.ClampCommand;
import org.usfirst.frc.team4131.robot.commands.ClawCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI{
	private static final int A=1, B=2, X=3, Y=4, LEFT_BUMPER=5, RIGHT_BUMPER=6, SCREEN_SELECT=7, MENU=8, LEFT_STICK=9, RIGHT_STICK=10;
	private static final int LEFT_X=0, LEFT_Y=1, LEFT_TRIGGER=2, RIGHT_TRIGGER=3, RIGHT_X=4, RIGHT_Y=5;
	private Joystick drive = new Joystick(0), aux = new Joystick(1);
	private Button clawOpen = new JoystickButton(aux, Y), clawClose = new JoystickButton(aux, A),
			engageClamp = new JoystickButton(aux, X), disengageClamp = new JoystickButton(aux, B)//,
//			unlock = new JoystickButton(drive, LEFT_BUMPER), driverOriented = new JoystickButton(drive, SCREEN_SELECT),
//			resetSensors = new JoystickButton(drive, B)
			;
	//button.whenPressed(Command command): run the command once when pressed
	//button.whileHeld(Command command): run the command while the button is held and interrupt it when it's released
	//button.whenReleased(Command command): run the command once when released
	public OI(){
		clawOpen.whenPressed(new ClawCommand(false));
		clawClose.whenPressed(new ClawCommand(true));
		engageClamp.whenPressed(new ClampCommand(true));
		disengageClamp.whenPressed(new ClampCommand(false));
	}
	public double x(){return drive.getRawAxis(LEFT_X)* 1.25;}
	public double y(){return -drive.getRawAxis(LEFT_Y);}
	public double rotation(){return drive.getRawAxis(RIGHT_X);}
	public int lock(){return drive.getPOV();}
	public double leftArm(){return aux.getRawAxis(LEFT_X);}
	public double rightArm(){return aux.getRawAxis(RIGHT_X);}
	public double leftArmWheels(){return aux.getRawAxis(LEFT_Y);}
	public double rightArmWheels(){return aux.getRawAxis(RIGHT_Y);}
	public double clawElevation(){return aux.getRawAxis(LEFT_Y);}
	public double elevator(){return drive.getRawAxis(RIGHT_TRIGGER) - drive.getRawAxis(LEFT_TRIGGER);}
}

