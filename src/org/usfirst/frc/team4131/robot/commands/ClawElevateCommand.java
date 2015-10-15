package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ClawElevateCommand extends Command{
	private double speed, time;
	private Timer timer = new Timer();
	public ClawElevateCommand(double speed, double time){
		super();
		requires(Robot.clawElevator);
		this.speed = speed; this.time = time;
	}
	@Override
	protected void initialize(){
		timer.start();
		timer.reset();
		Robot.log(this, "Starting (" + speed + ", " + time + ")");
	}
	@Override
	protected void execute(){
		Robot.clawElevator.set(speed);
	}
	@Override protected boolean isFinished(){return timer.get() >= time;}
	@Override
	protected void end(){
		Robot.clawElevator.set(0);
		timer.stop();
		Robot.log(this, "Ending");
	}
	@Override
	protected void interrupted(){
		Robot.clawElevator.set(0);
		timer.stop();
		Robot.log(this, "Interrupting");
	}
}
