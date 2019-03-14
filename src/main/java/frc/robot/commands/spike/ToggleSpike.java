/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.spike;

import frc.robot.subsystems.Spike;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Command;

public class ToggleSpike extends Command {

    private boolean _bool;

    public ToggleSpike() {
        requires(Spike.getInstance());
    }

    @Override
    protected void initialize() {
        if (Spike.getInstance().getRelay() == Value.kForward) {
            _bool = false;
        } else {
            _bool = true;
        }

        Spike.getInstance().setRelay(_bool);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
