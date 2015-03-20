package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriverOrientationCommand extends Command{
	private boolean driverOriented;
	public DriverOrientationCommand(boolean driverOriented){
		super();
		this.driverOriented = driverOriented;
	}
	@Override protected void initialize(){}
	@Override
	protected void execute(){Robot.drive.setDriverOriented(driverOriented);}
	
	@Override
	protected boolean isFinished(){
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void end(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void interrupted(){
		// TODO Auto-generated method stub
		
	}
	
}
