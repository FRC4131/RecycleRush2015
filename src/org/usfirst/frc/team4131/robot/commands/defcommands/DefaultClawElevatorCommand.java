package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultClawElevatorCommand extends DefaultCommand{
	public DefaultClawElevatorCommand(){super(Robot.clawElevator);}
	@Override protected void execute(){Robot.clawElevator.set(Robot.oi.clawElevation());}
	@Override protected void end(){Robot.clawElevator.set(0);}
}
