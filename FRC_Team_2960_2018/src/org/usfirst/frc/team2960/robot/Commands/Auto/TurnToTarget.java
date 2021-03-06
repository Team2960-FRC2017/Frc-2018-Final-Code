package org.usfirst.frc.team2960.robot.Commands.Auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team2960.robot.Subsytems.Drive;

public class TurnToTarget extends CommandGroup{

    Drive drive = Drive.getInstance();
    private boolean isFinish = false;
    private double angle;
    private double speed;

    public TurnToTarget(double angle, double speed){
        this.angle = angle;
        this.speed = speed;
    }

    @Override
    protected void initialize() {
       drive.zeroSensors();
    }

    /**
     * Starts up the command. Gets the command ready to start. <p> Note that the command will
     * eventually start, however it will not necessarily do so immediately, and may in fact be
     * canceled before initialize is even called. </p>
     *
     *
     */
    @Override
    public synchronized void start() {
        super.start();
    }


    /**
     * Returns true if all the commands in this group have been started and have
     * finished.
     * <p>
     * <p> Teams may override this method, although they should probably reference super.isFinished()
     * if they do. </p>
     *
     * @return whether this {@link CommandGroup} is finished
     */
    @Override
    protected boolean isFinished() {
        if(isFinish) {
            System.out.println("TURN FINISHED");
            return true;
        }
        else
            return false;
    }



    @Override
    protected void execute() {
        isFinish = drive.turnToTarget(angle, speed);
    }

    @Override
    protected void end() {
        super.end();
    }
}
