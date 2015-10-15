package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorResetCommand extends Command{
	private boolean leftReset, rightReset;
	public ElevatorResetCommand(){
		super();
		requires(Robot.elevator);
	}
	@Override
	protected void initialize(){
		Robot.log(this, "Starting");
		leftReset = rightReset = false;
	}
	@Override
	protected void execute(){
		if(!leftReset){
			if(Robot.elevator.getLimitL()){
				Robot.elevator.stopL();
				leftReset = true;
				Robot.elevator.resetL();
			}else{
				Robot.elevator.setL(-0.5);
			}
		}
		if(!rightReset){
			if(Robot.elevator.getLimitR()){
				Robot.elevator.stopR();
				rightReset = true;
				Robot.elevator.resetR();
			}else{
				Robot.elevator.setR(-0.5);
			}
		}
	}
	@Override
	protected boolean isFinished(){
		return Robot.elevator.getLimitL() && Robot.elevator.getLimitR();//Both sides are centered
	}
	@Override
	protected void end(){
		Robot.log(this, "Ending");
		Robot.elevator.resetL();
	}
	@Override
	protected void interrupted(){
		Robot.elevator.stop();
		Robot.log(this, "Interrupting");
	}
}
