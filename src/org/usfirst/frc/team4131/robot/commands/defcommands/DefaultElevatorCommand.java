package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultElevatorCommand extends DefaultCommand{
	public DefaultElevatorCommand(){super(Robot.elevator);}
	@Override
	protected void execute(){
		Robot.elevator.setL(Robot.oi.elevatorL());
		Robot.elevator.setR(Robot.oi.elevatorR());
	}
	@Override protected void end(){Robot.elevator.stop();}
}
