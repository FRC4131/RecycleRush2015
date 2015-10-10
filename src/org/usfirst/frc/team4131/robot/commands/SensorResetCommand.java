package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class SensorResetCommand extends Command{
	private boolean hasRun;
	@Override protected void initialize(){hasRun = false;}
	@Override protected void execute(){
		Robot.sensors.reset();
		Robot.drive.reset();
		hasRun = true;
		DriverStation.reportError("Reset\n", false);
	}
	@Override protected boolean isFinished(){return hasRun;}
	@Override protected void end(){}
	@Override protected void interrupted(){}
}
