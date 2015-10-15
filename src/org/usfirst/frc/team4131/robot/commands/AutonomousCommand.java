package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.subsystems.Claw;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup{
	public AutonomousCommand(){
		addSequential(new LogCommand(this, "Begin Command Group I"));
		addSequential(new ClawCommand(Claw.CLOSE));
//		addParallel(new ElevatorResetCommand());
		addSequential(new ClawElevateCommand(1, 2));
		
//		addSequential(new ClawElevateCommand(1, 4));
//		addSequential(new DriveCommand(1200));
//		addSequential(new DriveCommand(92));
	}
	/*public  AutonomousCommand(){
		addParallel(new ElevatorResetCommand());
		addParallel(new ClawCommand(false));
		addParallel(new ClawElevateCommand(Claw.HEIGHT));
		addSequential(new TurnCommand(180, false));
		
		addParallel(new ArmCommand(0.5, 0.5));
		addSequential(new ArmWheelCommand(1, 1));
		
		addSequential(new TurnCommand(-90, false));
		
		addSequential(new DriveCommand(66));
	}*/
	/*public AutonomousCommand(){
		addParallel(new ElevatorResetCommand());
		addSequential(new TurnCommand(90, false));
		addSequential(new DriveCommand(66));
	}*/
}
