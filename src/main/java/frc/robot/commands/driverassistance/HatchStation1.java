package frc.robot.commands.driverassistance;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Variables;
import frc.robot.commands.drivetrain.FollowTwoTarget;
import frc.robot.commands.intake.WristPosition;
import frc.robot.commands.lift.ElevatorPosition;
import frc.robot.commands.drivetrain.DriveSeconds;
import frc.robot.commands.drivetrain.FollowOneTarget;
import frc.robot.commands.intake.ActivateIntake;
import frc.robot.commands.driverassistance.Wait;

//Sequence of commands used for loading hatch panels from station
public class HatchStation1 extends CommandGroup {

    
    public HatchStation1() {
        addSequential(new Interrupt());
        addSequential(new ElevatorPosition(Variables.getInstance().HATCH_ONE));
        addSequential(new WristPosition(15)); //15 degrees for now
    }
}

