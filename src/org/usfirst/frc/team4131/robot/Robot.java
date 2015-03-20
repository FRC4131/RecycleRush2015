
package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.commands.AutonomousCommand;
import org.usfirst.frc.team4131.robot.oi.OI;
import org.usfirst.frc.team4131.robot.subsystems.ArmWheels;
import org.usfirst.frc.team4131.robot.subsystems.Arms;
import org.usfirst.frc.team4131.robot.subsystems.Clamps;
import org.usfirst.frc.team4131.robot.subsystems.Claw;
import org.usfirst.frc.team4131.robot.subsystems.ClawElevator;
import org.usfirst.frc.team4131.robot.subsystems.DriveBase;
import org.usfirst.frc.team4131.robot.subsystems.Elevator;
import org.usfirst.frc.team4131.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot{
	public static OI oi = new OI();
	public static final DriveBase drive = new DriveBase(new int[]{1, 2, 3, 4}, new int[][]{{0, 1}, {2, 3}, {8, 9}, {6, 7}});
	public static final Sensors sensors = new Sensors(1);
	public static final Arms arms = new Arms(3, 5);
	public static final ArmWheels armWheels = new ArmWheels(4, 6);
	public static final Elevator elevator = new Elevator(7);
	public static final Clamps clamps = new Clamps(6, 4, 3);
	public static final Claw claw = new Claw(6, 2, 5);
	public static final ClawElevator clawElevator = new ClawElevator(3, 0);
	
	private Command autonomous = new AutonomousCommand();
	@Override public void robotInit(){}
	
	@Override public void autonomousInit(){autonomous.start();}
	@Override public void autonomousPeriodic(){Scheduler.getInstance().run();}
	
	@Override public void teleopInit(){autonomous.cancel();}
	@Override public void teleopPeriodic(){Scheduler.getInstance().run();}
	
	@Override public void testInit(){}
	@Override public void testPeriodic(){LiveWindow.run();}
	
	@Override public void disabledPeriodic(){Scheduler.getInstance().run();}
	@Override public void disabledInit(){}
}
