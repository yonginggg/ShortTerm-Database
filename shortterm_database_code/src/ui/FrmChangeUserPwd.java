package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import kitchen.util.*;
import kitchen.model.*;
import kitchen.control.*;
import java.awt.Font;

public class FrmChangeUserPwd extends JDialog implements ActionListener {

	public BeanUser currentUser = null;

	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private JButton btnOk = new JButton("确定");
	private JButton btnCancel = new JButton("取消");

	private JLabel labelPwdOld = new JLabel("原密码：");
	private JLabel labelPwd = new JLabel("新密码：");
	private JLabel labelPwd2 = new JLabel("新密码：");
	private JPasswordField edtPwdOld = new JPasswordField(20);
	private JPasswordField edtPwd = new JPasswordField(20);
	private JPasswordField edtPwd2 = new JPasswordField(20);

	public FrmChangeUserPwd(Frame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		btnOk.setFont(new Font("宋体", Font.BOLD, 20));
		toolBar.add(this.btnOk);
		btnCancel.setFont(new Font("宋体", Font.BOLD, 20));
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.setLayout(null);
		labelPwdOld.setFont(new Font("宋体", Font.BOLD, 20));
		labelPwdOld.setBounds(25, 40, 86, 18);
		workPane.add(labelPwdOld);
		edtPwdOld.setBounds(125, 40, 166, 24);
		workPane.add(edtPwdOld);
		labelPwd.setFont(new Font("宋体", Font.BOLD, 20));
		labelPwd.setBounds(25, 88, 86, 18);
		workPane.add(labelPwd);
		edtPwd.setBounds(125, 86, 166, 24);
		workPane.add(edtPwd);
		labelPwd2.setFont(new Font("宋体", Font.BOLD, 20));
		labelPwd2.setBounds(25, 144, 86, 18);
		workPane.add(labelPwd2);
		edtPwd2.setBounds(125, 142, 166, 24);
		workPane.add(edtPwd2);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(340, 337);
		this.btnCancel.addActionListener(this);
		this.btnOk.addActionListener(this);

		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnCancel) {
			if (JOptionPane.showConfirmDialog(this, "是否确认取消?", "确认",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				this.setVisible(false);
			}
		}

		else if (e.getSource() == this.btnOk) {
			if (JOptionPane.showConfirmDialog(this, "是否确认修改用户密码?", "确认",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				try {
					UserManager userManager = new UserManager();
					userManager.changePwd(currentUser, new String(edtPwdOld.getPassword()),
							new String(edtPwd.getPassword()), new String(edtPwd2.getPassword()));
					this.setVisible(false);
				} catch (BaseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

		}

	}

}
