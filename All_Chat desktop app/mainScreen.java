package all_chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.swing.border.LineBorder;

/**
 *
 * @author sabab
 */

public class mainScreen extends javax.swing.JFrame {

	/**
	 * Creates new form mainScreen
	 */
	private JPanel friendsPanel;
	private static String username;
	private String password;
	private ArrayList<String> arrOfFriends;
	public static ArrayList<String> arrOfU1,arrOfU2;
	private Timer t;
	private void setIcon() {
		setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
	}
	public mainScreen(String username,String password) {
		this.username = username;
		this.password=password;
		initComponents();
		setIcon();
		setLocationRelativeTo(null);
		loadEverything();
	}
	private void loadEverything() {
		jLabel2.setText(username);
		loadIMG();
		loadFriends();
		t = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				loadIMG();
				loadFriends();
			}
		});
		t.start();
	}

	private String getIMG() {
		String lastIMG = "";

		String db = "jdbc:mysql://localhost/allchat";
		String user = "root";
		String pass = "12345678";

		try {
			Connection conn = DriverManager.getConnection(db, user, pass);
			java.sql.Statement stmt = conn.createStatement();
			String query = "SELECT picture FROM users WHERE BINARY username='" + username + "'";
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				lastIMG = result.getString(1);
			} else {
				System.exit(0);
			}
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		return lastIMG;
	}

	private void loadIMG() {
		Image image = null;
		String lastIMG = getIMG();
		String imgurl = "http://localhost/All_Chat/Extra/styles/images/" + lastIMG + ".png";
		try {
			URL url = new URL(imgurl);
			image = ImageIO.read(url);
			image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
			ImageIcon img = new ImageIcon(image);
			Image i = img.getImage();
			Image modified = i.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			jLabel1.setIcon(new ImageIcon(modified));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadFriends() {
		friendsPanel = new JPanel();
		friendsPanel.setBounds(0, 0, 319, 432);
		jScrollPane1.getViewport().add(friendsPanel);
		int howMany = getHowMany();
		friendsPanel.setLayout(new GridLayout(howMany, 4));
		showFriends();
	}

	private void showFriends() {
		String db = "jdbc:mysql://localhost/allchat";
		String user = "root";
		String pass = "12345678";

		try {
			Connection conn = DriverManager.getConnection(db, user, pass);
			java.sql.Statement stmt = conn.createStatement();
			for (int i = 0; i < arrOfFriends.size(); i++) {
				String query = "SELECT * FROM users WHERE BINARY username='" + arrOfFriends.get(i) + "'";
				ResultSet result = stmt.executeQuery(query);

				JLabel ava = new JLabel();
				ava.setPreferredSize(new Dimension(50, 50));
				ava.setOpaque(true);
				ava.setHorizontalAlignment(SwingConstants.CENTER);
				ava.setForeground(Color.white);
				result.next();
				if (result.getString(4).equals("0")) {
					ava.setBackground(Color.red);
					ava.setText("OFFLINE");
				} else if (result.getString(4).equals("1")) {
					ava.setBackground(Color.green);
					ava.setText("ONLINE");
				}

				JLabel name = new JLabel();
				name.setFont(new Font("Callibari", Font.BOLD, 10));
				name.setForeground(Color.blue);
				name.setText("" + arrOfFriends.get(i));

				JLabel pic = new JLabel();
				pic.setPreferredSize(new Dimension(50, 50));
				String imgurl = "http://localhost/All_Chat/Extra/styles/images/" + result.getString(3) + ".png";
				Image image = null;
				try {
					URL url = new URL(imgurl);
					image = ImageIO.read(url);
					image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
					ImageIcon img = new ImageIcon(image);
					Image ii = img.getImage();
					Image modified = ii.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
					pic.setIcon(new ImageIcon(modified));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				JButton chatButton = new JButton("Chat");
				chatButton.setBackground(Color.white);
				chatButton.setForeground(Color.red);
				chatButton.setBorder(new LineBorder(Color.red, 2));
				chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
				int ii = i;
				chatButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						dispose();
						t.stop();
						new chatWithFriendScreen(username,password, "" + arrOfFriends.get(ii)).setVisible(true);;

					}
				});

				friendsPanel.setBackground(Color.white);
				friendsPanel.add(ava);
				friendsPanel.add(name);
				friendsPanel.add(pic);
				friendsPanel.add(chatButton);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private int getHowMany() {
		arrOfU1=new ArrayList<String>();
		arrOfU2=new ArrayList<String>();
		arrOfFriends = new ArrayList<String>();
		String db = "jdbc:mysql://localhost/allchat";
		String user = "root";
		String pass = "12345678";

		int after = 0;
		try {
			Connection conn = DriverManager.getConnection(db, user, pass);
			java.sql.Statement stmt = conn.createStatement();
			String query = "SELECT * FROM friends WHERE BINARY user1='" + username + "' OR BINARY user2='"
					+ username + "'";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				if (result.getString(1).equals(username)) {
					arrOfFriends.add("" + result.getString(2));
					arrOfU1.add(username);
					arrOfU2.add(result.getString(2));
				} else if (result.getString(2).equals(username)) {
					arrOfFriends.add("" + result.getString(1));
					arrOfU1.add(result.getString(1));
					arrOfU2.add(username);
				}
				after++;
			}
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		return after;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 3));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setMaximumSize(new java.awt.Dimension(643, 60));
        jPanel2.setMinimumSize(new java.awt.Dimension(643, 60));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jLabel5.setBackground(new java.awt.Color(102, 102, 102));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("_");
        jLabel5.setToolTipText("");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.setOpaque(true);
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5minimize(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5hoverOnTopLabels(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel5exitOnTopLabels(evt);
            }
        });
        jPanel3.add(jLabel5);

        jLabel6.setBackground(new java.awt.Color(102, 102, 102));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("X");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.setOpaque(true);
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6exit(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6hoverOnTopLabels(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6exitOnTopLabels(evt);
            }
        });
        jPanel3.add(jLabel6);

        jButton1.setBackground(java.awt.Color.red);
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Logout");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.blue, 2));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusable(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutHover(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutOut(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setBackground(new java.awt.Color(102, 102, 102));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 200));

        jButton2.setBackground(java.awt.Color.blue);
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Edit Profile");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusable(false);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                editHover(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                editOut(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToEditProfileScreen(evt);
            }
        });

        jButton3.setBackground(java.awt.Color.blue);
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Add A Friend");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusable(false);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addHover(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addOut(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToAddFriendScreen(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(53, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goToAddFriendScreen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToAddFriendScreen
        // TODO add your handling code here:
    	dispose();
    	t.stop();
    	new addFriendScreen(username,password).setVisible(true);
    }//GEN-LAST:event_goToAddFriendScreen
    private void goToEditProfileScreen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToEditProfileScreen
        // TODO add your handling code here:
    	try {
    		Desktop.getDesktop().browse(new URI("http://localhost/all_chat/uploadPic.php?username="+username+"&password="+password+"&temp=javaToWeb1090"));
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, ""+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, ""+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
    }//GEN-LAST:event_goToEditProfileScreen

	private void jLabel5minimize(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel5minimize
		// TODO add your handling code here:
		setState(JFrame.ICONIFIED);
	}// GEN-LAST:event_jLabel5minimize

	private void jLabel5hoverOnTopLabels(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel5hoverOnTopLabels
		// TODO add your handling code here:
		JLabel temp = (JLabel) evt.getSource();
		temp.setBackground(Color.red);
	}// GEN-LAST:event_jLabel5hoverOnTopLabels

	private void jLabel5exitOnTopLabels(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel5exitOnTopLabels
		// TODO add your handling code here:
		JLabel temp = (JLabel) evt.getSource();
		temp.setBackground(new Color(102, 102, 102));
	}// GEN-LAST:event_jLabel5exitOnTopLabels

	private void jLabel6exit(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel6exit
		// TODO add your handling code here:
		changeAva();
		System.exit(0);
	}// GEN-LAST:event_jLabel6exit

	private void jLabel6hoverOnTopLabels(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel6hoverOnTopLabels
		// TODO add your handling code here:
		JLabel temp = (JLabel) evt.getSource();
		temp.setBackground(Color.red);
	}// GEN-LAST:event_jLabel6hoverOnTopLabels

	private void jLabel6exitOnTopLabels(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jLabel6exitOnTopLabels
		// TODO add your handling code here:
		JLabel temp = (JLabel) evt.getSource();
		temp.setBackground(new Color(102, 102, 102));
	}// GEN-LAST:event_jLabel6exitOnTopLabels

	private void logoutHover(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_logoutHover
		// TODO add your handling code here:
		jButton1.setBackground(Color.blue);
		jButton1.setBorder(new LineBorder(Color.red, 2));
	}// GEN-LAST:event_logoutHover

	private void logoutOut(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_logoutOut
		// TODO add your handling code here:
		jButton1.setBackground(Color.red);
		jButton1.setBorder(new LineBorder(Color.blue, 2));
	}// GEN-LAST:event_logoutOut

	private void editHover(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_editHover
		// TODO add your handling code here:
		jButton2.setBackground(Color.red);
	}// GEN-LAST:event_editHover

	private void editOut(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_editOut
		// TODO add your handling code here:
		jButton2.setBackground(Color.blue);
	}// GEN-LAST:event_editOut

	private void addHover(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_addHover
		// TODO add your handling code here:
		jButton3.setBackground(Color.red);
	}// GEN-LAST:event_addHover

	private void addOut(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_addOut
		// TODO add your handling code here:
		jButton3.setBackground(Color.blue);
	}// GEN-LAST:event_addOut

	public static void changeAva() {
		String db = "jdbc:mysql://localhost/allchat";
		String user = "root";
		String pass = "12345678";

		try {
			Connection conn = DriverManager.getConnection(db, user, pass);
			java.sql.Statement stmt = conn.createStatement();
			String query = "UPDATE users SET available='0' WHERE BINARY username='" + username + "'";
			stmt.executeUpdate(query);
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void logout(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_logout
		// TODO add your handling code here:
		changeAva();
		dispose();
		t.stop();
		new loginScreen().setVisible(true);
	}// GEN-LAST:event_logout

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(mainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(mainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(mainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(mainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new mainScreen(null,null).setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
