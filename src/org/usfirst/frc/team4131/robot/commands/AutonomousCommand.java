package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.subsystems.Claw;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup{
	public  AutonomousCommand(){
		addParallel(new ElevatorResetCommand());
		addParallel(new ClawCommand(false));
		addParallel(new ClawElevateCommand(Claw.HEIGHT));
		addSequential(new TurnCommand(180, false));
		
		addParallel(new ArmCommand(0.5, 0.5));
		addSequential(new ArmWheelCommand(1, 1));
		
		addSequential(new TurnCommand(-90, false));
		
		addSequential(new DriveCommand(66));
	}
}
