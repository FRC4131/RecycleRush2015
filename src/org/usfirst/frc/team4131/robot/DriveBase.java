package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

public class DriveBase{
	private static final double ramp = 0.60;//Amount to ramp up or down by in each tick
	private Sensors sensors;
	private CANTalon[] talons = new CANTalon[4];
	private Encoder[] encoders = new Encoder[4];
	private RobotDrive drive;
	private int lockDir = -1;//0-360; keep the robot in this orientation. -1 is unlocked.
	private double x, y, rot;//Used for ramping
	public DriveBase(Sensors sensors, int[] motors, int[] encoders){
		this.sensors = sensors;
		for(int i=0;i<motors.length;i++){
			talons[i] = new CANTalon(motors[i]);
			talons[i].enableBrakeMode(true);
			this.encoders[i] = new Encoder(encoders[2*i], encoders[2*i + 1]);
			this.encoders[i].setDistancePerPulse(Math.PI * 8 / 250);//8" wheels (r^2=16), 250 pulses/rev
		}
		drive = new RobotDrive(talons[0], talons[1], talons[2], talons[3]);
	}
	public void drive(double x, double y, double rot, boolean driverOriented){
		x=-x; rot=-rot;
		if(lockDir > -1) rot = (lockDir - constrain(sensors.gyroAngle())) / 90;
		x = Math.min(Math.max(x, -1), 1);
		y = Math.min(Math.max(y, -1), 1);
		rot = Math.min(Math.max(rot, -1), 1);
		drive.mecanumDrive_Cartesian(this.x + ramp*(x-this.x), this.y + ramp*(y-this.y), this.rot + ramp*(rot-this.rot),
				driverOriented ? sensors.gyroAngle() : 0);
		this.x = x; this.y = y; this.rot = rot;
	}
	public void stop(){
		for(CANTalon talon : talons) talon.set(0);
		unlock();
	}
	public void lock(int dir){lockDir = dir;}
	public void unlock(){lockDir = -1;}
	public int getLock(){return lockDir;}
	public CANTalon getMotor(int index){return talons[index];}
	public CANTalon[] getMotors(){return talons;}
	public boolean isStopped(int index){return encoders[index].getStopped();}
	public boolean getDirection(int index){return encoders[index].getDirection() != index<2;}//Invert on left side (index<2)
	public double getDistance(int index){
		if(index == -1) return (getDistance(0) + getDistance(1) + getDistance(2) + getDistance(3))/4; 
		return encoders[index].getDistance() * (index<2 ? -1 : 1);
	}
	public double getRate(int index){return encoders[index].getRate() * (index<2 ? -1 : 1);}
	public void reset(){for(Encoder encoder : encoders) encoder.reset();}
	private double constrain(double raw){return raw - 360*Math.floor(raw/360);}//Constrain to [0, 360)
}
