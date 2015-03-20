package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveCommand extends Command{
	private double start, distance;
	public DriveCommand(double inches){
		super();
		requires(Robot.drive);
		this.distance = inches;
	}
	@Override
	protected void initialize(){
		start = Robot.drive.getEncoderDistance(0);
	}
	@Override
	protected void execute(){
		Robot.drive.drive(0, Math.copySign(0.7, distance), 0, false);
	}
	@Override protected boolean isFinished(){return Math.abs(Robot.drive.getEncoderDistance(0) - start) < Math.abs(distance);}
	@Override protected void end(){Robot.drive.stop();}
	@Override protected void interrupted(){Robot.drive.stop();}
	
}
