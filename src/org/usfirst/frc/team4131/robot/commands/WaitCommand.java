package org.usfirst.frc.team4131.robot.commands;

import org.usfirst.frc.team4131.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class WaitCommand extends Command{
	private double delay;
	private Timer timer = new Timer();
	public WaitCommand(double delay){
		super();
		this.delay = delay;
	}
	@Override
	protected void initialize(){
		timer.reset();
		timer.start();
		Robot.log(this, "Beginning (" + delay + ")");
	}
	@Override protected void execute(){}
	@Override protected boolean isFinished(){return timer.get() >= delay;}
	@Override protected void end(){timer.stop(); Robot.log(this, "Ending");}
	@Override protected void interrupted(){timer.stop(); Robot.log(this, "Interrupting");}
}
