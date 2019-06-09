package alchemystics.menu.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}

}
