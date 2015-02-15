package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveBase{
	private Sensors sensors;
	private CANTalon[] talons = new CANTalon[4];
	private Encoder[] encoders = new Encoder[4];
	private RobotDrive drive;
	public DriveBase(Sensors sensors, int[] motors, int[] encoders){
		this.sensors = sensors;
		for(int i=0;i<motors.length;i++){
			talons[i] = new CANTalon(motors[i]);
			talons[i].enableBrakeMode(true);
			this.encoders[i] = new Encoder(encoders[2*i], encoders[2*i + 1]);
			this.encoders[i].setDistancePerPulse(Math.PI * 16 / 250);//8" wheels (r^2=16), 250 pulses/rev
		}
		drive = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
	}
	public void drive(double x, double y, double rotation, boolean driverOriented){
		drive.mecanumDrive_Cartesian(x, -y, rotation, driverOriented ? sensors.gyroAngle() : 0);
	}
	public void stop(){drive(0, 0, 0, false);}
	public CANTalon getMotor(int index){return talons[index];}
	public CANTalon[] getMotors(){return talons;}
	public boolean isStopped(int index){return encoders[index].getStopped();}
	public boolean getDirection(int index){return encoders[index].getDirection() != index<2;}//Invert on left side (index<2)
	public double getDistance(int index){return encoders[index].getDistance() * (index<2 ? -1 : 1);}
	public double getRate(int index){return encoders[index].getRate() * (index<2 ? -1 : 1);}
	public void reset(){for(Encoder encoder : encoders) encoder.reset();}
}
