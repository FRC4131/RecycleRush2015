package org.usfirst.frc.team4131.robot;

import java.util.Calendar;

import org.usfirst.frc.team4131.robot.commands.AutonomousCommand;
import org.usfirst.frc.team4131.robot.oi.Controller;
import org.usfirst.frc.team4131.robot.oi.OI;
import org.usfirst.frc.team4131.robot.subsystems.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot{
	public static final int PDP_ID = 1, PCM_ID = 2;//CAN IDs
	public static OI oi;
	public static DriveBase drive;
	public static Sensors sensors;
	public static Arms arms;
	public static ArmWheels armWheels;
	public static Elevator elevator;
	public static Clamps clamps;
	public static Claw claw;
	public static ClawElevator clawElevator;
	private Command autonomous;
	@Override
	public void robotInit(){
		try{
			drive = new DriveBase(new int[]{3, 4, 5, 6}, new int[]{0, 1, 2, 3, 8, 9, 6, 7});
			sensors = new Sensors(1);
			arms = new Arms(9, 10);
			armWheels = new ArmWheels(11, 12);
			elevator = new Elevator(7, 8, 4, 5, 10, 11);
			clamps = new Clamps(3, 4);
			claw = new Claw(2, 5);
			clawElevator = new ClawElevator(13, 0);
			oi = new OI(0, 1);
			autonomous = new AutonomousCommand();
		}catch(Throwable ex){
			StringBuilder string = new StringBuilder();
			string.append(ex.toString());
			for(StackTraceElement element : ex.getStackTrace()) string.append("\n" + element.toString()); 
			DriverStation.reportError(string.toString(), false);
		}
	}
	
	@Override public void autonomousInit(){autonomous.start();}
	@Override public void autonomousPeriodic(){Scheduler.getInstance().run();}
	
	@Override public void teleopInit(){autonomous.cancel();}
	@Override public void teleopPeriodic(){Scheduler.getInstance().run(); dashboard();}
	private void dashboard(){
		SmartDashboard.putNumber("EL-E", elevator.getEncL());
		SmartDashboard.putNumber("ER-E", elevator.getEncR());
		SmartDashboard.putNumber("EL-ER", elevator.getEncRawL());
		SmartDashboard.putNumber("ER-ER", elevator.getEncRawR());
		SmartDashboard.putNumber("EL-C", elevator.getCurrentL());
		SmartDashboard.putNumber("ER-C", elevator.getCurrentR());
		SmartDashboard.putNumber("EL-V", elevator.getL());
		SmartDashboard.putNumber("ER-V", elevator.getR());
		SmartDashboard.putNumber("OI-EL", oi.elevatorL());
		SmartDashboard.putNumber("OI-ER", oi.elevatorR());
		SmartDashboard.putNumber("Gyro", sensors.gyroAngle());
		SmartDashboard.putBoolean("OI-DO", drive.isDriverOriented());
		for(int i=0;i<4;i++){
			SmartDashboard.putNumber("D" + i + "-EV", drive.getEncoderDistance(i));
			SmartDashboard.putNumber("D" + i + "-ER", drive.getEncoderRate(i));
			SmartDashboard.putNumber("D" + i + "-C", drive.getCurrent(i));
		}
		if(oi.getButton(true, Controller.B)){
			sensors.reset();
			drive.reset();
			elevator.resetL();
			elevator.resetR();
			System.out.println("Reset");
		}
	}
	@Override
	public void testInit(){
		System.out.println("Test");
	}
	@Override
	public void testPeriodic(){
		dashboard();
	}
	
	@Override public void disabledInit(){}
	@Override public void disabledPeriodic(){Scheduler.getInstance().run(); dashboard();}
	
	public static void log(Object logger, String message){
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		int milli = now.get(Calendar.MILLISECOND);
		System.out.println(String.format("LOG (%02d:%02d:%02d.%03d) -> %s: %s", hour, minute, second, milli, logger.getClass().getSimpleName(), message));
	}
}
