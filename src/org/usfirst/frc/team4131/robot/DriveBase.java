package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A representation of the drivetrain for the robot.
 * There are four motors with Mecanum wheels, and a quadrature encoder on each wheel.
 * This also contains all of the drive-related automation functions (ie point-here), because otherwise it would be a very empty class.
 */
public class DriveBase{
	private Sensors sensors;
	private CANTalon[] talons = new CANTalon[4];
	private Encoder[] encoders = new Encoder[4];
	private RobotDrive drive;
	private int lock = -1;//[0, 360); keep the robot in this orientation. -1 is unlocked.
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
		if(rot==0 && lock > -1){
			double diff = diff(lock, sensors.gyroAngle());
			SmartDashboard.putNumber("Lock", lock);
			SmartDashboard.putNumber("Diff", diff);
			rot = diff / 90;
		}
		SmartDashboard.putNumber("DR-X", x);
		SmartDashboard.putNumber("DR-Y", y);
		SmartDashboard.putNumber("DR-R", rot);
		drive.mecanumDrive_Cartesian(constrain(x, -1, 1), constrain(-y, -1, 1), constrain(rot, -1, 1), driverOriented ? sensors.gyroAngle() : 0);
	}
	public void stop(){
		for(CANTalon talon : talons) talon.set(0);
		unlock();
	}
	public void lock(double lock){this.lock = (int)wrap(lock, 0, 360);}
	public void unlock(){lock = -1;}
	public int getLock(){return lock;}
	public boolean isLocked(){return lock > -1;}
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
	private double diff(double target, double current){
		double diff = wrap(target, 0, 360) - wrap(current, 0, 360);
		if(Math.abs(diff) > 180) diff = Math.copySign(Math.abs(diff) - 360, -diff);
		return diff;
	}
	
	/**Wrap the value between the minimum and maximum.
	 * This is similar to a modulus action, except the minimum and maximum values can be given.
	 * @param value The value to wrap
	 * @param min The minimum value the number can be, inclusive
	 * @param max The maximum value the number can be, exclusive
	 * @return The wrapped value
	 */
	private static double wrap(double value, double min, double max){
		double range = max - min;
		while(value < min) value += range;
		while(value >= max) value -= range;
		return value;
	}
	/**Constrain the value to the maximum and minimum values provided.
	 * If the value is outside of the provided range, it will be assigned to the nearer limit, ie values below minimum become the minimum
	 * and values above the maximum become the maximum.
	 * @param value The value to constrain
	 * @param min The minimum value, inclusive
	 * @param max The maximum value, inclusive
	 * @return The constrained value
	 */
	private static double constrain(double value, double min, double max){return Math.max(Math.min(value, max), min);}
}
