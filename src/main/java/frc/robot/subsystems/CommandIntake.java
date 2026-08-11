package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Rotations;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CommandIntake extends SubsystemBase {
    public static final double PIVOT_GEAR_RATIO = 100;

    public static final Angle PIVOT_POSITION_IDLE = Degrees.of(5);
    public static final Angle PIVOT_POSITION_ACTIVE = Degrees.of(70);

    public static final double PIVOT_STATOR_LIMIT = 20;

    public TalonFX intake = new TalonFX(4);
    public TalonFX pivot = new TalonFX(9);

    public PIDController pivotPID = new PIDController(3, 0, 0);

    public boolean pivotActive = false;

    public CommandIntake() {
        intake.getConfigurator().apply(new TalonFXConfiguration());
        
        pivot.getConfigurator().apply(new TalonFXConfiguration()
            .withMotorOutput(new MotorOutputConfigs()
                .withNeutralMode(NeutralModeValue.Brake))
            .withFeedback(new FeedbackConfigs()
                .withSensorToMechanismRatio(PIVOT_GEAR_RATIO))
            .withSoftwareLimitSwitch(new SoftwareLimitSwitchConfigs()
                .withReverseSoftLimitEnable(true)
                .withReverseSoftLimitThreshold(Degrees.of(0))
                .withForwardSoftLimitEnable(true)
                .withForwardSoftLimitThreshold(PIVOT_POSITION_ACTIVE.plus(Degrees.of(10))))
            .withCurrentLimits(new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(PIVOT_STATOR_LIMIT))
                .withStatorCurrentLimitEnable(true)));

        pivot.setPosition(0);
    }

    @Override
    public void periodic() {
        pivot.set(pivotPID.calculate(pivot.getPosition().getValueAsDouble(), (pivotActive ? PIVOT_POSITION_ACTIVE : PIVOT_POSITION_IDLE).in(Rotations)));
    }

    public Command intake() {
        return Commands.run(() -> {
            intake.set(1);
        }, this);
    }

    public Command idle() {
        return Commands.run(() -> {
            intake.set(0);
        }, this);
    }

    public Command pivot() {
        return Commands.runOnce(() -> {
            pivotActive = !pivotActive;
        });
    }
}
