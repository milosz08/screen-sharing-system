package pl.polsl.screensharing.client.view.popup;

import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.component.JAppPasswordTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PasswordPopup extends JPanel {
    private final AbstractPopupDialog root;

    private final JAppPasswordTextField passwordTextField;
    private final JCheckBox passwordTogglerCheckbox;
    private final String[] options = {"OK", "Cancel"};

    public PasswordPopup(AbstractPopupDialog root) {
        this.root = root;

        passwordTextField = new JAppPasswordTextField(10);
        passwordTogglerCheckbox = new JCheckBox("Show password");

        passwordTogglerCheckbox.addActionListener(this::togglePasswordField);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(passwordTextField);
        add(passwordTogglerCheckbox);
    }

    public String showPopupAndWaitForInput() {
        final int option = JOptionPane.showOptionDialog(root, this, "Insert password",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, options, options[1]);
        if (option != 0) {
            return null;
        }
        final char[] password = passwordTextField.getPassword();
        return new String(password);
    }

    private void togglePasswordField(ActionEvent event) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        passwordTextField.toggleVisibility(checkBox.isSelected());
    }
}
