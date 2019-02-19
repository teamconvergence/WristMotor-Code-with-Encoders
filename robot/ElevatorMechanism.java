/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import javax.management.RuntimeErrorException;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 * Add your docs here.
 */
public class ElevatorMechanism extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public WPI_TalonSRX elevatorMotor1 = new WPI_TalonSRX(RobotMap.p_elevatormotor1);
  public WPI_TalonSRX elevatorMotor2 = new WPI_TalonSRX(RobotMap.p_elevatormotor2);

  public DigitalInput upperLimit = new DigitalInput(RobotMap.p_frontUpperLimit);
  public DigitalInput lowerLimit = new DigitalInput(RobotMap.p_frontLowerLimit);

  public static int FLOOR_POSITION = 0;
  public static int CARGO1_POSITION = 1000;
  public static int PANEL1_POSITION = 2000;
  public static int CARGO2_POSITION = 3000;
  public static int PANEL2_POSITION = 4000;
  public static int CARGO3_POSITION = 5000;
  public static int PANEL3_POSITION = 6000;
  // public Encoder liftEncoder = new Encoder(RobotMap.p_encoderchannel1, RobotMap.p_encoderchannel2, false, Encoder.EncodingType.k4X);
  // public FeedbackDevice encoder = FeedbackDevice.QuadEncoder;

  public ElevatorMechanism(){
    // liftEncoder.setDistancePerPulse(1000);
    // liftEncoder.reset();
    elevatorMotor1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    elevatorMotor2.follow(elevatorMotor1);
    elevatorMotor2.setInverted(true);
    elevatorMotor1.setNeutralMode(NeutralMode.Brake);
    elevatorMotor2.setNeutralMode(NeutralMode.Brake);
  }
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void resetEncoder(){
    // liftEncoder.reset();
    elevatorMotor1.setSelectedSensorPosition(0);
  }
  public double getEncoderCount(){
    // return liftEncoder.get();
    return elevatorMotor1.getSelectedSensorPosition();
  }
  public void setEncoderDestination(int counts){
    // elevatorMotor1.setSelectedSensorPosition(counts);
    if(counts>getEncoderCount()){
      setSpeed(-0.15);
    }
    else if(counts>=getEncoderCount()-100 && counts<=getEncoderCount()+100){
      setSpeed(0);
    }
    else setSpeed(0.15);
  }
  public void setSpeed(double speed){
    if(!upperLimit.get()&&!lowerLimit.get()){
      elevatorMotor1.set(speed);
    }
    else elevatorMotor1.set(0.0); 
  }
  public void goFloor(){
    setEncoderDestination(FLOOR_POSITION);

    // if(FLOOR_POSITION == getEncoderCount()){
    //   resetEncoder();
    // }
    // goes down until it hits the limit switch
    // then reset encoder to -50 maybe
  }
  public boolean setZeroPosition(){
    boolean atBottom = false;
    if(upperLimit.get()){
      elevatorMotor1.set(0.0);
      elevatorMotor2.set(0.0);
      throw new RuntimeErrorException( new Error("Elevator at top during initialization "));
    }
    else if(lowerLimit.get()){
      elevatorMotor1.set(0.0);
      elevatorMotor2.set(0.0);
      atBottom = true;
    }
    else {
      setSpeed(-0.15);
    }
    if(atBottom){
      setEncoderDestination(500);
      if(getEncoderCount()<=600 && getEncoderCount()>= 400){
        setSpeed(0.0);
        resetEncoder();
        return  true;
      }
    }
    return false;
    
  }
  // public double setUpperLimit(){
  //   if(){

  //   }
  // }

}
