package ui;

import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JDialog;

import kitchen.control.IngredientsManager;
import kitchen.control.RecipeManager;
import kitchen.model.BeanIngredientsCategory;
import kitchen.model.BeanIngredientsInformation;
import kitchen.model.BeanRecipeInformation;
import kitchen.model.BeanRecipeStep;
import kitchen.model.BeanUser;
import kitchen.util.*;

public class FrmAddEvaluation extends JDialog implements ActionListener{
	
	public BeanRecipeInformation curRecipe = null;
	
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private JButton btnOk = new JButton("确定");
	private JButton btnCancel = new JButton("取消");
	
	private JLabel labelMark = new JLabel("评分");
	private JLabel labelContent = new JLabel("内容");
	private JComboBox cmbCollection = new JComboBox(new String[] { "收藏", "不收藏" });	
	List<String> ingredientsInformations = new IngredientsManager().loadAllIngredientsName();
	
	private JTextField edtMark = new JTextField(20);
	private JTextField edtContent = new JTextField(20);
	
	public FrmAddEvaluation(JFrame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBar.add(btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.setLayout(null);
		labelMark.setBounds(8, 8, 45, 18);
		workPane.add(labelMark);
		edtMark.setBounds(108, 5, 166, 24);
		workPane.add(edtMark);
		labelContent.setBounds(8, 88, 45, 18);
		workPane.add(labelContent);
		edtContent.setBounds(108, 85, 166, 94);
		workPane.add(edtContent);
		cmbCollection.setBounds(108, 50, 166, 24);
		workPane.add(cmbCollection);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(343, 287);
		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);

		this.validate();
		this.btnOk.addActionListener(this);
		this.btnCancel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == this.btnCancel) {
			this.setVisible(false);
			return;
		} else if (e.getSource() == this.btnOk) {
			String collection = this.cmbCollection.getSelectedItem().toString();
			double mark = Double.parseDouble(this.edtMark.getText());
			String content = this.edtContent.getText();
			
			RecipeManager recipeManager = new RecipeManager();
			try {
				recipeManager.addEvaluation(curRecipe, BeanUser.currentUser, content, collection, mark);
				this.setVisible(false);
			} catch (BaseException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

	}
}
