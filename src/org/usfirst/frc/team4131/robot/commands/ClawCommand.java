package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClawCommand extends Command{
	private boolean open, hasRun;
	public ClawCommand(boolean open){
		super();
		requires(Robot.claw);
		this.open = open;
	}
	protected void initialize(){
		
		hasRun = false;
		Robot.log(this, "Starting (" + open + ")");
	}
	protected void execute(){
		Robot.claw.setOpen(open);
		hasRun = true;
	}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){Robot.log(this, "Ending");}
	@Override protected void interrupted(){Robot.log(this, "Interrupting");}
}
