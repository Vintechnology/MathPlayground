package com.rootonchair.phv;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/* TODO: format text, disable draw dots
 * TODO: Resizable
 */
public class MathPlayground extends JFrame {
	private DrawCanvas canvas;
	private EquationInterpret interpreter;
	
	public MathPlayground(){
		interpreter=new EquationInterpret("0","x",0);
		canvas=new DrawCanvas();
		canvas.setPreferredSize(new Dimension(DrawCanvas.CANVAS_WIDTH,DrawCanvas.CANVAS_HEIGHT));

		JPanel editPanel=new JPanel();
		editPanel.setLayout(new BoxLayout(editPanel,BoxLayout.Y_AXIS));
		
		JPanel textPanel=new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel,BoxLayout.Y_AXIS));
		textPanel.add(new JLabel("Equation:"));
		
		JPanel variablePanel=new JPanel();
		variablePanel.setLayout(new GridLayout(1,0));
		final JTextField equationField=new JTextField(10);
		variablePanel.add(equationField);
		
		JPanel groupPanel=new JPanel();
		groupPanel.setLayout(new BoxLayout(groupPanel,BoxLayout.X_AXIS));
		groupPanel.add(textPanel);
		groupPanel.add(Box.createRigidArea(new Dimension(5,0)));
		groupPanel.add(variablePanel);
		final JLabel resultLabel=new JLabel();
		resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		final JButton okButton=new JButton("OK");
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input=equationField.getText();
				if(!input.isEmpty()){
					interpreter.setString(input);
					canvas.clearCanvas();
					double x,y;
					int scaledX,scaledY;
					for(int i=-100;i<=100;i++){
						x=i/10d;
						interpreter.setVariable(x);
						try{
							y=interpreter.parse();
						}catch(RuntimeException e){
							JOptionPane.showMessageDialog(null, "Equation error: "+e.getMessage()
												,"MATH ERROR",JOptionPane.ERROR_MESSAGE);
							break;
						}
						scaledX=(int)Math.round(x*canvas.getScale());
						scaledY=(int)Math.round(y*(-canvas.getScale()));
						canvas.drawPoint(scaledX, scaledY);
					}
					resultLabel.setText("y="+input);
				}
			}
			
		});
		JButton resetButton=new JButton("Reset");
		resetButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.clearCanvas();
			}
			
		});
		
		JPanel buttonPanel=new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		buttonPanel.add(resetButton);
		
		editPanel.add(groupPanel);
		editPanel.add(Box.createRigidArea(new Dimension(0,5)));
		editPanel.add(resultLabel);
		editPanel.add(buttonPanel);
		
		JPanel upperView=new JPanel();
		upperView.setLayout(new FlowLayout());
		upperView.add(canvas);
		upperView.add(editPanel);
		
		Container c= getContentPane();
		c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
		c.add(upperView);
		c.add(Box.createRigidArea(new Dimension(0,5)));
		
		
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setTitle("Math Playground");
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				new MathPlayground();
			}
			
		});
	}

}
