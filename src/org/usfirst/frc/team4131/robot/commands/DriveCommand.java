package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		Robot.log(this, "Starting (" + distance + ", " + start + " -> " + (distance + start) + ")");
	}
	@Override
	protected void execute(){
		Robot.drive.drive(0, Math.copySign(0.7, distance), 0, false);
		Robot.log(this, "Diff: " + dist());
		SmartDashboard.putNumber("Diff", dist());
	}
	@Override protected boolean isFinished(){return dist() >= Math.abs(distance);}
	@Override protected void end(){Robot.drive.stop(); Robot.log(this, "Ending");}
	@Override protected void interrupted(){Robot.drive.stop(); Robot.log(this, "Interrupting");}
	
	private double dist(){return Math.abs(Robot.drive.getEncoderDistance(0) - start);}
}
