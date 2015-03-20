
package org.usfirst.frc.team4131.robot;

import org.usfirst.frc.team4131.robot.commands.AutonomousCommand;
import org.usfirst.frc.team4131.robot.oi.OI;
import org.usfirst.frc.team4131.robot.subsystems.*;

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
	public static final int PDP_ID = 1, PCM_ID = 2;
	public static OI oi = new OI();
	public static final DriveBase drive = new DriveBase(new int[]{3, 4, 5, 6}, new int[][]{{0, 1}, {2, 3}, {8, 9}, {6, 7}});
	public static final Sensors sensors = new Sensors(1);
	public static final Arms arms = new Arms(9, 10);
	public static final ArmWheels armWheels = new ArmWheels(11, 12);
	public static final Elevator elevator = new Elevator(7, 8, 10, 11, 12, 13);
	public static final Clamps clamps = new Clamps(4, 3);
	public static final Claw claw = new Claw(2, 5);
	public static final ClawElevator clawElevator = new ClawElevator(13, 0);
	
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
