package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriverOrientationCommand extends Command{
	private boolean driverOriented, hasRun = false;
	public DriverOrientationCommand(boolean driverOriented){
		super();
		this.driverOriented = driverOriented;
	}
	public DriverOrientationCommand(){this(!Robot.drive.isDriverOriented());}//Toggle driver orientation
	@Override protected void initialize(){}
	@Override protected void execute(){Robot.drive.setDriverOriented(driverOriented); hasRun = true;}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
}
