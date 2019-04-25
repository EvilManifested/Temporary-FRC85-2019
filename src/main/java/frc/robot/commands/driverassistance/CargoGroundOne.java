package frc.robot.commands.driverassistance;

import frc.robot.Variables;
import frc.robot.commands.intake.ActivateIntake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CargoGroundOne extends CommandGroup {
    
    public CargoGroundOne() {
        addSequential(new Interrupt());

        addSequential(new Place(Variables.CARGO_FLOOR[Variables.getInstance().isPracticeBot()], Variables.WRIST_CARGO_FLOOR[Variables.getInstance().isPracticeBot()], Variables.MAST_FORWARD_POS));
        //addSequential(new ActivateIntake(Variables.ROLLER_IN));
    }

}