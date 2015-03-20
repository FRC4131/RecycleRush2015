package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ArmWheelCommand extends Command{
	private double left, right;
	private Timer timer = new Timer();
	public ArmWheelCommand(double left, double right){
		super();
		requires(Robot.armWheels);
		this.left = left; this.right = right;
	}
	@Override
	protected void initialize(){
		timer.start();
	}
	@Override
	protected void execute(){
		Robot.armWheels.set(left, right);
	}
	@Override protected boolean isFinished(){return timer.get() > 5;}
	@Override protected void end(){Robot.armWheels.set(0, 0);}
	@Override protected void interrupted(){Robot.armWheels.set(0, 0);}
}
