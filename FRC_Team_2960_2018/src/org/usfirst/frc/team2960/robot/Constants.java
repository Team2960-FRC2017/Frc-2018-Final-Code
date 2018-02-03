package org.usfirst.frc.team2960.robot;

import org.usfirst.frc.team2960.Util.ConstantsBase;

public class Constants extends ConstantsBase {

    //Talon Ids
    public static int mRightMasterId = 0;
    public static int mRightSlave1Id = 1;
    public static int mRightSlave2Id = 2;
    public static int mLeftMasterId = 3;
    public static int mLeftSlave1Id = 4;
    public static int mLeftSlave2Id = 5;
    public static int mWinchMasterId = 6;
    public static int mWinchSlaveId = 7;
    public static int mElevatorMasterId = 8;
    public static int mElevatorSlaveId = 9;
    public static int mIntakeMasterId = 10;
    public static int mIntakeSlaveId = 11;
    public static int mHookId = 12;

    // TODO: 1/17/18 Change wheel diameter to 6 when we get the real robot
    // Robot Contraints
    public static double wheelDiameter = 4;
    public static double inchPerRotation = wheelDiameter * Math.PI;
    public static double inchPerTick = inchPerRotation / 4096;

    //Talon Constants
    public static int kPIDLoopIDx = 0;
    public static int kTimeoutMs = 10;
    public static int kSlotIdx = 0;

    public static int kCruiseVelocity = 10000; //Sensor UnitPer100ms
    public static int kAcceleration = 3000; //Sensor UnitPer100ms

    //DIO (Digital In And Out)
    public static int kBottomPhotoeyeId = 0;
    public static int kTopPhotoeyeId = 1;
    public static int mUltrasonicRight1In = 2;
    public static int mUltrasonicRight1Out = 3;
    public static int mUltrasonicRight2In = 4;
    public static int mUltrasonicRight2Out = 5;
    public static int mUltrasonicLeft1In = 6;
    public static int mUltrasonicLeft1Out = 7;
    public static int mUltrasonicLeft2In = 8;
    public static int mUltrasonicLeft2Out = 9;

    //Analog
    public static int mUltrasonicFront = 1;

}