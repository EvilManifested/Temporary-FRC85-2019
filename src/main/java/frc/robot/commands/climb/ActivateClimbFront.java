/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climb;

import frc.robot.subsystems.ClimbFront;

import edu.wpi.first.wpilibj.command.Command;

public class ActivateClimbFront extends Command {

    private double _speed, _timeout;

    public ActivateClimbFront(double speed, double seconds) {
        requires(ClimbFront.getInstance());
        _speed = speed;
        _timeout = seconds;
    }

    @Override
    protected void initialize() {
       setTimeout(_timeout); 
    }

    @Override
    protected void execute() {
        ClimbFront.getInstance().setClimbFrontMotors(_speed);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        ClimbFront.getInstance().setClimbFrontMotors(0.0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}