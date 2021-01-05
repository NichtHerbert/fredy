package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = -5511353684955783810L;

	public MenuBar() {
		super();
		
		JMenu 	fileMenu,
				aboutMenu;
		
		// FileMenu
//		fileMenu = new JMenu("File");
//		JMenuItem clear, open, save, saveAs, exit;
//		
//		add(fileMenu);
		
		// AboutMenu
		aboutMenu = new JMenu("About");
		JMenuItem about = new JMenuItem("About fredy");
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MenuBar.this,
					    "\nfredy programmed by NichtHerbert 2018, 2020.\n\n\nIcons: \n" + EIcons.getAllAuthorHtmlLinks(),
					    "About fredy",
					    JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		aboutMenu.add(about);
		add(aboutMenu);
	}



}
