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
//		elevator.engage();
		/*---------------NOT CURRENTLY ATTACHED---------------*\
		claw.setOpen(Boolean.FALSE);
		\*----------------------------------------------------*/
		drive.unlock();
		while(isOperatorControl() && isEnabled()){
			if(oi.unlock()) drive.unlock();
			else if(oi.getPOV()>-1) drive.lock(oi.getPOV());
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), driverOriented);
			conveyor.set(oi.getConveyorSpeed());
			arms.squeeze(oi.leftArm(), oi.rightArm());
			arms.rollIn(oi.leftArmWheels(), oi.rightArmWheels());
			/*---------------NOT CURRENTLY ATTACHED---------------*\
			claw.setOpen(oi.getClaw());
			claw.elevate(oi.getClawElevation());
			claw.rotate(oi.getClawRotation());
			\*----------------------------------------------------*/
			elevator.setElevator(oi.getElevator());
			if(oi.engageClamp()) elevator.engage();
			else if(oi.disengageClamp()) elevator.disengage();
			/*---------------NOT CURRENTLY ATTACHED---------------*\
			if(elevator.getSwitch1()){
				conveyor.set(0);
				elevator.lift();
			}
			if(elevator.getSwitch2()){
				elevator.engage();
				elevator.drop();
			}
			if(oi.disengageClamp()) elevator.disengage();
			\*----------------------------------------------------*/
			Timer.delay(0.005);
		}
	}
	@Override
	public void test(){
		drive.stop();
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
			SmartDashboard.putNumber("OI-X", oi.getX());
			SmartDashboard.putNumber("OI-Y", oi.getY());
			SmartDashboard.putNumber("OI-R", oi.getRotation());
			SmartDashboard.putNumber("Lock", drive.getLock());
			SmartDashboard.putBoolean("Driver-Oriented", driverOriented);
			SmartDashboard.putNumber("Conveyor", oi.getConveyorSpeed());
			if(oi.resetSensors()){
				sensors.reset();
				drive.reset();
				drive.stop();
			}
			if(oi.toggleDriverOrientation()) driverOriented = !driverOriented;
			Timer.delay(0.005);
		}
	}
}
