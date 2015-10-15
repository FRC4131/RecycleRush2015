package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultElevatorCommand extends DefaultCommand{
	public DefaultElevatorCommand(){super(Robot.elevator);}
	@Override
	protected void execute(){
		Robot.elevator.set(Robot.oi.elevator());
	}
	@Override protected void end(){Robot.elevator.stop();}
}
