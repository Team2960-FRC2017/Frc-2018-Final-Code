package org.usfirst.frc.team2960.robot.Commands.Auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team2960.robot.Commands.ElevatorMove;
import org.usfirst.frc.team2960.robot.Commands.IntakeMove;
import org.usfirst.frc.team2960.robot.Subsytems.Elevator;
import org.usfirst.frc.team2960.robot.Subsytems.Intake;

public class RightScaleAutoFarSide extends CommandGroup {

    public RightScaleAutoFarSide(){

        addSequential(new IntakeAdjustMove(), .1);
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        addSequential(new MoveForwardTime(.1, -.5));
        addParallel(new ElevatorMove(Elevator.mElevatorState.ScaleBalanced, 0), 5);
        if (gameData.charAt(1) == 'R') {
            addSequential(new MoveForwardDistance(296, 1));
            addSequential(new TurnToTarget(-90, .5));
            addSequential(new IntakeAdjustMove(), .75);
            addSequential(new IntakeMove(Intake.mIntakeState.backward), 1.5);
        }
        else if(gameData.charAt(1) == 'L'){
            addSequential(new MoveForwardDistance(210, 1));
            addSequential(new TurnToTarget(-90, .5));
            addSequential(new MoveForwardDistance(228, 1));
            addSequential(new TurnToTarget(90, .5));
            addSequential(new MoveForwardDistance(80, 1));
            addSequential(new TurnToTarget(90, .5));
            addSequential(new IntakeAdjustMove(), .75);
            addSequential(new IntakeMove(Intake.mIntakeState.backward), 1.5);
        }
        else {
            addSequential(new MoveForwardDistance(210, 1));
        }
    }
}
