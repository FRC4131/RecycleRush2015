package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorResetCommand extends Command{
	private boolean leftReset, rightReset;//Whether each side has been zeroed
	public ElevatorResetCommand(){
		super();
		requires(Robot.elevator);
	}
	@Override
	protected void initialize(){
		Robot.log(this, "Starting");
		leftReset = false;
		rightReset = false;
	}
	@Override
	protected void execute(){
		if(!leftReset){
			Robot.elevator.moveLeftDown();
//			if(Robot.elevator.getCurrentL() > 1.5){
			if(Robot.elevator.getLimitL()){
				leftReset = true;
				Robot.elevator.stopL();
				System.out.println("Left centered");
			}
		}
		if(!rightReset){
			Robot.elevator.moveRightDown();
//			if(Robot.elevator.getCurrentR() > 1.5){
			if(Robot.elevator.getLimitR()){
				rightReset = true;
				Robot.elevator.stopR();
				System.out.println("Right centered");
			}
		}
	}
	@Override
	protected boolean isFinished(){
		return leftReset && rightReset;//Both sides are centered
	}
	@Override
	protected void end(){
		Robot.log(this, "Ending");
//		Robot.elevator.resetL();
//		Robot.elevator.resetR();
	}
	@Override
	protected void interrupted(){
		Robot.elevator.stop();
		Robot.log(this, "Interrupting");
	}
}
