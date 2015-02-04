package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class DriveBase{
	private PIDTalon[] motors;
	private RobotDrive drive;
	private Gyro gyro = new Gyro(1);
	public DriveBase(PIDTalon... motors){
		this.motors = motors;
		if(motors.length>=4) drive = new RobotDrive(motors[0], motors[1], motors[2], motors[3]);
		else drive = new RobotDrive(motors[0], motors[1]);
		drive.setInvertedMotor(MotorType.kFrontLeft, false);
		drive.setInvertedMotor(MotorType.kFrontRight, false);
		drive.setInvertedMotor(MotorType.kRearLeft, false);
		drive.setInvertedMotor(MotorType.kRearRight, false);
	}
	public void drive(double x, double y, double rotation){
		drive.mecanumDrive_Cartesian(x, y, rotation, (int)Robot.sensors.getAngle());
		PIDTalon.equalize();
	}
	public void drive(double speed, double rotation){
		drive.arcadeDrive(speed, rotation);
		PIDTalon.equalize();
	}
	public double getAngle(){return gyro.getAngle();}
	public PIDTalon getMotor(int index){return motors[index];}
}
