package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LockCommand extends Command{
	private static final int CURRENT = -2;//Get the current value rather than a fixed one
	private int lock;
	private boolean hasRun = false;
	public LockCommand(){this(CURRENT);}
	public LockCommand(int lock){super(); this.lock = lock;}
	@Override protected void initialize(){}
	@Override
	protected void execute(){
		if(lock==-1) Robot.drive.unlock();
		else if(lock==CURRENT) Robot.drive.lock((int)Robot.sensors.gyroAngle());
		else Robot.drive.lock(lock);
		hasRun = true;
	}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
}
