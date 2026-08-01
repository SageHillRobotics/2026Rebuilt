package frc.robot.util;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class PlatformXboxController extends CommandXboxController {
    private static final int[] kXInputAxisOrder = {0, 1, 2, 3, 4, 5};
    private static final int[] kXInputButtonOrder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static final int[] kMacOsHidAxisOrder = {0, 1, 5, 4, 2, 3};
    private static final int[] kMacOsHidButtonOrder = {0, 1, 2, 4, 5, 7, 8, 11, 12, 14, 15};

    private final int[] m_axisOrder;
    private final int[] m_buttonOrder;
    private final boolean m_triggersRestAtNegativeOne;

    public PlatformXboxController(int port) {
        super(port);
        boolean macOs = isMacOs();
        m_axisOrder = macOs ? kMacOsHidAxisOrder : kXInputAxisOrder;
        m_buttonOrder = macOs ? kMacOsHidButtonOrder : kXInputButtonOrder;
        m_triggersRestAtNegativeOne = macOs;
    }

    private static boolean isMacOs() {
        return System.getProperty("os.name", "").startsWith("Mac");
    }

    @Override
    public double getRawAxis(int axis) {
        return scale(axis, super.getRawAxis(translate(m_axisOrder, axis)));
    }

    @Override
    public Trigger button(int button, EventLoop loop) {
        return super.button(translate(m_buttonOrder, button), loop);
    }

    @Override
    public double getLeftX() {
        return getRawAxis(XboxController.Axis.kLeftX.value);
    }

    @Override
    public double getLeftY() {
        return getRawAxis(XboxController.Axis.kLeftY.value);
    }

    @Override
    public double getRightX() {
        return getRawAxis(XboxController.Axis.kRightX.value);
    }

    @Override
    public double getRightY() {
        return getRawAxis(XboxController.Axis.kRightY.value);
    }

    @Override
    public double getLeftTriggerAxis() {
        return getRawAxis(XboxController.Axis.kLeftTrigger.value);
    }

    @Override
    public double getRightTriggerAxis() {
        return getRawAxis(XboxController.Axis.kRightTrigger.value);
    }

    private double scale(int axis, double rawValue) {
        if (m_triggersRestAtNegativeOne && isTriggerAxis(axis)) {
            return (rawValue + 1.0) / 2.0;
        }
        return rawValue;
    }

    private static boolean isTriggerAxis(int axis) {
        return axis == XboxController.Axis.kLeftTrigger.value
            || axis == XboxController.Axis.kRightTrigger.value;
    }

    private static int translate(int[] order, int index) {
        return index >= 0 && index < order.length ? order[index] : index;
    }
}
