package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultArmWheelsCommand extends DefaultCommand{
	public DefaultArmWheelsCommand(){super(Robot.armWheels);}
	@Override protected void execute(){Robot.armWheels.set(Robot.oi.leftArmWheels(), Robot.oi.rightArmWheels());}
	@Override protected void end(){Robot.armWheels.set(0, 0);}
}
