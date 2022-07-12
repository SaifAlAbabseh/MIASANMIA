/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package all_chat;

/**
 *
 * @author sabab
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class chatWithFriendScreen extends javax.swing.JFrame {

	/**
	 * Creates new form chatWithFriendScreen
	 */
	String username,password, friend;
	String friendAva = "0";
	String tableName = "";
	int lastMessagesIndex = 0;
	private Timer t;
	private void setIcon() {
		setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
	}
	public chatWithFriendScreen(String username,String password, String friend) {
		this.username = username;
		this.password=password;
		this.friend = friend;
		initComponents();
		setIcon();
		loadEverything();
		setLocationRelativeTo(null);
	}

	private void loadEverything() {
		jLabel2.setText(friend);
		loadFriendProfile();
		loadMessages();
		t = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				loadFriendProfile();
				loadLatestMessage();
			}
		});
		t.start();
	}

	private String getTableName() {
		String TName = "";
		for (int i = 0; i < mainScreen.arrOfU1.size(); i++) {
			if (mainScreen.arrOfU1.get(i).equals(friend)) {

				TName += friend + "" + username;
				break;
			} else if (mainScreen.arrOfU2.get(i).equals(friend)) {
				TName += username + "" + friend;
				break;
			}
		}
		return TName;
	}

	private void showMessage(String fromWho, String message) {
		JPanel row = new JPanel();
		if (fromWho.equals(username)) {
			row.setLayout(new FlowLayout(FlowLayout.RIGHT));
		} else {
			row.setLayout(new FlowLayout(FlowLayout.LEFT));
		}

		JPanel column = new JPanel();
		column.setLayout(new GridLayout(2, 1));
		

		JLabel fromwho = new JLabel();
		fromwho.setText("From : " + fromWho);
		fromwho.setForeground(Color.blue);
		fromwho.setFont(new Font("Callibari", Font.PLAIN, 12));
		fromwho.setPreferredSize(fromwho.getMinimumSize());

		JLabel messagee = new JLabel();
		String decodeMessage="";
		try {
			decodeMessage=URLDecoder.decode(message,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,"Error", "Error", JOptionPane.ERROR_MESSAGE);
		}
		messagee.setText(decodeMessage);
		messagee.setForeground(Color.red);
		messagee.setFont(new Font("Callibari", Font.PLAIN, 12));
		messagee.setPreferredSize(messagee.getMinimumSize());

		column.add(fromwho);
		column.add(messagee);
		column.setPreferredSize(column.getMinimumSize());

		row.add(column);
		row.setPreferredSize(row.getMinimumSize());

		jPanel6.add(row);

	}

	private void scrollToBottom(JScrollPane scrollPane) {
		JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}

	private void loadLatestMessage() {
		try {

			Connection conn = DriverManager.getConnection(DBInfo.db, DBInfo.user, DBInfo.pass);
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM " + tableName + " ";
			ResultSet result = stmt.executeQuery(query);
			String fromwho = "", message = "";
			int lastPos = 0;
			while (result.next()) {
				lastPos = Integer.parseInt("" + result.getString(3));
				fromwho = result.getString(1);
				message = result.getString(2);
			}

			if (lastPos > lastMessagesIndex) {
				jPanel6.setLayout(new GridLayout(lastMessagesIndex+1,1));
				showMessage(fromwho, message);
				jPanel6.revalidate();
				lastMessagesIndex++;
				scrollToBottom(jScrollPane1);
			}
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void loadMessages() {

		try {

			Connection conn = DriverManager.getConnection(DBInfo.db, DBInfo.user, DBInfo.pass);
			Statement stmt = conn.createStatement();
			String tname = getTableName();
			tableName = tname;
			String query = "SELECT * FROM " + tname + " ";
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				lastMessagesIndex++;
			}
			jPanel6.setLayout(new GridLayout(lastMessagesIndex, 1));
			ResultSet result2 = stmt.executeQuery(query);
			while (result2.next()) {
				showMessage(result2.getString(1), result2.getString(2));
			}
			scrollToBottom(jScrollPane1);
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private String get() {
		String lastIMG = "";
		String ava = "";


		try {
			Connection conn = DriverManager.getConnection(DBInfo.db, DBInfo.user, DBInfo.pass);
			Statement stmt = conn.createStatement();
			String query = "SELECT picture,available FROM users WHERE BINARY username='" + friend + "'";
			ResultSet result = stmt.executeQuery(query);
			if (result.next()) {
				lastIMG = result.getString(1);
				if (result.getString(2).equals("0")) {
					ava = "0";
				} else if (result.getString(2).equals("1")) {
					ava = "1";
				}
			} else {
				dispose();
				t.stop();
				new mainScreen(username,password).setVisible(true);
			}
			stmt.close();
			conn.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		friendAva = ava;
		return lastIMG;
	}

	private void loadFriendProfile() {
		Image image = null;
		String lastIMG = get();
		String imgurl = "http://localhost/All_Chat/Extra/styles/images/" + lastIMG + ".png";
		try {
			URL url = new URL(imgurl);
			image = ImageIO.read(url);
			image.getScaledInstance(91, 67, Image.SCALE_DEFAULT);
			ImageIcon img = new ImageIcon(image);
			Image i = img.getImage();
			Image modified = i.getScaledInstance(91, 67, Image.SCALE_SMOOTH);
			jLabel3.setIcon(new ImageIcon(modified));
			if (friendAva.equals("0")) {
				jLabel1.setBackground(Color.red);
			} else if (friendAva.equals("1")) {
				jLabel1.setBackground(Color.green);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
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
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();

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

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4goToLink(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4hoverEffectOnLink(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4outEffectOnLink(evt);
            }
        });
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setBackground(new java.awt.Color(102, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Go Back");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusable(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1enterGoBackButton(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1exitGoBackButton(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1GoBackToMainScreen(evt);
            }
        });
        jPanel4.add(jButton1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setOpaque(true);

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 395, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel6);

        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 0, 0));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea1);

        jButton2.setBackground(java.awt.Color.blue);
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("SEND");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 429, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendMessage(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessage
        // TODO add your handling code here:
    	String message=jTextArea1.getText();
    	if(message.trim().equals("")) {
    		JOptionPane.showMessageDialog(null,"Empty Message.","Invalid Entry",JOptionPane.ERROR_MESSAGE);
    	}
    	else {
    		
    		try {
    			Connection conn = DriverManager.getConnection(DBInfo.db, DBInfo.user, DBInfo.pass);
    			Statement stmt = conn.createStatement();
    			String encodedMessage=URLEncoder.encode(message.trim(),"UTF-8");
    			String query="INSERT INTO "+tableName+" VALUES ('"+username+"','"+encodedMessage+"','"+(lastMessagesIndex+1)+"')";
    			stmt.executeUpdate(query);
    			stmt.close();
    			conn.close();
    			jTextArea1.setText("");
    		}
    		catch(Exception ex) {
    			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    }//GEN-LAST:event_sendMessage

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
		mainScreen.changeAva();
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

	private void jPanel4goToLink(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanel4goToLink
		try {
			Desktop.getDesktop().browse(new URI("http://allchat.lovestoblog.com/"));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_jPanel4goToLink

	private void jPanel4hoverEffectOnLink(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanel4hoverEffectOnLink
		// TODO add your handling code here:
		jPanel4.setBackground(Color.white);
	}// GEN-LAST:event_jPanel4hoverEffectOnLink

	private void jPanel4outEffectOnLink(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanel4outEffectOnLink
		// TODO add your handling code here:
		jPanel4.setBackground(new Color(51, 51, 51));
	}// GEN-LAST:event_jPanel4outEffectOnLink

	private void jButton1enterGoBackButton(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButton1enterGoBackButton
		// TODO add your handling code here:
		jButton1.setBackground(Color.white);
		jButton1.setForeground(Color.red);
		jButton1.setBorder(new LineBorder(Color.red, 3));
	}// GEN-LAST:event_jButton1enterGoBackButton

	private void jButton1exitGoBackButton(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jButton1exitGoBackButton
		// TODO add your handling code here:
		jButton1.setBackground(new Color(102, 102, 102));
		jButton1.setForeground(Color.white);
		jButton1.setBorder(new LineBorder(Color.white, 3));
	}// GEN-LAST:event_jButton1exitGoBackButton

	private void jButton1GoBackToMainScreen(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1GoBackToLoginScreen
		// TODO add your handling code here:
		dispose();
		t.stop();
		new mainScreen(username,password).setVisible(true);
	}// GEN-LAST:event_jButton1GoBackToLoginScreen

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
			java.util.logging.Logger.getLogger(chatWithFriendScreen.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(chatWithFriendScreen.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(chatWithFriendScreen.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(chatWithFriendScreen.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new chatWithFriendScreen(null, null,null).setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
