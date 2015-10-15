package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultIntakeCommand extends DefaultCommand{
	public DefaultIntakeCommand(){super(Robot.intake);}
	@Override protected void execute(){Robot.intake.set(Robot.oi.intake());}
	@Override protected void end(){Robot.intake.set(0);}
}
