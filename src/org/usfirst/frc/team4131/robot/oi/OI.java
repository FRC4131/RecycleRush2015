package org.usfirst.frc.team4131.robot.oi;

import static org.usfirst.frc.team4131.robot.oi.Controller.A;
import static org.usfirst.frc.team4131.robot.oi.Controller.B;
import static org.usfirst.frc.team4131.robot.oi.Controller.LEFT_BUMPER;
import static org.usfirst.frc.team4131.robot.oi.Controller.LEFT_TRIGGER;
import static org.usfirst.frc.team4131.robot.oi.Controller.LEFT_X;
import static org.usfirst.frc.team4131.robot.oi.Controller.LEFT_Y;
import static org.usfirst.frc.team4131.robot.oi.Controller.MENU;
import static org.usfirst.frc.team4131.robot.oi.Controller.RIGHT_BUMPER;
import static org.usfirst.frc.team4131.robot.oi.Controller.RIGHT_TRIGGER;
import static org.usfirst.frc.team4131.robot.oi.Controller.RIGHT_X;
import static org.usfirst.frc.team4131.robot.oi.Controller.X;
import static org.usfirst.frc.team4131.robot.oi.Controller.Y;

import org.usfirst.frc.team4131.robot.commands.ClampCommand;
import org.usfirst.frc.team4131.robot.commands.ClawCommand;
import org.usfirst.frc.team4131.robot.commands.ElevatorCommand;
import org.usfirst.frc.team4131.robot.commands.ElevatorResetCommand;
import org.usfirst.frc.team4131.robot.subsystems.Clamps;
import org.usfirst.frc.team4131.robot.subsystems.Claw;
import org.usfirst.frc.team4131.robot.subsystems.Elevator;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI{
	private Controller drive, aux;
	public OI(int drivePort, int auxPort){
		drive = new Controller(drivePort);
		aux = new Controller(auxPort);
		aux.button(Y).onPress(new ClawCommand(Claw.OPEN));
		aux.button(A).onPress(new ClawCommand(Claw.CLOSE));
		aux.button(X).onPress(new ClampCommand(Clamps.ENGAGED));
		aux.button(B).onPress(new ClampCommand(Clamps.DISENGAGED));
		drive.button(MENU).onPress(new ElevatorResetCommand());
		new POVButton(drive, 0).whenPressed(new ElevatorCommand(Elevator.ENCODER_MAX));
		new POVButton(drive, 180).whenPressed(new ElevatorResetCommand());
	}
	public double x(){return removeDeadband(drive.getRawAxis(LEFT_X)) * 1.25;}
	public double y(){return removeDeadband(drive.getRawAxis(LEFT_Y));}
	public double rotation(){return removeDeadband(drive.getRawAxis(RIGHT_X));}
	public double intake(){return removeDeadband(aux.getRawAxis(LEFT_Y));}
	public double elevator(){
		if(drive.getButton(LEFT_BUMPER) || drive.getButton(RIGHT_BUMPER)) return -1;
		else return Math.max(drive.getAxis(LEFT_TRIGGER), drive.getAxis(RIGHT_TRIGGER));
	}
	public double clawElevation(){return aux.getPOV() == 0 ? 1 : aux.getPOV() == 180 ? -1 : 0;}
	
	public boolean getButton(boolean controller, int button){return (controller ? drive : aux).getButton(button);}
	private static double removeDeadband(double raw){return (Math.abs(raw)) > 0.09 ? raw : 0;}
}

