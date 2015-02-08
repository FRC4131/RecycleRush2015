package org.usfirst.frc.team4131.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;

public class PIDTalon extends CANTalon{
	private static final double CIRCUMFERENCE = 25.1327;//Circumference of each wheel
	private static List<PIDTalon> motors = new ArrayList<PIDTalon>();//List of all motors assigned
	private Encoder encoder;
	private boolean invMotor, invEnc;
	private double prevValue = 0;//Previous encoder value
	private double ratio = 1;//Distance per power unit
	private double mult = 1;//Multiplier set by equalizer
	public PIDTalon(int motor, int enc1, int enc2, double ticks, boolean invMotor, boolean invEnc){
		super(motor);
		enableBrakeMode(false);
		encoder = new Encoder(enc1, enc2);
		encoder.setDistancePerPulse(CIRCUMFERENCE/ticks);
		this.invMotor = invMotor;
		this.invEnc = invEnc;
		motors.add(this);
	}
	@Override
	public void set(double speed){
		super.set((invMotor ? -1 : 1) * speed);// * mult);
		if(speed!=0) ratio = Math.abs((encoder.getDistance()-prevValue) / speed);//Distance per power unit; constant for any power set
		prevValue = encoder.getDistance();
	}
	public double getDistance(){return (invEnc ? -1 : 1) * encoder.getDistance();}
	public void reset(){encoder.reset();}
	public boolean isStopped(){return encoder.getStopped();}
	public boolean getDirection(){return invEnc != encoder.getDirection();}//If inverted, flip direction
	public static void equalize(){
		double mean = 0;
		for(PIDTalon motor : motors) mean+=motor.ratio;
		mean /= motors.size();//This is now the average power sent to each motor.
		for(PIDTalon motor : motors){
			motor.mult = mean / motor.ratio;
			/*SmartDashboard.putNumber("Ratio " + motor.getDeviceID(), motor.ratio);
			SmartDashboard.putNumber("Multiplier " + motor.getDeviceID(), motor.mult);
			SmartDashboard.putNumber("Distance " + motor.getDeviceID(), motor.getDistance());
			SmartDashboard.putString("Direction " + motor.getDeviceID() , motor.isStopped() ? "Stopped" : motor.getDirection() ? "Forward" : "Backward");*/
		}
		//If a motor is going faster than the others, its ratio will be higher than the mean and its multiplier will be less than 1.
		//If a motor is slow, its ratio will be lower, and its multiplier will be greater than 1.
	}
}
