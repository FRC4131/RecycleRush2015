package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private OI oi = new OI(0, 1);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 8, 9, 6, 7});//LF, LB, RF, RB
	private Conveyor conveyor = new Conveyor(0, 1);
//	private Arms arms = new Arms(1, 2, 4, 5);
//	private Claw claw = new Claw(6, 2, 5, 3, 4, 3, 2, 3, 4, 5);
	private Elevator elevator = new Elevator(6, 2, 5, 0, 4, 5);
	public Robot(){new Thread(this).start();}
	@Override
	public void autonomous(){
		Thread thread = new Thread(){public void run(){try{
//			claw.elevate(-1.0);//Full speed down
//			claw.setOpen(Boolean.FALSE);
//			claw.elevate(1.0);
//			arms.squeeze(1, 1);
			turn(180);
			drive.stop();
		}catch(InterruptedException ex){}}};
		thread.start();
		while(isEnabled() && isAutonomous()) Timer.delay(0.005);
		if(thread.isAlive()) thread.interrupt();
		drive.stop();
	}
	@Override
	public void operatorControl(){
		elevator.engage();
		//claw.setOpen(Boolean.FALSE);
		while(isOperatorControl() && isEnabled()){
			if(oi.unlockDrive()) drive.unlock(); else if(oi.getPOV()>-1) drive.lock(oi.getPOV());
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), true);
			conveyor.set(oi.getConveyorSpeed());
//			arms.squeeze(oi.leftArm(), oi.rightArm());
//			arms.rollIn(oi.leftArmWheels(), oi.rightArmWheels());
//			claw.setOpen(oi.getClaw());
//			claw.elevate(oi.getClawElevation());
//			claw.rotate(oi.getClawRotation());
			if(oi.liftElevator()) elevator.lift();
			else if(oi.dropElevator()) elevator.drop();
			else elevator.stop();
			if(oi.engageClamp()) elevator.engage();
			else if(oi.disengageClamp()) elevator.disengage();
			/*if(elevator.getSwitch1()){
				conveyor.set(0);
				elevator.lift();
			}
			if(elevator.getSwitch2()){
				elevator.engage();
				elevator.drop();
			}
			if(oi.disengageClamp()) elevator.disengage();*/
			Timer.delay(0.005);
		}
	}
	@Override
	public void test(){
		while(isTest() && isEnabled()){
			drive.unlock();
			int[] buttons = new int[]{OI.Y, OI.X, OI.B, OI.A};
			for(int i=0;i<buttons.length;i++) drive.getMotor(i).set(oi.getButton(true, buttons[i]) ? -0.2 : 0);
			Timer.delay(0.005);
		}
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
			SmartDashboard.putBoolean("Switch 1", elevator.getSwitch1());
			SmartDashboard.putBoolean("Switch 2", elevator.getSwitch2());
			if(oi.resetSensors()){
				sensors.reset();
				drive.reset();
			}
			Timer.delay(0.005);
		}
	}
	private void move(double inches) throws InterruptedException{
		SmartDashboard.putString("Phase", "Move " + inches);
		double start = drive.getDistance(1);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(1) - start)) > 15){
			drive.drive(0, Math.copySign(0.3, inches), 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private void turn(int angle) throws InterruptedException{
		SmartDashboard.putString("Phase", "Turn " + angle);
		drive.lock(angle - (int)sensors.gyroAngle());
		while(Math.abs(angle - sensors.gyroAngle()) > 1){
			drive.drive(0, 0, 0, false);//Let the rotation lock have its way
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
		drive.unlock();
	}
	private void strafe(double inches) throws InterruptedException{
		SmartDashboard.putString("Phase", "Strafe " + inches);
		double start = drive.getDistance(0);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(0) - start)) > 15){
			drive.drive(Math.copySign(0.3, inches), 0, 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
}
