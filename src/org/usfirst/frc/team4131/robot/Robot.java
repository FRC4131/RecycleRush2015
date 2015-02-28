package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private OI oi = new OI(0, 1);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 8, 9, 6, 7});//LF, LB, RF, RB
	private Arms arms = new Arms(3, 5, 4, 6);
//	private Claw claw = new Claw(6, 2, 5, 3, 4, 3, 2, 3, 4, 5);
	private Conveyor conveyor = new Conveyor(0, 1, 2);
	private Elevator elevator = new Elevator(6, 4, 3, 7, 3);
	private Autonomous autonomous = new Autonomous(this, sensors, drive);
	private boolean driverOriented = true;
	public Robot(){new Thread(this).start();}
	@Override
	public void autonomous(){
		autonomous.run();
	}
	@Override
	public void operatorControl(){
		drive.unlock();
		arms.squeeze(0, 0);
		arms.rollIn(0, 0);
		conveyor.set(0);
		elevator.engageClamp();
		/*-----NOT CURRENTLY ATTACHED-----*\
		claw.close();
		\*--------------------------------*/
		while(isOperatorControl() && isEnabled()){
			if(oi.unlock()) drive.unlock();
			else if(oi.lock()>-1) drive.lock(oi.lock());
			drive.drive(oi.x(), oi.y(), oi.rotation(), driverOriented);
			arms.squeeze(oi.leftArm(), oi.rightArm());
			arms.rollIn(oi.leftArmWheels(), oi.rightArmWheels());
			if(oi.conveyorOverride()){
				conveyor.set(0.28);
				elevator.disengageClamp();
			}else{
				conveyor.set(oi.conveyorSpeed());
				if(oi.engageClamp()) elevator.engageClamp();
				else if(oi.disengageClamp()) elevator.disengageClamp();
			}
			elevator.setElevator(oi.elevator());
			/*-----NOT CURRENTLY ATTACHED-----*\
			if(oi.clawOpen()) claw.open(); else if(oi.clawClose()) claw.close();
			claw.elevate(oi.clawElevation());
			claw.rotate(oi.clawRotation());
			\*--------------------------------*/
			Timer.delay(0.005);
		}
	}
	@Override
	public void test(){
		drive.stop();//Let the compressor charge
	}
	@Override
	public void run(){
		while(true){
			SmartDashboard.putNumber("AccX", sensors.getAcceleration('x'));
			SmartDashboard.putNumber("AccY", sensors.getAcceleration('y'));
			SmartDashboard.putNumber("AccZ", sensors.getAcceleration('z'));
			SmartDashboard.putNumber("Sonar", sensors.sonarIn());
			SmartDashboard.putNumber("Gyro", sensors.gyroAngle());
			SmartDashboard.putNumber("Temp (C)", sensors.tempC());
			SmartDashboard.putNumber("Temp (F)", sensors.tempF());
			for(int i=0;i<drive.getMotors().length;i++){
				SmartDashboard.putNumber("Encoder V " + i, drive.getDistance(i));
				SmartDashboard.putNumber("Encoder R " + i, drive.getRate(i));
				SmartDashboard.putString("Encoder D " + i, drive.isStopped(i) ? "Stopped" : drive.getDirection(i) ? "Forward" : "Backward");
				SmartDashboard.putNumber("Current " + i, drive.getMotor(i).getOutputCurrent());
			}
			SmartDashboard.putNumber("Battery", DriverStation.getInstance().getBatteryVoltage());
			SmartDashboard.putNumber("Elevator Pot", elevator.getPot());
			SmartDashboard.putNumber("OI-X", oi.x());
			SmartDashboard.putNumber("OI-Y", oi.y());
			SmartDashboard.putNumber("OI-R", oi.rotation());
			SmartDashboard.putNumber("Lock", drive.getLock());
			SmartDashboard.putBoolean("Driver-Oriented", driverOriented);
			SmartDashboard.putNumber("Conveyor", oi.conveyorSpeed());
			if(oi.resetSensors()){
				sensors.reset();
				drive.reset();
				drive.stop();
			}
			if(oi.unlock()) drive.unlock();
			if(oi.driverOrientation()) driverOriented = !driverOriented;
			Timer.delay(0.005);
		}
	}
}
