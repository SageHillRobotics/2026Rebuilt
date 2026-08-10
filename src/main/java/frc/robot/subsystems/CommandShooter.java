package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CommandShooter extends SubsystemBase {
    public SparkMax spindexer = new SparkMax(5, MotorType.kBrushless);
    public SparkMax tunnel = new SparkMax(23, MotorType.kBrushless);

    public TalonFX rightShooter = new TalonFX(20);
    public TalonFX leftShooter = new TalonFX(22);

    public CommandShooter() {
        spindexer.configure(new SparkMaxConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        tunnel.configure(new SparkMaxConfig(), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightShooter.getConfigurator().apply(new TalonFXConfiguration());
        leftShooter.getConfigurator().apply(new TalonFXConfiguration());
    }

    public Command shoot() {
        return Commands.run(() -> {
            spindexer.set(1);
            tunnel.set(1);
            rightShooter.set(1);
            leftShooter.set(-1);
        }, this);
    }

    public Command idle() {
        return Commands.run(() -> {
            spindexer.set(0);
            tunnel.set(0);
            rightShooter.set(0);
            leftShooter.set(0);
        }, this);
    }
}
