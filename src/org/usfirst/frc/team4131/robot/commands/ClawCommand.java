package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClawCommand extends Command{
	private boolean target;
	public ClawCommand(boolean open){
		super();
		requires(Robot.claw);
		this.target = open;
	}
	protected void initialize(){}
	protected void execute(){
		Robot.claw.setOpen(target);
	}

	@Override
	protected boolean isFinished(){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end(){
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted(){
		// TODO Auto-generated method stub
		
	}
}
