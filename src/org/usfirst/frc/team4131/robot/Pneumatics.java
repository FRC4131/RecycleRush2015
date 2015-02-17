package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pneumatics{
	private Solenoid[] solenoids = new Solenoid[8];
	private Compressor comp;
	public Pneumatics(int comp){
		this.comp = new Compressor(comp);
		this.comp.setClosedLoopControl(true);
		for(int i=0;i<solenoids.length;i++) solenoids[i] = new Solenoid(i);
	}
	public void loop(){
		for(int i=0;i<solenoids.length;i++){
			solenoids[i].set(true);
			SmartDashboard.putNumber("Solenoid Index", i);
			Timer.delay(0.5);
			solenoids[i].set(false);
		}
	}
}
