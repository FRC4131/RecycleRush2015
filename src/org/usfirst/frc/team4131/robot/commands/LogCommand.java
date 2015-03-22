package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LogCommand extends Command{
	private Object parent;
	private String message;
	private boolean hasRun;
	public LogCommand(Object parent, String message){
		super();
		this.parent = parent;
		this.message = message;
	}
	@Override protected void initialize(){hasRun = false;}
	@Override protected void execute(){Robot.log(parent, message); hasRun = true;}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
}
