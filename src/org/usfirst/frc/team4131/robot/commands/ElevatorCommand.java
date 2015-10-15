package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;
import org.usfirst.frc.team4131.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorCommand extends Command{
	private static final int tolerance = 0;
	private final int target;
	private boolean leftSet, rightSet;
	public ElevatorCommand(int target){
		super();
		requires(Robot.elevator);
		if(target < 0) this.target = 0;
		else if(target > Elevator.ENCODER_MAX) this.target = Elevator.ENCODER_MAX;
		else this.target = target;
	}
	@Override
	protected void initialize(){
		leftSet = rightSet = false;
	}
	@Override
	protected void execute(){
		if(!leftSet){
			if(Math.abs(Robot.elevator.getEncoderL() - target) <= tolerance){
				leftSet = true;
				Robot.elevator.stopL();
			}else{
				Robot.elevator.setL(Math.copySign(0.75, target - Robot.elevator.getEncoderL()));
			}
		}
		if(!rightSet){
			if(Math.abs(Robot.elevator.getEncoderR() - target) <= tolerance){
				rightSet = true;
				Robot.elevator.stopR();
			}else{
				Robot.elevator.setR(target - Robot.elevator.getEncoderR());
			}
		}
	}
	@Override protected boolean isFinished(){return leftSet && rightSet;}
	@Override protected void end(){Robot.elevator.stop();}
	@Override protected void interrupted(){Robot.elevator.stop();}
}
