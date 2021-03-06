package org.usfirst.frc.team2960.robot.Subsytems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
import org.usfirst.frc.team2960.robot.Constants;
import org.usfirst.frc.team2960.robot.Pid.TurnPidOutput;


/**
 * Class for controlling the drive motors on our robot
 * @author Malcolm Machesky
 * @author Alex Bolejack
 *
 * Includes 6 mRight Master on Talon SRX's for controlling the drive train through a WCP SS Gearbox
 *
 */
public class Drive extends Subsystem implements SubsystemBase {


    boolean range = false;

    private static Drive m_Instance;

    /**
     * The NavX private instance
     */
    private AHRS navX;

    // Talons
    private TalonSRX mRightMaster, mRightSlave1, mRightSlave2, mLeftMaster, mLeftSlave1, mLeftSlave2;

    /**
     * Ultrasonic Sensors, all but front are vex sensors
     */
    private Ultrasonic mUltraRight1, mUltraRight2, mUltraLeft1, mUltraLeft2;
    //private AnalogInput mUltraFront;

    /**
     * The array for the Ultrasonic Sensors
     */

    private Ultrasonic[] mUltrasonics;

    private PIDController turnPidController;
    private PIDOutput turnPidOutput;

    private Elevator elevator;


    private boolean isTurning = false;
    private boolean isMoving = false;

    /**
     * Private constructor for Drive Class
     */
    private Drive()
    {
        navX = new AHRS(SPI.Port.kMXP);
        turnPidOutput = new TurnPidOutput(this);
        elevator = Elevator.getInstance();
        //Talons
        setupTalons();
        zeroSensors();
        //NavX


        //Ultrasonic setup
        /*
        mUltraRight1 = new Ultrasonic(Constants.mUltrasonicRight1Out, Constants.mUltrasonicRight1In);
        mUltraRight2 = new Ultrasonic(Constants.mUltrasonicRight2Out, Constants.mUltrasonicRight2In);
        mUltraLeft1 = new Ultrasonic(Constants.mUltrasonicLeft1Out, Constants.mUltrasonicLeft1In);
        mUltraLeft2 = new Ultrasonic(Constants.mUltrasonicLeft2Out, Constants.mUltrasonicLeft2In);

        mUltrasonics = new Ultrasonic[]{mUltraRight1, mUltraRight2, mUltraLeft1, mUltraLeft2};
        mUltraRight1.setAutomaticMode(true);
        */
        //mUltraFront = new AnalogInput(Constants.mUltrasonicFront);







    }


    /**
     * Initialize the default command for a subsystem By default subsystems have no default command,
     * but if they do, the default command is set with this method. It is called on all Subsystems by
     * CommandBase in the users program after all the Subsystems are created.
     */
    @Override
    protected void initDefaultCommand()
    {

    }

    /**
     * Method to setup settings on the 6 mRightMaster talons
     */
    private void setupTalons()
    {
        //Right Master
        mRightMaster = new TalonSRX(Constants.mRightMasterId);
        mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIDx, Constants.kTimeoutMs);
        //Right Slaves
        mRightSlave1 = new TalonSRX(Constants.mRightSlave1Id);
        mRightSlave1.follow(mRightMaster);

        mRightSlave2 = new TalonSRX(Constants.mRightSlave2Id);
        mRightSlave2.follow(mRightMaster);

