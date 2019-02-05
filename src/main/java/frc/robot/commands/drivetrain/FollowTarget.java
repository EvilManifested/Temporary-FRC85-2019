package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.OI;
import frc.robot.Vision;
import frc.robot.subsystems.DriveTrain;

public class FollowTarget extends Command {
    private double[] _stop = new double[] {0, 0, 0, 0};

    private boolean _targetFound;
    
    public FollowTarget() {
        requires(DriveTrain.getInstance());
    }
    @Override
    protected void initialize() {
        //SmartDashboard.putNumber("kPVision", 0.0);
        //SmartDashboard.putNumber("kIVision", 0.0);
        //SmartDashboard.putNumber("kDVision", 0.0);

        super.initialize();
    }

    @Override
    protected void execute() {
        super.execute();
        double xSpeed, ySpeed, zRotation;
        double targetDistance = 60.0;//SmartDashboard.getNumber("Target Distance", 0.0);
        double targetCenter = 0.0;//SmartDashboard.getNumber("Target Center", 0.0);
        double kPVision = 0.1; // SmartDashboard.getNumber("kPVision", 0.0);
        double kIVision = 0.0;  //SmartDashboard.getNumber("kIVision", 0.0);
        double kDVision = 0.09;  //SmartDashboard.getNumber("kDVision", 0.0);
        double kPVisionRot = 0.1, kIVisionRot = 0.0, kDVisionRot = 0.0;

        xSpeed = OI.getInstance().applyPID(OI.getInstance().VISION_X_SYSTEM, Vision.getInstance().centerX(), targetCenter, kPVision, kIVision, kDVision, .25, -.25);
        ySpeed = OI.getInstance().applyPID(OI.getInstance().VISION_Y_SYSTEM, Vision.getInstance().distance(), targetDistance, kPVision, kIVision, kDVision, .25, -.25);
        zRotation = OI.getInstance().applyPID(OI.getInstance().VISION_ROT_SYSTEM, Vision.getInstance().getAreaDifference(), 0.0, kPVisionRot, kIVisionRot, kDVisionRot, .25, -.25); //we'll see if this works better?

        SmartDashboard.putNumber("Rotation For Vision", zRotation); // this and the other pid value shit will help us out significantly

        double[] _speedArray = {-xSpeed, ySpeed, zRotation, 0};
        DriveTrain.getInstance().cartDrive(_speedArray);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public synchronized void cancel() { //I think that this is called when the command is cancelled
        super.cancel();
        DriveTrain.getInstance().cartDrive(_stop);
    }

    @Override
    protected void end() {
        super.end();
        DriveTrain.getInstance().cartDrive(_stop);
    }
}