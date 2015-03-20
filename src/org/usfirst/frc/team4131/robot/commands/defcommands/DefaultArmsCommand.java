package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultArmsCommand extends DefaultCommand{
	public DefaultArmsCommand(){super(Robot.arms);}
	@Override protected void execute(){Robot.arms.set(Robot.oi.leftArm(), Robot.oi.rightArm());}
	@Override protected void end(){Robot.arms.set(0, 0);}
}
