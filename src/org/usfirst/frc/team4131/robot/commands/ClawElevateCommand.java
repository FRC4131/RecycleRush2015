package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;
import org.usfirst.frc.team4131.robot.subsystems.Claw;

import edu.wpi.first.wpilibj.command.Command;

public class ClawElevateCommand extends Command{
	private double target;
	public ClawElevateCommand(double target){
		super();
		requires(Robot.clawElevator);
		this.target = Math.max(0, Math.min(Claw.HEIGHT, target));
	}
	@Override protected void initialize(){}
	@Override
	protected void execute(){
		Robot.clawElevator.set(target);
	}
	@Override protected boolean isFinished(){return Math.abs(Robot.clawElevator.get() - target) < 1;}//1" tolerance on either side
	@Override protected void end(){Robot.clawElevator.set(0);}
	@Override protected void interrupted(){Robot.clawElevator.set(0);}
}
