package ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.peer.FramePeer;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import kitchen.control.IngredientsOrderManager;
import kitchen.control.RecipeManager;
import kitchen.model.BeanIngredientsOrder;
import kitchen.model.BeanOrderDetail;
import kitchen.model.BeanRecipeEvaluation;
import kitchen.model.BeanRecipeInformation;
import kitchen.model.BeanRecipeMaterial;
import kitchen.model.BeanRecipeStep;
import kitchen.model.BeanUser;
import kitchen.util.BaseException;

public class FrmOrderStatic extends JFrame implements ActionListener {
	private JPanel toolBar = new JPanel();
	private JButton btnChangeStatus = new JButton("修改状态");
	private JComboBox cmbOrderStatus = new JComboBox(new String[] { "下单", "在途", "送达" });

//	用户
	private Object tblUserTitle[] = BeanUser.tblUserTitle;
	private Object tblUserData[][];
	DefaultTableModel tabUserModel = new DefaultTableModel();
	private JTable dataTableUser = new JTable(tabUserModel);
	private BeanUser curUser = null;
	List<BeanUser> allUsers = null;

//	订单信息
	private Object tblOrderTitle[] = BeanIngredientsOrder.tblOrderTitle;
	private Object tblOrderData[][];
	DefaultTableModel tabOrderModel = new DefaultTableModel();
	private JTable dataTableOrder = new JTable(tabOrderModel);
	private BeanIngredientsOrder curOrder = null;
	List<BeanIngredientsOrder> allOrders = null;

//	订单详情
	private Object tblDetailsTitle[] = BeanOrderDetail.tblDetailTitle;
	private Object tblDetailData[][];
	DefaultTableModel tabDetailModel = new DefaultTableModel();
	private JTable dataTableDetails = new JTable(tabDetailModel);
	private BeanOrderDetail curDetail = null;
	List<BeanOrderDetail> allDetails = null;

//	刷新订单
	private void reloadOrdersTable() {// 这是测试数据，需要用实际数替换
		IngredientsOrderManager orderManager = new IngredientsOrderManager();
		curUser = BeanUser.currentUser;

		try {
			allOrders = orderManager.loadAllOrders();// 根据用户loadall菜谱
		} catch (BaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		tblOrderData = new Object[allOrders.size()][BeanIngredientsOrder.tblOrderTitle.length];
		for (int i = 0; i < allOrders.size(); i++) {
			for (int j = 0; j < BeanIngredientsOrder.tblOrderTitle.length; j++)
				tblOrderData[i][j] = allOrders.get(i).getCell(j);
		}
		tabOrderModel.setDataVector(tblOrderData, tblOrderTitle);
		this.dataTableOrder.validate();
		this.dataTableOrder.repaint();
	}

//	刷新订单详情
	private void reloadDetailsTable(int recipe_number) {// 这是测试数据，需要用实际数替换
		IngredientsOrderManager orderManager = new IngredientsOrderManager();

		try {
			allDetails = orderManager.loadAllDetails(curOrder);
		} catch (BaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		tblDetailData = new Object[allDetails.size()][BeanOrderDetail.tblDetailTitle.length];
		for (int i = 0; i < allDetails.size(); i++) {
			for (int j = 0; j < BeanOrderDetail.tblDetailTitle.length; j++)
				tblDetailData[i][j] = allDetails.get(i).getCell(j);
		}
		tabDetailModel.setDataVector(tblDetailData, tblDetailsTitle);
		this.dataTableDetails.validate();
		this.dataTableDetails.repaint();
	}

	public FrmOrderStatic(JFrame f, String s, boolean b) {

		super();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolBar.add(cmbOrderStatus);
		toolBar.add(btnChangeStatus);

//		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setSize(1000, 700);
		this.setTitle("订单统计");

		this.getContentPane().add(toolBar, BorderLayout.NORTH);

//		订单
		JScrollPane paneOrder = new JScrollPane(this.dataTableOrder);
		paneOrder.setPreferredSize(new Dimension(500, 0));
		this.getContentPane().add(paneOrder, BorderLayout.WEST);

//		鼠标点击 刷新界面 订单/ 订单详情
		this.dataTableOrder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = FrmOrderStatic.this.dataTableOrder.getSelectedRow();
				if (i < 0) {
					return;
				}
				FrmOrderStatic.this.reloadDetailsTable(i);
			}

		});
		this.reloadOrdersTable();

//		步骤
		JScrollPane paneDetail = new JScrollPane(this.dataTableDetails);
//		paneDetail.setPreferredSize(new Dimension(60, 0));
		this.getContentPane().add(paneDetail, BorderLayout.CENTER);

		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2, (int) (height - this.getHeight()) / 2);

		this.validate();

		this.btnChangeStatus.addActionListener(this);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				System.exit(0);
			}
		});
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
////		this.reloadTable();
//		if (e.getSource() == this.btnSearch) {
//			this.reloadTable();
//		} else if (e.getSource() == this.btnEvaluation) {
//			FrmAddEvaluation addEvaluation = new FrmAddEvaluation(this, "添加评价", true);
//			addEvaluation.curRecipe = this.curRecipes;
//			addEvaluation.setVisible(true);
//			this.reloadEvaluationsTable(this.curRecipes.getRecipe_number() - 1);
//		}else if (e.getSource() == this.btnOrder) {
//			FrmAddOrder addOrder = new FrmAddOrder(this, "添加评价", true);
//			addOrder.setVisible(true);
////			this.reloadEvaluationsTable(this.curRecipes.getRecipe_number() - 1);
//		}
	}
}
