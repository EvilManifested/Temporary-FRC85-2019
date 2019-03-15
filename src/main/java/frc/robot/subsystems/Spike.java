/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
   
import frc.robot.Addresses;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

public class Spike extends Subsystem {
    
    public static Spike _instance = null;

    private Relay _relay;

    public Spike() {
        _relay = new Relay(Addresses.LIGHT_RELAY, Direction.kForward);
    }

    public static Spike getInstance() {
        if (_instance == null) {
            _instance = new Spike();
        }
        return _instance;
    }

    @Override
	protected void initDefaultCommand() {
    }

    /**
     * @param bool boolean
     * param true activates spike
     * param false deactivates spike
     */
    public void setRelay(boolean bool) {
        if (bool) {
            _relay.set(Relay.Value.kForward);
        } else {
            _relay.set(Relay.Value.kOff);
        }

        if (getRelay()) {
            SmartDashboard.putBoolean("Relay Set", true);
        } else {
            SmartDashboard.putBoolean("Relay Set", false);
        }
    }

    public boolean getRelay() {
        boolean _bool;

        if (_relay.get() == Value.kForward) {
            _bool = true;
        } else {
            _bool = false;
        }

        return _bool;
    }
    
}

  

