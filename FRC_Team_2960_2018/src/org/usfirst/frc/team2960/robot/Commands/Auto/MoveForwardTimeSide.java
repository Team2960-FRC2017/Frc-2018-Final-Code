package org.usfirst.frc.team2960.robot.Commands.Auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.IllegalUseOfCommandException;
import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team2960.robot.Subsytems.Drive;

public class MoveForwardTimeSide extends Command{
    double time;
    double speed;
    boolean done;
    Timer timeToMove;
    boolean side; //True right side false left side

    public MoveForwardTimeSide(double time, double speed, boolean side) {
        super("MoveForwardTime");
        requires(Drive.getInstance());
        this.time = time;
        this.speed = speed;
        done = false;
        timeToMove = new Timer();
        this.side = side;
    }


    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     * <p>
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     * <p>
     * <p>Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Returning true will result in the
     * command executing once and finishing immediately. We recommend using {@link InstantCommand}
     * for this.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        if(timeToMove.get() >= time) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        timeToMove.start();

    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        if(side) {
            Drive.getInstance().setSpeed(0, -speed);
        }
        else{
            Drive.getInstance().setSpeed(speed, 0);
        }
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        Drive.getInstance().setSpeed(0, 0);
        timeToMove.stop();
        timeToMove.reset();
    }

    /**
     * Called when the command ends because somebody called {@link Command#cancel() cancel()} or
     * another command shared the same requirements as this one, and booted it out.
     * <p>
     * <p>This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     * <p>
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method within this
     * method, as done here.
     */
    @Override
    protected void interrupted() {
    }

    /**
     * Starts up the command. Gets the command ready to start. <p> Note that the command will
     * eventually start, however it will not necessarily do so immediately, and may in fact be
     * canceled before initialize is even called. </p>
     *
     * @throws IllegalUseOfCommandException if the command is a part of a CommandGroup
     */
    @Override
    public synchronized void start() {
    }
}
