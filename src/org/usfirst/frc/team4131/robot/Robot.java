package org.usfirst.frc.team4131.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot implements Runnable{
	private OI oi = new OI(0, 1);
	private Sensors sensors = new Sensors(0, 0, 1, 2);
	private DriveBase drive = new DriveBase(sensors, new int[]{1, 2, 3, 4}, new int[]{0, 1, 2, 3, 8, 9, 6, 7});//LF, LB, RF, RB
	private Arms arms = new Arms(1, 2, 3, 4);
//	private Claw claw = new Claw(6, 2, 5, 3, 4, 3, 2, 3, 4, 5);
	private Conveyor conveyor = new Conveyor(0, 1);
	private Elevator elevator = new Elevator(6, 2, 5, 0, 4, 5);
	private boolean driverOriented = true;
	public Robot(){new Thread(this).start();}
	@Override
	public void autonomous(){
		Thread thread = new Thread(){public void run(){try{
			drive.unlock();
			strafe(75);
			move(49);
			strafe(44);
			turnTo(24);//arctan(23/52)
			move(Math.sqrt(23*23 + 52*52));
			turnTo(-39);//arctan(-39/48)
			move(Math.sqrt(39*39 + 48*48));
			turnTo(0);
			move(43);
			strafe(-42);
			move(-61);
			drive.stop();
		}catch(InterruptedException ex){}}};
		thread.start();
		while(isEnabled() && isAutonomous()) Timer.delay(0.005);
		thread.interrupt();
		drive.stop();
	}
	@Override
	public void operatorControl(){
//		elevator.engage();
		/*---------------NOT CURRENTLY ATTACHED---------------*\
		claw.setOpen(Boolean.FALSE);
		\*----------------------------------------------------*/
		drive.unlock();
		while(isOperatorControl() && isEnabled()){
			if(oi.toggleLock()){
				if(drive.isLocked()) drive.unlock(); else drive.lock(sensors.gyroAngle());
			}else if(oi.getPOV()>-1) drive.lock(oi.getPOV());
			if(oi.toggleDriverOrientation()) driverOriented = !driverOriented;
			drive.drive(oi.getX(), oi.getY(), oi.getRotation(), driverOriented);
			conveyor.set(oi.getConveyorSpeed());
			arms.squeeze(oi.leftArm(), oi.rightArm());
			arms.rollIn(oi.leftArmWheels(), oi.rightArmWheels());
			/*---------------NOT CURRENTLY ATTACHED---------------*\
			claw.setOpen(oi.getClaw());
			claw.elevate(oi.getClawElevation());
			claw.rotate(oi.getClawRotation());
			\*----------------------------------------------------*/
//			if(oi.liftElevator()) elevator.lift();
//			else if(oi.dropElevator()) elevator.drop();
//			else elevator.stop();
//			if(oi.engageClamp()) elevator.engage();
//			else if(oi.disengageClamp()) elevator.disengage();
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
			SmartDashboard.putNumber("OI-X", oi.getX());
			SmartDashboard.putNumber("OI-Y", oi.getY());
			SmartDashboard.putNumber("OI-R", oi.getRotation());
			SmartDashboard.putNumber("Lock", drive.getLock());
			SmartDashboard.putBoolean("Driver-Oriented", driverOriented);
			if(oi.resetSensors()){
				sensors.reset();
				drive.reset();
				drive.stop();
			}
			Timer.delay(0.005);
		}
	}
	private void move(double inches) throws InterruptedException{
		SmartDashboard.putString("Phase", "Move " + inches);
		double start = drive.getDistance(0);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(0) - start)) > 15){
			drive.drive(0, Math.copySign(0.3, inches), 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
	}
	private void turnBy(double angle) throws InterruptedException{
		turnTo(angle + sensors.gyroAngle());
	}
	private void turnTo(double angle) throws InterruptedException{
		SmartDashboard.putString("Phase", "Turn to " + angle);
		drive.lock(angle);
		while(Math.abs(angle - sensors.gyroAngle()) > 1){
			drive.drive(0, 0, 0, false);//Let the rotation lock have its way
			SmartDashboard.putNumber("Lock?", drive.getLock());
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
		drive.unlock();
	}
	private void strafe(double inches) throws InterruptedException{
		SmartDashboard.putString("Phase", "Strafe " + inches);
		inches *= 1.25;//Account for slip
		drive.lock(sensors.gyroAngle());//Because our center of gravity is behind the center of our bot's volume, the back wheels
		//Do more of the work than the front. While strafing, because the front wheels are against the back, the robot will turn, because
		//the back wheels have more influence over the robot's direction. By locking the direction, it counteracts this turn.
		double start = drive.getDistance(3);
		double diff;
		while(Math.abs(diff = inches - (drive.getDistance(3) - start)) > 1){
			drive.drive(Math.copySign(0.3, inches), 0, 0, false);
			SmartDashboard.putNumber("Diff", diff);
			if(Thread.interrupted()) throw new InterruptedException();
			Timer.delay(0.005);
		}
		drive.unlock();
	}
}
