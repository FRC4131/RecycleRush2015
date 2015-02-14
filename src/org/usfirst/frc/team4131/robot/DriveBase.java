package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveBase{
	private Sensors sensors;
	private CANTalon[] talons = new CANTalon[4];
	private RobotDrive drive;
	public DriveBase(Sensors sensors, int leftFront, int leftBack, int rightFront, int rightBack){
		this.sensors = sensors;
		talons[0] = new CANTalon(leftFront);
		talons[1] = new CANTalon(leftBack);
		talons[2] = new CANTalon(rightFront);
		talons[3] = new CANTalon(rightBack);
		drive = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
	}
	public void drive(double x, double y, double rotation, boolean driverOriented){
		drive.mecanumDrive_Cartesian(x, -y, rotation, driverOriented ? sensors.gyroAngle() : 0);
	}
}
