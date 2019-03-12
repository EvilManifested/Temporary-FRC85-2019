package frc.robot.commands.driverassistance;

import frc.robot.Variables;
import frc.robot.commands.intake.ActivateIntake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CargoGroundOne extends CommandGroup {
    
    public CargoGroundOne() {
        addSequential(new Interrupt());

        addSequential(new Place(Variables.CARGO_FLOOR, Variables.WRIST_CARGO_FLOOR));
        addSequential(new ActivateIntake(Variables.ROLLER_IN));
    }

}