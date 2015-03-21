package org.usfirst.frc.team4131.robot.oi;

import edu.wpi.first.wpilibj.buttons.Button;

public class POVButton extends Button{
	private Controller controller;
	private int dir;
	public POVButton(Controller controller, int dir){super(); this.controller = controller; this.dir = dir;}
	@Override public boolean get(){return controller.getPOV() == dir;}
}
