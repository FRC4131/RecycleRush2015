package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorResetCommand extends Command{
	private boolean left, right;//Whether each side has been zeroed
	private Timer timerL = new Timer(), timerR = new Timer();//Started when the thing is zeroed out 
	private final double delay = 0.1;//Time, in seconds, to wait for the elevator to bounce back
	public ElevatorResetCommand(){
		super();
		requires(Robot.elevator);
	}
	@Override
	protected void initialize(){
		Robot.log(this, "Starting");
		left = false;
		right = false;
	}
	@Override
	protected void execute(){
		if(!left){
			Robot.elevator.moveLeftDown();
			if(Robot.elevator.getCurrentL() > 1.5){
				left = true;
				Robot.elevator.stopL();
				System.out.println("Left centered");
				timerL.reset();
				timerL.start();
			}
		}
		if(!right){
			Robot.elevator.moveRightDown();
			if(Robot.elevator.getCurrentR() > 1.5){
				right = true;
				Robot.elevator.stopR();
				System.out.println("Right centered");
				timerR.reset();
				timerR.start();
			}
		}
	}
	@Override
	protected boolean isFinished(){
		return left && right && timerL.get() >= delay && timerR.get() >= delay;//Both sides are done and the methods
	}
	@Override
	protected void end(){
		Robot.log(this, "Ending");
		Robot.elevator.resetL();
		Robot.elevator.resetR();
	}
	@Override
	protected void interrupted(){
		Robot.elevator.stop();
		Robot.log(this, "Interrupting");
	}
}
