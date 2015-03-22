package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleSyncCommand extends Command{
	private static boolean isSynced = false;
	private Boolean target, hasRun;
	public ToggleSyncCommand(Boolean driverOriented){
		super();
		this.target = driverOriented;
	}
	public ToggleSyncCommand(){this(null);}//Toggle it
	@Override protected void initialize(){hasRun = false;}
	@Override
	protected void execute(){
		if(target == null)
//			Robot.drive.setDriverOriented(!Robot.drive.isDriverOriented());
			isSynced = !isSynced;
		else
//			Robot.drive.setDriverOriented(driverOriented);
			isSynced = target;
		hasRun = true;
	}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
	public static boolean isSynced(){return isSynced;}
}
