package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ArmCommand extends Command{
	double left, right;
	public ArmCommand(double left, double right){
		super();
		requires(Robot.arms);
		this.left = left; this.right = right;
	}
	@Override
	protected void initialize(){
		
	}
	@Override
	protected void execute(){
		Robot.arms.set(left, right);
	}
	@Override
	protected boolean isFinished(){
		return false;
	}
	@Override
	protected void end(){
		
	}
	@Override
	protected void interrupted(){
		
	}
}