        //Left Master
        mLeftMaster = new TalonSRX(Constants.mLeftMasterId);
        mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIDx, Constants.kTimeoutMs);

        //Left Slave
        mLeftSlave1 = new TalonSRX(Constants.mLeftSlave1Id);
        mLeftSlave1.follow(mLeftMaster);

        mLeftSlave2 = new TalonSRX(Constants.mLeftSlave2Id);
        mLeftSlave2.follow(mLeftMaster);


        turnPidController = new PIDController(Constants.kPidTurnP, Constants.kPidTurnI, Constants.kPidTurnD, navX, turnPidOutput);

        turnPidController.setInputRange(-180.0f, 180.0f);
        turnPidController.setOutputRange(-1.0, 1.0);
        turnPidController.setAbsoluteTolerance(Constants.kTolerance);

        //Velocity control setup

        mRightMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
        mRightMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
        mRightMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
        mRightMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        mRightMaster.config_kF(Constants.kPIDLoopIDx, Constants.rightVelocityKf, Constants.kTimeoutMs);
        mRightMaster.config_kP(Constants.kPIDLoopIDx, Constants.rightVelocityKp, Constants.kTimeoutMs);
        mRightMaster.config_kI(Constants.kPIDLoopIDx, Constants.rightVelocityKi, Constants.kTimeoutMs);
        mRightMaster.config_kD(Constants.kPIDLoopIDx, Constants.rightVelocityKd, Constants.kTimeoutMs);

        mLeftMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
        mLeftMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
        mLeftMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
        mLeftMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        mLeftMaster.config_kF(Constants.kPIDLoopIDx, Constants.leftVelocityKf, Constants.kTimeoutMs);
        mLeftMaster.config_kP(Constants.kPIDLoopIDx, Constants.leftVelocityKp, Constants.kTimeoutMs);
        mLeftMaster.config_kI(Constants.kPIDLoopIDx, Constants.leftVelocityKi, Constants.kTimeoutMs);
        mLeftMaster.config_kD(Constants.kPIDLoopIDx, Constants.leftVelocityKd, Constants.kTimeoutMs);
    }

    public void setSpeed(double right, double left)
    {
        mRightMaster.set(ControlMode.PercentOutput, -right);
        mLeftMaster.set(ControlMode.PercentOutput, -left);
    }

    public void setVelocity(double right, double left)
    {
        mRightMaster.set(ControlMode.Velocity, right);
        mLeftMaster.set(ControlMode.Velocity, left);
    }

    /**
     * Function to get the array of ultrasonics
     * @return The array of ultrasonics on the Drive train
     */
    //public Ultrasonic[] getUltrasonics() {
        //return mUltrasonics;
    //}

    /**
     * Function to get the private instance of Drive
     * @return Singleton of the drive class
     */
    public static Drive getInstance()
    {
        if (m_Instance == null) {
            m_Instance = new Drive();
        }
        return m_Instance;
    }

    public boolean turnToTarget(double target, double speed)
    {
        //Positive target to turn right, Negative to turn Left
        /*
        turnPidController.enable();
        turnPidController.setSetpoint(target);
        if(turnPidController.onTarget()){
            isTurning = false;
            turnPidController.disable();
            return true;
        }
        else
            return false;
        */
        double slowDownDistance = 60;
        //double slope = inchesPerSecondToTicksPer100ms(speed)/ slowDownDistance;

        double degreesToGo = (target - (-navX.getAngle()));

        if(Math.abs(degreesToGo) <= 7 && Math.abs(getRightEncoderVelocity()) <= 300) {
            setVelocity(0,0);
            return true;
        }
        else {
            if(Math.abs(degreesToGo) > slowDownDistance /*10*/ /*&& degreesToGo > slowDownDistance*/){
                if(degreesToGo > 0){
                    setVelocity(-inchesPerSecondToTicksPer100ms(speed),-inchesPerSecondToTicksPer100ms(speed));
                }
                else if(degreesToGo < 0){
                    setVelocity(inchesPerSecondToTicksPer100ms(speed),inchesPerSecondToTicksPer100ms(speed));
                }
                return false;
            }
            else if(Math.abs(degreesToGo) <= slowDownDistance) {
                /*
                if (degreesToGo > 0) {
                    setVelocity(-(slope * degreesToGo), -(slope * degreesToGo));
                } else if (degreesToGo < 0) {
                    setVelocity((slope * degreesToGo), (slope * degreesToGo));
                }
                */

                if(degreesToGo > 0){
                    setVelocity(-inchesPerSecondToTicksPer100ms(speed / 2),-inchesPerSecondToTicksPer100ms(speed / 2));
                }
                else if(degreesToGo < 0){
                    setVelocity(inchesPerSecondToTicksPer100ms(speed / 2),inchesPerSecondToTicksPer100ms(speed /2 ));
                }

                return false;
            }
        }
        return false;
    }

    public boolean moveForward(double distance, double speed)
    {

        double encoderDistance = (ticksToInches(getRightEncoder()) + ticksToInches(-getLeftEncoder())) / 2;
        double away = Math.abs(distance - encoderDistance);
        double direction;
        System.out.println("Away: " + away);
        System.out.println("Encoders: " + (getRightEncoder() + getLeftEncoder()) / 2);

        if(distance > encoderDistance){
            direction = 1;
        }else{
            direction = -1;
        }

            //85
            if(away >= 60){
                setSpeed((speed) * direction, -(speed) * direction);
                return false;
            }
            //85 - 65
            else if (away < 60 && away >40){
                setSpeed((speed * .5) * direction, -(speed * .5) * direction);
                return false;
            //65 - 45
            }else if(away < 40 && away > 20){
                setSpeed((speed * .25) * direction, -(speed * .25) * direction);
                return false;

            }

            //45-15
            if(away < 20 && away > 0){
                //setSpeed((speed * .125) * direction, -(speed * .125) * direction);

                //setSpeed(0, 0);
                //Timer.delay(.09);
                setSpeed(-speed * direction, speed * direction);
                Timer.delay(.575);
                setSpeed(0, 0);
                //Timer.delay(.3);
                //setSpeed(0, 0);

                return true;
            }
            //15
            else if (away <= 20){

                setSpeed(0,0);
                return true;
            }

            return false;
    }

    public int getRightEncoder()
    {
        return mRightMaster.getSelectedSensorPosition(Constants.kPIDLoopIDx);
    }

    public int getLeftEncoder()
    {
        return mLeftMaster.getSelectedSensorPosition(Constants.kPIDLoopIDx);
    }

    public double ticksToInches(double encoderValue)
    {
        double inches = encoderValue * Constants.inchesPerTick;
        return inches;
    }

    public double inchesToTicks(double inches) {
        double ticks = inches / Constants.inchesPerTick;
        return ticks;
    }


    public double getHeading()
    {
       return navX.getAngle();
    }

    public double getRightDistanceInMeters()
    {
        return getRightEncoder() * Constants.inchesPerTick * Constants.meterConversion;
    }

    public double getLeftDistanceInMeters()
    {
        return getLeftEncoder() * Constants.inchesPerTick * Constants.meterConversion;
    }

    public double getRightEncoderVelocity()
    {
        return mRightMaster.getSelectedSensorVelocity(Constants.kSlotIdx);
    }

    public double getLeftEncoderVelocity()
    {
        return mLeftMaster.getSelectedSensorVelocity(Constants.kSlotIdx);
    }

    /**
     * Updates the Subsystem
     */
    @Override
    public void update()
    {

    }

    /**
     * Startup code for the Subsystem
     */
    @Override
    public void startup()
    {

    }

    /**
     * Output the Subsystems Values to SmartDashboard
     */
    @Override
    public void toSmartDashboard()
    {

        SmartDashboard.putNumber("SensorVelRight", mRightMaster.getSelectedSensorVelocity(Constants.kPIDLoopIDx));
        SmartDashboard.putNumber("SensorPosRight",  mRightMaster.getSelectedSensorPosition(Constants.kPIDLoopIDx));
        //SmartDashboard.putNumber("MotorOutputPercentRight", mRightMaster.getMotorOutputPercent());
        //SmartDashboard.putNumber("ClosedLoopErrorRight", mRightMaster.getClosedLoopError(Constants.kPIDLoopIDx));

        SmartDashboard.putNumber("SensorVelLeft", mLeftMaster.getSelectedSensorVelocity(Constants.kPIDLoopIDx));
        SmartDashboard.putNumber("SensorPosLeft",  mLeftMaster.getSelectedSensorPosition(Constants.kPIDLoopIDx));

        //SmartDashboard.putNumber("MotorOutputPercentLeft", mLeftMaster.getMotorOutputPercent());
        //SmartDashboard.putNumber("ClosedLoopErrorLeft", mLeftMaster.getClosedLoopError(Constants.kPIDLoopIDx));
        /*
        SmartDashboard.putNumber("The BAD ULtra", mUltraLeft2.getRangeInches());
        SmartDashboard.putNumber("NAVX ANGLE", navX.getAngle());
        SmartDashboard.putNumber("BARometric Pressure", navX.getBarometricPressure());
        */
        SmartDashboard.putNumber("YAW", navX.getYaw());
        SmartDashboard.putNumber("Navx Rate", navX.getRate());
        SmartDashboard.putNumber("Gyro x", navX.getRawGyroX());
        SmartDashboard.putNumber("Gyro Angle", navX.getAngle());
        SmartDashboard.putBoolean("GYRO WORKING", navX.isConnected());
        /*
        SmartDashboard.putNumber("Gyro y", navX.getRawGyroY());
        SmartDashboard.putNumber("Gyro z", navX.getRawGyroZ());
        for(Ultrasonic ultra: mUltrasonics){
            SmartDashboard.putNumber("Ultra Value: " + ultra.getName(), ultra.getRangeInches());
        }

        //SmartDashboard.putNumber("Ultra Value: analog ultra", mUltraFront.getValue());
        */
    }

    /**
     * Stops all tasks in the Subsystem
     */
    @Override
    public void stop()
    {

    }

    /**
     * Zero all Sensors in the Subsystem
     */
    @Override
    public void zeroSensors()
    {
        mRightMaster.setSelectedSensorPosition(0, Constants.kPIDLoopIDx, Constants.kTimeoutMs);
        mLeftMaster.setSelectedSensorPosition(0, Constants.kPIDLoopIDx, Constants.kTimeoutMs);
        navX.zeroYaw();
    }

    public void setNeturalMode(NeutralMode mode)
    {
        mRightMaster.setNeutralMode(mode);
        mRightSlave1.setNeutralMode(mode);
        mRightSlave2.setNeutralMode(mode);
        mLeftMaster.setNeutralMode(mode);
        mLeftSlave1.setNeutralMode(mode);
        mLeftSlave2.setNeutralMode(mode);
    }


    /**
     * Moves the robot to a specified distance with a speed
     * @param distance desired distance you want to go in inches
     * @param speed desired speed you want to go in inches per second
     * @return returns if the robot is at the distance
     */
    public boolean goToDistanceVelocity(double distance, double speed)
    {
        double encoderDistance = (ticksToInches(getRightEncoder()) + ticksToInches(-getLeftEncoder())) / 2;
        double away = (distance - encoderDistance);
        System.out.println("Away: " + away);
        double slowDownDistance = distance;
        double slope =  (inchesPerSecondToTicksPer100ms(speed) / (slowDownDistance));
        //274 slowDownDistance
        //int reverseDistance = 19;
        if (away > slowDownDistance) {
            setVelocity(-inchesPerSecondToTicksPer100ms(speed), inchesPerSecondToTicksPer100ms(speed));
        }
        else if(away <= slowDownDistance && away > 75) {
            setVelocity(-(slope * away), (slope * (away +15)));
            return false;
        }
        else if (Math.abs(away) <= 70 && away > 1) {
            //if(away > reverseDistance)
                //away = reverseDistance;
            //setVelocity((slope * away) * (speed * 0.95), -(slope * away)*(speed * 0.95));
            setVelocity(-(slope * 65), (slope * 75));
            return false;
        }
        else if (Math.abs(away) <= 1)
        {
            setVelocity(0,0);
            System.out.println("STOPPPPPPPPPEDDDDDDDDD");
            return true;
        }
        return false;
    }

    public double inchesPerSecondToTicksPer100ms(double inchesPerSecond) {
        double ticksPerSecond = inchesToTicks(inchesPerSecond);
        return ticksPerSecond / 10;
    }





}
