package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TurnCommand extends Command{
	private double target;
	public TurnCommand(double degrees, boolean absolute){
		super();
		requires(Robot.drive);
		if(absolute) target = degrees; else target = degrees + Robot.sensors.gyroAngle();
	}
	@Override protected void initialize(){}
	@Override
	protected void execute(){
		double diff = target - Robot.sensors.gyroAngle();
		if(diff > 5) Robot.drive.drive(0, 0, 0.7);
		else if(diff < -5) Robot.drive.drive(0, 0, -0.7);
		else Robot.drive.stop();
	}
	@Override protected boolean isFinished(){return Math.abs(target - Robot.sensors.gyroAngle()) < 5;}
	@Override protected void end(){Robot.drive.stop();}
	@Override protected void interrupted(){Robot.drive.stop();}
	
}
