package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

public class DriveBase{
	private PIDTalon[] talons;
	private RobotDrive drive;
	private Sensors sensors;
	public DriveBase(Sensors sensors, PIDTalon... talons){
		this.talons = talons;
		this.sensors = sensors;
		if(talons.length>=4) drive = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
		else drive = new RobotDrive(talons[0], talons[1]);
		drive.setInvertedMotor(MotorType.kFrontLeft, false);
		drive.setInvertedMotor(MotorType.kFrontRight, false);
		drive.setInvertedMotor(MotorType.kRearLeft, false);
		drive.setInvertedMotor(MotorType.kRearRight, false);
	}
	public void drive(double x, double y, double rotation, boolean driverOriented){
		drive.mecanumDrive_Cartesian(x, -y, rotation, driverOriented ? sensors.gyroAngle() : 0);
	}
	public void stop(){drive(0, 0, 0, false);}
	public PIDTalon getMotor(int index){return talons[index];}
	public PIDTalon[] getMotors(){return talons;}
}
