package com.chatapp.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.json.JSONObject;

import com.chatapp.server.ChatAppConstants;
import com.chatapp.server.ConnectionHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ClientApp {

	private String chatApp = "Chat";

	private JFrame newFrame = new JFrame(chatApp);

	private JButton sendMessage;

	private JTextField messageBox;

	private JTextArea chatBox;

	private JTextField usernameChooser;

	private JFrame preFrame;

	private String userName;

	private Channel receiveChannel;

	private Channel sendChannel;

	public ClientApp() {
		ConnectionHandler.getHandler().createConnection();
		receiveChannel = ConnectionHandler.getHandler().getReceiveChannel();
		sendChannel = ConnectionHandler.getHandler().getSendChannel();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ClientApp mainGUI = new ClientApp();
				mainGUI.preDisplay();
			}
		});
	}

	final Consumer consumer = new DefaultConsumer(receiveChannel) {
		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
				throws IOException {
			String message = new String(body, "UTF-8");
			chatBox.append(message);
			chatBox.append("\n");
			System.out.println(" [x] Received to user '" + message + "'");
		}
	};

	public void startConsuming() {
		try {
			receiveChannel.exchangeDeclare(ChatAppConstants.ROUTING_KEY, "topic");
			String queueName = receiveChannel.queueDeclare().getQueue();
			receiveChannel.queueBind(queueName, ChatAppConstants.ROUTING_KEY, "");
			receiveChannel.basicConsume(queueName, false, consumer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		try {
			JSONObject json = new JSONObject();
			json.put("user", userName);
			json.put("message", message);

			sendChannel.exchangeDeclare(ChatAppConstants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

			sendChannel.queueBind(ChatAppConstants.QUEUE_NAME, ChatAppConstants.EXCHANGE_NAME,
					ChatAppConstants.ROUTING_KEY);

			sendChannel.basicPublish(ChatAppConstants.EXCHANGE_NAME, ChatAppConstants.ROUTING_KEY, null,
					json.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preDisplay() {
		newFrame.setVisible(false);
		preFrame = new JFrame(chatApp);
		usernameChooser = new JTextField(15);
		JLabel chooseUsernameLabel = new JLabel("Pick a username:");
		JButton enterServer = new JButton("Enter Chat Server");
		enterServer.addActionListener(new enterServerButtonListener());
		JPanel prePanel = new JPanel(new GridBagLayout());

		GridBagConstraints preRight = new GridBagConstraints();
		preRight.insets = new Insets(0, 0, 0, 10);
		preRight.anchor = GridBagConstraints.EAST;
		GridBagConstraints preLeft = new GridBagConstraints();
		preLeft.anchor = GridBagConstraints.WEST;
		preLeft.insets = new Insets(0, 10, 0, 10);
		// preRight.weightx = 2.0;
		preRight.fill = GridBagConstraints.HORIZONTAL;
		preRight.gridwidth = GridBagConstraints.REMAINDER;

		prePanel.add(chooseUsernameLabel, preLeft);
		prePanel.add(usernameChooser, preRight);
		preFrame.add(BorderLayout.CENTER, prePanel);
		preFrame.add(BorderLayout.SOUTH, enterServer);
		preFrame.setSize(300, 300);
		preFrame.setVisible(true);

	}

	public void display() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		southPanel.setLayout(new GridBagLayout());

		messageBox = new JTextField(30);
		messageBox.requestFocusInWindow();

		sendMessage = new JButton("Send Message");
		sendMessage.addActionListener(new sendMessageButtonListener());

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
		chatBox.setLineWrap(true);

		mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;

		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1.0D;
		right.weighty = 1.0D;

		southPanel.add(messageBox, left);
		southPanel.add(sendMessage, right);

		mainPanel.add(BorderLayout.SOUTH, southPanel);

		newFrame.add(mainPanel);
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newFrame.setSize(470, 300);
		newFrame.setVisible(true);
	}

	class sendMessageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (messageBox.getText().length() < 1) {
				// do nothing
			} else if (messageBox.getText().equals(".clear")) {
				chatBox.setText("Cleared all messages\n");
				messageBox.setText("");
			} else {
				chatBox.append("\n<" + userName + ">:  " + messageBox.getText());
				sendMessage(messageBox.getText());
				messageBox.setText("");
			}
			messageBox.requestFocusInWindow();
		}
	}

	class enterServerButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			userName = usernameChooser.getText();
			if (userName.length() < 1) {
				System.out.println("No!");
			} else {
				preFrame.setVisible(false);
				display();
				startConsuming();
			}
		}
	}

}