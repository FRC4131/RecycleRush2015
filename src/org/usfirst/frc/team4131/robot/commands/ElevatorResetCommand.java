package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorResetCommand extends Command{
	private boolean left = false, right = false;//Whether each side has been zeroed
	public ElevatorResetCommand(){
		super();
		requires(Robot.elevator);
	}
	@Override protected void initialize(){}
	@Override
	protected void execute(){
		if(!left){
			Robot.elevator.setL(-0.5);
			if(Robot.elevator.getCurrentL() > 7){
				left = true;
				Robot.elevator.setL(0);
			}
		}
		if(!right){
			Robot.elevator.setR(-0.5);
			if(Robot.elevator.getCurrentR() > 7){
				right = true;
				Robot.elevator.setR(0);
			}
		}
	}
	@Override protected boolean isFinished(){return left && right;}
	@Override
	protected void end(){
		Robot.elevator.resetL();
		Robot.elevator.resetR();
	}
	@Override
	protected void interrupted(){
		Robot.elevator.set(0);
	}
}
