package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CommandGrab extends SubsystemBase {
    // SparkMax motor
    SparkMax grabMotor = new SparkMax(1, MotorType.kBrushless);

    // TalonFX motor
    TalonFX pivotMotor = new TalonFX(2);

    double pivotDegrees = 10;

    // PID Controller
    PIDController pivotPID = new PIDController(0.1, 0, 0);

    public Command active() {
        return Commands.run(() -> {
            // when active:
            grabMotor.set(0.75);
            pivotDegrees = 270;
        }, this);
    }

    public Command idle() {
        return Commands.run(() -> {
            // when idle:
            grabMotor.set(0);
            pivotDegrees = 10;
        }, this);
    }

    @Override
    public void periodic() {
        // every 50Hz
        double currentPivotRotations = pivotMotor.getPosition().getValueAsDouble();
        double targetPivotRotations = pivotDegrees / 360;
        pivotMotor.set(pivotPID.calculate(targetPivotRotations - currentPivotRotations));
    }
}
