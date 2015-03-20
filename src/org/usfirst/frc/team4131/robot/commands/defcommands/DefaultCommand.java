package org.usfirst.frc.team4131.robot.commands.defcommands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class DefaultCommand extends Command{
	public DefaultCommand(Subsystem system){super(); requires(system);}
	@Override protected void initialize(){}
	@Override protected boolean isFinished(){return false;}
	@Override protected void interrupted(){end();}
	
}
