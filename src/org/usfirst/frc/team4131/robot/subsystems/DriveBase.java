package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveBase extends Subsystem{
	private final CANTalon[] motors;
	private final RobotDrive drive;
	public DriveBase(int[] motors){
		this.motors = new CANTalon[motors.length];
		for(int i=0;i<motors.length;i++) this.motors[i] = new CANTalon(motors[i]);
		drive = new RobotDrive(this.motors[0], this.motors[1], this.motors[2], this.motors[3]);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultDriveCommand());}
	public void drive(double x, double y, double rot){
		drive.mecanumDrive_Cartesian(x, y, rot,  0);
	}
	public CANTalon getMotor(int index){return motors[index];}
	public double getCurrent(int index){return motors[index].getOutputCurrent();}
	public void stop(){
		for(SpeedController motor : motors) motor.set(0);
	}
//	private static double constrain(double value, double min, double max){return Math.max(Math.min(value, max), min);}
}

