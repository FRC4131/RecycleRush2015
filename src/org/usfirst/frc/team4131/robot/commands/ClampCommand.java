package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClampCommand extends Command{
	private boolean engaged, hasRun = false;
	public ClampCommand(boolean engaged){
		super();
		requires(Robot.clamps);
		this.engaged = engaged;
	}
	@Override protected void initialize(){}
	@Override protected void execute(){Robot.clamps.set(engaged); hasRun = true;}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
	
}
