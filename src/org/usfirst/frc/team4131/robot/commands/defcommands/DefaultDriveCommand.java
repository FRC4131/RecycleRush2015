package org.usfirst.frc.team4131.robot.commands.defcommands;

import org.usfirst.frc.team4131.robot.Robot;

public class DefaultDriveCommand extends DefaultCommand{
	public DefaultDriveCommand(){super(Robot.drive);}
	protected void execute(){Robot.drive.drive(Robot.oi.x(), Robot.oi.y(), Robot.oi.rotation());}
	protected void end(){Robot.drive.stop();}
}
