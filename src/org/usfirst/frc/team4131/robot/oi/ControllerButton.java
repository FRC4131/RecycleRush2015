package org.usfirst.frc.team4131.robot.oi;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

public class ControllerButton extends Button{
	private final Controller controller;
	private final int button;
	public ControllerButton(Controller controller, int button){super(); this.controller = controller; this.button = button;}
	@Override public boolean get(){return controller.getButton(button);}
	public ControllerButton onPress(Command command){whenPressed(command); return this;}
	public ControllerButton whilePressed(Command command){whilePressed(command); return this;}
	public ControllerButton onRelease(Command command){whenReleased(command); return this;}
	
}
