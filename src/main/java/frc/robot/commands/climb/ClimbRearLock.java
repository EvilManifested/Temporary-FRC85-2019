/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import frc.robot.Variables;
import frc.robot.sensors.Sensors;
import frc.robot.subsystems.ClimbRear;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This system of using a clutch to stop the elevator from moving exploits the FRC system of command based.
 * Each subsystem (this is a command, whose subsystem is the elevator) maybe only have one command activate a time, calling a new command interrupts the old command.
 * By setting this as the default command of elevator, when there is no other command sceduled this command will be called.
 * When the command is called, it initalizes and activates the locking mechanism.
 * When the command is interrupted (a command to move the lift), interrupted() runs, unlocking the lift.
 * When the movement command finishes, this command is called again (since it is the default command).
 */
public class ClimbRearLock extends Command {

    double _timeout;

    public ClimbRearLock() {
        requires(ClimbRear.getInstance());
        _timeout = 0;
    }

    public ClimbRearLock(double timeout) {
        requires(ClimbRear.getInstance());
        _timeout = timeout;
        setTimeout(_timeout);
    }

    @Override
    protected void initialize() {
        ClimbRear.getInstance().setServo(Variables.getInstance().getClimbUnlocked());
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override 
    protected void end() {
        ClimbRear.getInstance().setServo(Variables.getInstance().getClimbUnlocked());
        ClimbRear.getInstance().setClimbRearDriveMotor(0.0);
    }

    @Override
    protected void interrupted() {
        ClimbRear.getInstance().setServo(Variables.getInstance().getClimbUnlocked());
        //ClimbRear.getInstance().setClimbRearDriveMotor(0.0);
    }

}
