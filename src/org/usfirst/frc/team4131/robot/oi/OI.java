package org.usfirst.frc.team4131.robot.oi;

import static org.usfirst.frc.team4131.robot.oi.Controller.*;

import org.usfirst.frc.team4131.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI{
	private Controller drive, aux;
//	private Button clawOpen, clawClose, engageClamp, disengageClamp, lock, unlock, driverOriented, resetSensors;
	//button.onPress(Command command): run the command once when pressed
	//button.whileHeld(Command command): run the command while the button is held and interrupt it when it's released
	//button.whenReleased(Command command): run the command once when released
	public OI(int drivePort, int auxPort){
		drive = new Controller(drivePort);
		aux = new Controller(auxPort);
		/*clawOpen = */aux.button(Y).onPress(new ClawCommand(false));
		/*clawClose = */aux.button(A).onPress(new ClawCommand(true));
		/*engageClamp = */aux.button(X).onPress(new ClampCommand(true));
		/*disengageClamp = */aux.button(B).onPress(new ClampCommand(false));
//		/*lock = */new POV(drive).onPress(new LockCommand());
		/*unlock = */drive.button(LEFT_BUMPER).onPress(new LockCommand(-1));
		/*driverOriented = */drive.button(SCREEN_SELECT).onPress(new DriverOrientationCommand());
		/*resetSensors = */drive.button(B).onPress(new SensorResetCommand());
	}
	public double x(){return removeDeadband(drive.getRawAxis(LEFT_X)) * 1.25;}
	public double y(){return -removeDeadband(drive.getRawAxis(LEFT_Y));}
	public double rotation(){return removeDeadband(drive.getRawAxis(RIGHT_X));}
	public int lock(){return drive.getPOV();}
	public double leftArm(){return removeDeadband(aux.getRawAxis(LEFT_X));}
	public double rightArm(){return removeDeadband(aux.getRawAxis(RIGHT_X));}
	public double leftArmWheels(){return removeDeadband(aux.getRawAxis(LEFT_Y));}
	public double rightArmWheels(){return removeDeadband(aux.getRawAxis(RIGHT_Y));}
	public double elevator(){return removeDeadband(drive.getRawAxis(RIGHT_TRIGGER) - drive.getRawAxis(LEFT_TRIGGER));}
	public double clawElevation(){return drive.getPOV() == 0 ? 0.5 : drive.getPOV() == 180 ? -0.5 : 0;}
	public boolean getButton(boolean controller, int button){return (controller ? drive : aux).getButton(button);}
	
	private static double removeDeadband(double raw){return (Math.abs(raw)) > 0.09 ? raw : 0;}
}

