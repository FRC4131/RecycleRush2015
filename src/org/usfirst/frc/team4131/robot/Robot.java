package org.usfirst.frc.team4131.robot;

import java.util.Calendar;

import org.usfirst.frc.team4131.robot.commands.AutonomousCommand;
import org.usfirst.frc.team4131.robot.oi.OI;
import org.usfirst.frc.team4131.robot.subsystems.Clamps;
import org.usfirst.frc.team4131.robot.subsystems.Claw;
import org.usfirst.frc.team4131.robot.subsystems.ClawElevator;
import org.usfirst.frc.team4131.robot.subsystems.DriveBase;
import org.usfirst.frc.team4131.robot.subsystems.Elevator;
import org.usfirst.frc.team4131.robot.subsystems.Intake;
import org.usfirst.frc.team4131.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
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
	public static Intake intake;
	public static Elevator elevator;
	public static Clamps clamps;
	public static Claw claw;
	public static ClawElevator clawElevator;
	private AutonomousCommand autonomous;
	@Override
	public void robotInit(){
		try{
			drive = new DriveBase(new int[]{3, 4, 5, 6});
			sensors = new Sensors(1);
			intake = new Intake(11, 12);
			elevator = new Elevator(7, 8, 0, 1, 2, 3, 4, 5);
			clamps = new Clamps(0, 1);
			claw = new Claw(2, 3);
			clawElevator = new ClawElevator(13/*, 0*/);
			oi = new OI(0, 1);
			autonomous = new AutonomousCommand();
		}catch(Throwable ex){
			StringBuilder string = new StringBuilder();
			string.append(ex.toString());
			for(StackTraceElement element : ex.getStackTrace()) string.append("\n" + element.toString()); 
			DriverStation.reportError(string.toString(), false);
		}
	}
	
	@Override public void autonomousInit(){autonomous.start(); log(this, "Autonomous");}
	@Override public void autonomousPeriodic(){Scheduler.getInstance().run();}
	
	@Override public void teleopInit(){autonomous.cancel(); log(this, "TeleOp");}
	@Override public void teleopPeriodic(){Scheduler.getInstance().run(); dashboard();}
	private void dashboard(){
		SmartDashboard.putNumber("Gyro", sensors.gyroAngle());
		//Drive
		SmartDashboard.putNumber("OI-DX", oi.x());
		SmartDashboard.putNumber("OI-DY", oi.y());
		SmartDashboard.putNumber("OI-DR", oi.rotation());
		for(int i=0; i<4; i++){
			SmartDashboard.putNumber("D" + i + "-S", drive.getMotor(i).get());
			SmartDashboard.putNumber("D" + i + "-C", drive.getCurrent(i));
		}
		//Intake
		SmartDashboard.putNumber("OI-IS", oi.intake());
		SmartDashboard.putNumber("I-S", intake.get());
		SmartDashboard.putNumber("I-CL", intake.getCurrentL());
		SmartDashboard.putNumber("I-CR", intake.getCurrentR());
		//Elevator
		SmartDashboard.putNumber("OI-E", oi.elevator());
		SmartDashboard.putNumber("EL-E", elevator.getEncoderL());
		SmartDashboard.putNumber("ER-E", elevator.getEncoderR());
		SmartDashboard.putNumber("EL-C", elevator.getCurrentL());
		SmartDashboard.putNumber("ER-C", elevator.getCurrentR());
		SmartDashboard.putNumber("EL-S", elevator.getL());
		SmartDashboard.putNumber("ER-S", elevator.getR());
		SmartDashboard.putBoolean("EL-LS", elevator.getLimitL());
		SmartDashboard.putBoolean("ER-LS", elevator.getLimitR());
		//Clamps
		SmartDashboard.putBoolean("C-E", clamps.get());
		//Claw
		SmartDashboard.putBoolean("C-O", claw.getOpen());
		SmartDashboard.putNumber("OI-CE", oi.clawElevation());
		SmartDashboard.putNumber("CE-V", clawElevator.get());
//		SmartDashboard.putBoolean("CE-LS", clawElevator.getLimit());
	}
	@Override
	public void testInit(){
		log(this, "Test");
	}
	@Override public void testPeriodic(){dashboard();}
	
	@Override public void disabledInit(){log(this, "Disabled");}
	@Override public void disabledPeriodic(){dashboard();}
	
	public static void log(Object logger, String message){
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		int milli = now.get(Calendar.MILLISECOND);
		System.out.println(String.format("LOG (%02d:%02d:%02d.%03d) -> %s: %s", hour, minute, second, milli, logger.getClass().getSimpleName(), message));
	}
}
