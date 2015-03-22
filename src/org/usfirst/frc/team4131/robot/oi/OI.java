package org.usfirst.frc.team4131.robot.oi;

import static org.usfirst.frc.team4131.robot.oi.Controller.*;

import org.usfirst.frc.team4131.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI{
	private Controller drive, aux;
	//button.onPress(Command command): run the command once when pressed
	//button.whileHeld(Command command): run the command while the button is held and interrupt it when it's released
	//button.whenReleased(Command command): run the command once when released
	public OI(int drivePort, int auxPort){
		drive = new Controller(drivePort);
		aux = new Controller(auxPort);
		/*clawOpen = */aux.button(Y).onPress(new ClawCommand(true));
		/*clawClose = */aux.button(A).onPress(new ClawCommand(false));
		/*engageClamp = */aux.button(X).onPress(new ClampCommand(true));
		/*disengageClamp = */aux.button(B).onPress(new ClampCommand(false));
//		/*lock = */new POV(drive).onPress(new LockCommand());
		/*unlock = */drive.button(LEFT_BUMPER).onPress(new LockCommand(-1));
		drive.button(SCREEN_SELECT).onPress(new ToggleSyncCommand());
		drive.button(B).onPress(new SensorResetCommand());
		drive.button(MENU).onPress(new ElevatorResetCommand());
	}
	public double x(){return removeDeadband(drive.getRawAxis(LEFT_X)) * 1.25;}
	public double y(){return -removeDeadband(drive.getRawAxis(LEFT_Y));}
	public double rotation(){return removeDeadband(drive.getRawAxis(RIGHT_X));}
	public int lock(){return drive.getPOV();}
	public double leftArm(){return removeDeadband(aux.getRawAxis(LEFT_X));}
	public double rightArm(){return removeDeadband(aux.getRawAxis(RIGHT_X));}
	public double leftArmWheels(){return removeDeadband(aux.getRawAxis(LEFT_Y));}
	public double rightArmWheels(){return removeDeadband(aux.getRawAxis(RIGHT_Y));}
	public double elevator(){
		if(drive.getButton(LEFT_BUMPER) || drive.getButton(RIGHT_BUMPER)) return -1;
		else return Math.max(drive.getAxis(LEFT_TRIGGER), drive.getAxis(RIGHT_TRIGGER));
	}
	public double elevatorL(){
		if(ToggleSyncCommand.isSynced()) return elevator(); else
		return drive.getButton(LEFT_BUMPER) ? -1 : removeDeadband(drive.getAxis(LEFT_TRIGGER));
	}
	public double elevatorR(){
		if(ToggleSyncCommand.isSynced()) return elevator(); else
		return drive.getButton(RIGHT_BUMPER) ? -1 : removeDeadband(drive.getAxis(RIGHT_TRIGGER));
	}
	public double clawElevation(){return aux.getPOV() == 0 ? 1 : aux.getPOV() == 180 ? -1 : 0;}
	public boolean getButton(boolean controller, int button){return (controller ? drive : aux).getButton(button);}
	
	private static double removeDeadband(double raw){return (Math.abs(raw)) > 0.09 ? raw : 0;}
}

