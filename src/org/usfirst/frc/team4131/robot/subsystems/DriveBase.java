package org.usfirst.frc.team4131.robot.subsystems;

import org.usfirst.frc.team4131.robot.Robot;
import org.usfirst.frc.team4131.robot.commands.defcommands.DefaultDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveBase extends Subsystem{
	private boolean driverOriented = false;
	private int lock = -1;
	private final SpeedController[] motors;
	private final Encoder[] encoders;
	private final RobotDrive drive;
	public DriveBase(int[] motors, int[][] encoders){
		this.motors = new SpeedController[motors.length];
		for(int i=0;i<motors.length;i++) this.motors[i] = new CANTalon(motors[i]);
		this.encoders = new Encoder[encoders.length/2];
		for(int i=0;i<encoders.length;i++) this.encoders[i] = new Encoder(encoders[i][0], encoders[i][1]);
		if(motors.length==4) drive = new RobotDrive(this.motors[0], this.motors[1], this.motors[2], this.motors[3]);
		else drive = new RobotDrive(this.motors[0], this.motors[1]);
	}
	protected void initDefaultCommand(){setDefaultCommand(new DefaultDriveCommand());}
	public void drive(double x, double y, double rot){drive(x, y, rot, driverOriented);}
	public void drive(double x, double y, double rot, boolean driverOriented){
		if(rot==0 && isLocked()){
			double diff = diff(lock, Robot.sensors.gyroAngle());
			rot = diff * 0.7/90;//70% power for every 90 degrees of turn to do
		}
		drive.mecanumDrive_Cartesian(constrain(x, -1, 1), constrain(y, -1, 1), constrain(rot, -1, 1),
				driverOriented ? Robot.sensors.gyroAngle() : 0);
	}
	public SpeedController getMotor(int index){return motors[index];}
	public double getEncoderDistance(int index){return encoders[index].getDistance();}
	public double getEncoderRate(int index){return encoders[index].getRate();}
	public void stop(){
		for(SpeedController motor : motors) motor.set(0);
		unlock();
	}
	public void reset(){for(Encoder encoder : encoders) encoder.reset();}
	public void lock(int dir){
		while(dir < 0) dir += 360;
		while(dir >= 360) dir -= 360;
		lock = dir;
	}
	public void unlock(){lock = -1;}
	public int getLock(){return lock;}
	public boolean isLocked(){return lock > -1;}
	public void setDriverOriented(boolean driverOriented){this.driverOriented = driverOriented;}
	public boolean isDriverOriented(){return driverOriented;}
	
	private static double diff(double target, double current){
		double diff = wrap(target, 0, 360) - wrap(current, 0, 360);
		if(Math.abs(diff) > 180) diff = Math.copySign(Math.abs(diff) - 360, -diff);
		return diff;
	}
	private static double wrap(double value, double min, double max){
		double range = max - min;
		while(value < min) value += range;
		while(value >= max) value -= range;
		return value;
	}
	private static double constrain(double value, double min, double max){return Math.max(Math.min(value, max), min);}
}

