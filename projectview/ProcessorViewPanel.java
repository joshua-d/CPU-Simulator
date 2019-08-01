package projectview;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import project.Machine;
public class ProcessorViewPanel {
	private Machine machine;
	private JTextField acc = new JTextField();
	private JTextField pc = new JTextField();
	public ProcessorViewPanel(Machine m) {
		machine = m;
	}
	public JComponent createProcessorDisplay() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.add(new JLabel("Accumulator: ", JLabel.RIGHT));
		panel.add(acc);
		panel.add(new JLabel("Program Counter: ", JLabel.RIGHT));
		panel.add(pc);
		return panel;
	}
	public void update() {
		if(machine != null) {
			acc.setText("" + machine.getAccum());
			pc.setText("" + machine.getPC());
		}
	}
	
	public static void main(String[] args) {
		Machine machine = new Machine(()->System.exit(0));
		ProcessorViewPanel panel = new ProcessorViewPanel(machine);
		JFrame frame = new JFrame("TEST");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 60);
		frame.setLocationRelativeTo(null);
		frame.add(panel.createProcessorDisplay());
		frame.setVisible(true);
	}
}