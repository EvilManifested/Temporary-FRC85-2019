package frc.robot.commands;

import frc.robot.commands.belttrain.BeltTrainDrive;
import frc.robot.commands.drivetrain.DriveSeconds;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SendItBro extends CommandGroup {

    double _speed[] = {0.0, -0.5, 0.0, 0.0};

    // i guess this is what we are doing for future reference

    public SendItBro() {
        addSequential(new Wait(0.3));
        addParallel(new DriveSeconds(_speed, 5.0));
        addSequential(new BeltTrainDrive(.5, 5.0));
    }
    
}