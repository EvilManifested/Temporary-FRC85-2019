package frc.robot.commands.driverassistance;

import frc.robot.Variables;
import frc.robot.commands.intake.WristPosition;
import frc.robot.commands.lift.ElevatorPosition;
import frc.robot.commands.lift.MastPosition;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class HatchGroundOne extends CommandGroup {
    
    public HatchGroundOne() {
        addSequential(new Interrupt());

        //addSequential(new Place(Variables.HATCH_FLOOR, Variables.WRIST_HATCH_FLOOR, Variables.MAST_CURRENT_POS));
        addSequential(new MastPosition(Variables.MAST_FORWARD_POS));
        addSequential(new ElevatorPosition(Variables.HATCH_FLOOR));
        addSequential(new WristPosition(Variables.WRIST_HATCH_FLOOR));
    }

}