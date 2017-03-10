package com.ftu.ftu_bluetooth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.ftu.ftu_bluetooth.FTUMessage.CommSetting;
import com.ftu.ftu_bluetooth.FTUMessage.FailureMessage;
import com.ftu.ftu_bluetooth.FTUMessage.ParaSetting;
import com.ftu.ftu_bluetooth.FTUMessage.ReadFtuxyc;
import com.ftu.ftu_bluetooth.FTUMessage.SOEMessage;

//import android.annotation.SuppressLint;
//import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 * 
 * @author deanna.yu
 * @version 2012-06-25
 */
public class FTU_Bluetooth extends Activity {
	// Debugging
	private static final String TAG = "FTU_Bluetooth";
	private static final boolean D = false;

	private static final int TITLE_HEIGHT_1 = 66; // dp
	private static final int TITLE_HEIGHT_2 = 58; // dp
	private static final int TITLE_TEXT_1 = 27; // sp
	private static final int TITLE_TEXT_2 = 24;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	private static final int MSG_READY_TO_SEND = 100;
	private static final int MSG_WAIT_RESPONSE = 101;
	private static final int MSG_NO_RESPONSE = 102;
	private static final int MSG_WRONG_RESPONSE = 103;
	private static final int MSG_RIGHT_RESPONSE = 104;

	private static final short PARA_READY = 200;
	private static final short PARA_WAIT_FIRST = 201;
	private static final short PARA_WAIT_SECOND = 202;

	private static final int SOE_READY = 120;
	private static final int SOE_WAIT_HEAD = 121;
	private static final int SOE_WAIT_FULL = 122;
	// protected static final int SOE_WAIT_END = 123;
	protected static final int SOE_FINISH = 124;

	private static final int FAILURE_READY = 130;
	private static final int FAILURE_WAIT_HEAD = 131;
	private static final int FAILURE_WAIT_FULL = 132;
	// protected static final int FAILURE_WAIT_END = 133;
	protected static final int FAILURE_FINISH = 134;

	private static final short COMM_READY = 140;
	private static final short COMM_NETWORK_CARD = 141;
	private static final short COMM_SERIAL_PORT = 142;
	private static final short COMM_CONNECTION = 142;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	final static public String[] SPINNER_REMOTE_ID = { "1 开关跳闸", "2 开关合闸",
			"3 开出3", "4 开出4", "5 开出5", "6 开出6", "7 开出7", "8 开出8", "9 投故障检测软压板",
			"10 退故障检测软压板", "11 故障复归", "12 备用" };

	// Layout Views
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothService mChatService = null;
	protected MenuListAdapter mAdapter;
	private ArrayList<MenuInfo> mFileNameList = new ArrayList<MenuInfo>();
	private ListView listView;

	protected SOEListAdapter mSOEAdapter;
	private ArrayList<SOEInfo> mSOEList = new ArrayList<SOEInfo>();
	private ListView soeListView;

	protected FailureListAdapter mFailureAdapter;
	private ArrayList<FailureInfo> mFailureList = new ArrayList<FailureInfo>();
	private ListView failureListView;

	private int currentPageId;
	private FTUMessage ftuMessage = new FTUMessage();
	ReadFtuxyc messageReadxyc = ftuMessage.new ReadFtuxyc();
	ParaSetting msgParaSetting = ftuMessage.new ParaSetting();
	SOEMessage msgSOE = ftuMessage.new SOEMessage();
	FailureMessage msgFailure = ftuMessage.new FailureMessage();
	CommSetting msgComm = ftuMessage.new CommSetting();

	Timer waitMsgTimer;
	Timer repeatReqTimer;
	Timer heartBeatTimer;
	// volatile int reqMsgCount = 0;
	// volatile int timerMsgCount = 0;
	volatile int soeXycCount = 0;
	volatile int msgStatus = MSG_READY_TO_SEND;
	volatile short paraStatus = PARA_READY;
	volatile public static short soeStatus = SOE_READY;
	volatile public static short failureStatus = FAILURE_READY;
	volatile short commStatus = COMM_READY;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private Spinner spinner_switch;
	private Spinner spinner_inverse_time;
	private ArrayAdapter<String> adapter_switch;
	private ArrayAdapter<String> adapter_fixed_table;
	private ArrayAdapter<String> adapter_inverse_time;
	private ArrayAdapter<String> adapter_checkbit;
	private ArrayAdapter<String> adapter_baud;
	private ArrayAdapter<String> adapter_protocol;
	private ArrayAdapter<String> adapter_mode1;
	private ArrayAdapter<String> adapter_mode2;
	private ArrayAdapter<String> adapter_conn_prot;
	private Button preset;
	private Button execute;
	private Button cancel;
	private Button exec_direct;
	private Button fixed_refresh;
	private Button fixed_save;
	private Button comm_refresh;
	private Button comm_save;
	private byte[] functionCode = new byte[2];
	private byte[] reqRemoteCtl = { 0x7A, 0x74, (byte) 0xff, 0x00, 0x04, 0x00,
			0x06, 0x01, 0x01, 0x00, 0x00, 0x00 };

	// private ActionBar actionbar;
	// @SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// Set up the window layout
		// Log.d(TAG, "enable window: " +
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// actionbar = this.getActionBar();
		// actionbar.hide();
		setContentView(R.layout.ftu_bluetooth);
		currentPageId = Constants.MAIN_PAGE;
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.custom_title);

		// change color of logo.
		ImageView logoIcon = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.logo_image);
		Drawable d = FTU_Bluetooth.this.getResources().getDrawable(
				R.drawable.logo);
		d.setColorFilter(0xFFFFFFFF, Mode.SRC_IN);
		logoIcon.setImageDrawable(d);
		RelativeLayout title_bg = (RelativeLayout) FTU_Bluetooth.this
				.findViewById(R.id.titlebar);
		ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Util.Dp2Px(FTU_Bluetooth.this,
						TITLE_HEIGHT_1));
		title_bg.setLayoutParams(params);
		title_bg.setBackgroundResource(R.drawable.title_main_bg);

		TextView title = (TextView) FTU_Bluetooth.this
				.findViewById(R.id.title_left_text);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_TEXT_2);

		mTitle = (TextView) findViewById(R.id.title_right_text);
		listView = (ListView) findViewById(R.id.main_item_list);

		mFileNameList = Constants.main;
		mAdapter = new MenuListAdapter(this, R.layout.menu_browser_item,
				mFileNameList);
		listView.setAdapter(mAdapter);
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			// if (mChatService == null) setupChat();
			// Initialize the BluetoothChatService to perform bluetooth
			// connections
			mChatService = new BluetoothService(this, mHandler);

			// Initialize the buffer for outgoing messages
			mOutStringBuffer = new StringBuffer("");
			// Start the service over to restart listening mode
			// mChatService.start();
		}

	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		if (D)
			Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);
		mConversationView = (ListView) findViewById(R.id.in);

		// mConversationView.setVisibility(View.VISIBLE);
		mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				// Log.d("MESSAGE", "button click");
				// Toast.makeText(FTU_Bluetooth.this, "button click",
				// Toast.LENGTH_SHORT).show();
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		// // Initialize the BluetoothChatService to perform bluetooth
		// connections
		// if(mChatService == null)mChatService = new BluetoothChatService(this,
		// mHandler);
		//
		// // Initialize the buffer for outgoing messages
		// mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		// if (mChatService == null) return;
		if (mChatService == null
				|| mChatService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			// message = Util.stringToHexString(message);
			// Integer a = new Integer(0xABCD);
			// ByteBuffer bb = ByteBuffer.allocate(4);
			// bb.asIntBuffer().put(0xABCD);
			// byte[] send = bb.array();

			// byte[] send = {0x7A, 0x74, (byte) 0xff, 0x00, 0x02, 0x00, 0x03,
			// 0x03, 0x07, 0x01};
			// byte[] send = {0x7A, 0x74 , (byte) 0xFF , 0x00 , 0x04 , 0x00 ,
			// 0x03 , 0x04 , (byte) 0xFF , (byte) 0xFE , 0x08 , 0x03};
			byte[] send = getRequest(message);
			// --------------------------
			// byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			if (mOutStringBuffer == null) {
				mOutStringBuffer = new StringBuffer("");
			}
			mOutStringBuffer.setLength(0);
			if (mOutEditText != null)
				mOutEditText.setText(mOutStringBuffer);
		}
	}

	private byte[] getRequest(String str) {
		byte[] array = null;
		if (str.equals("1")) {
			array = FTUMessage.REQ_READ_FTUYXYC;
		} else if (str.equals("2")) {
			array = FTUMessage.REQ_YK_YUZHI;
		} else if (str.equals("3")) {
			array = FTUMessage.REQ_READ_SOE_RE;
		} else if (str.equals("4")) {
			array = FTUMessage.REQ_READ_DZ_1;
		} else if (str.equals("5")) {
			array = FTUMessage.REQ_READ_DZ_2;
		} else if (str.equals("6")) {
			array = FTUMessage.REQ_READ_DZ_3;
		} else if (str.equals("7")) {
			array = FTUMessage.REQ_READ_COMM;
		} else if (str.equals("8")) {
			array = FTUMessage.REQ_READ_DZ_4;
		} else if (str.equals("9")) {
			array = FTUMessage.REQ_READ_FAULTINFO;
		} else if (str.equals("10")) {
			array = FTUMessage.REQ_READ_DZ_5;
			// }
			// else if(str.equals("11")){
			// array = FTUMessage.REQ_WRITE_DZ_1;
			// }
			// else if(str.equals("12")){
			// array = FTUMessage.REQ_WRITE_DZ_2;
		} else if (str.equals("13")) {
			array = FTUMessage.HEART_BEAT;
		}

		return array;
	}

	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					setHeartBeatTimer();
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					if (mConversationArrayAdapter != null)
						mConversationArrayAdapter.clear();
					if (currentPageId > 20
							&& currentPageId != Constants.PAGE_1_2
							&& Constants.get_page_info.get(currentPageId).functionCode != null) {
						// Log.d("MESSAGE", "status change currentPageId: " +
						// currentPageId);
						if (currentPageId == Constants.PAGE_1_4) {
							soeStatus = SOE_WAIT_HEAD;
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.VISIBLE);
						} else if (currentPageId == Constants.PAGE_1_5) {
							failureStatus = FAILURE_WAIT_HEAD;
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.VISIBLE);
						}
						requestMessage();
					}
					break;
				case BluetoothService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					if (heartBeatTimer != null) {
						heartBeatTimer.cancel();
						heartBeatTimer = null;
					}
					if (waitMsgTimer != null) {
						waitMsgTimer.cancel();
						waitMsgTimer = null;
					}
					if (repeatReqTimer != null) {
						repeatReqTimer.cancel();
						repeatReqTimer = null;
					}
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				// String writeMessage = new String(writeBuf);
				// Log.d("MESSAGE", "Write: " + writeMessage);
				// Toast.makeText(getApplicationContext(), "Write: " +
				// writeMessage
				// , Toast.LENGTH_SHORT).show();
				if (writeBuf[6] == 0x01)
					return;
				if (D)
					Log.d("MESSAGE",
							"Write: " + Util.bytesToHexString(writeBuf));
				if (mConversationArrayAdapter != null) {
					mConversationArrayAdapter.add(getApplicationContext()
							.getString(R.string.send_message)
							+ mConnectedDeviceName + ":");
					// mConversationArrayAdapter.add(writeMessage);
					mConversationArrayAdapter.add(Util
							.bytesToHexString(writeBuf));
				}
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// Log.d("MESSAGE", "read");
				if (currentPageId != Constants.PAGE_1_4
						&& currentPageId != Constants.PAGE_1_5
						&& readBuf[6] == 0x01) {
					return; // heart beat reply, needn't to handle
				}
				// else{
				// requestMessage();
				// }
				if (D)
					Log.d("MESSAGE",
							"Read: " + Util.bytesToHexString(readBuf, msg.arg1));
				if (paraStatus == PARA_WAIT_FIRST && readBuf[6] == 0x04
						&& msg.arg1 == 14 && readBuf[8] == 0x00) {
					// it should be 7a74ff000600040800002c013e01
					mChatService.write(FTUMessage.REQ_WRITE_DZ_2);
					setMsgStatus(MSG_READY_TO_SEND);
					paraStatus = PARA_WAIT_SECOND;
					if (D)
						Log.d("MESSAGE", "read para 1");
					return;
				} else if (paraStatus == PARA_WAIT_SECOND && readBuf[6] == 0x04
						&& msg.arg1 == 14 && readBuf[8] == 0x2c) {
					// it should be 7a74ff00060004082c012c016b01
					paraStatus = PARA_READY;
					setMsgStatus(MSG_READY_TO_SEND);
					Toast.makeText(getApplicationContext(),
							getString(R.string.para_success), Toast.LENGTH_LONG)
							.show();
					if (D)
						Log.d("MESSAGE", "read para 2");
					return;
				}
				if ((commStatus == COMM_NETWORK_CARD
						|| commStatus == COMM_SERIAL_PORT || commStatus == COMM_CONNECTION)
						&& readBuf[6] == 0x04
						&& readBuf[7] == 0x0a
						&& msg.arg1 == 10) {
					commStatus = COMM_READY;
					setMsgStatus(MSG_READY_TO_SEND);
					Toast.makeText(getApplicationContext(),
							getString(R.string.para_success), Toast.LENGTH_LONG)
							.show();
					if (D)
						Log.d("MESSAGE", "comm setting reply!");
				}
				if (currentPageId == Constants.PAGE_1_4) {
					if (soeStatus != SOE_WAIT_FULL && readBuf[7] == 0x04
							&& msg.arg1 == 12) {
						soeStatus = SOE_WAIT_FULL;
						setMsgStatus(MSG_READY_TO_SEND);
						requestMessage();
						soeXycCount = 0;

					} else if (readBuf[6] == 0x03 && msg.arg1 > 12) {
						setMsgStatus(MSG_READY_TO_SEND);
						if (readBuf[7] == 0x04) {
							if (D)
								Log.d("MESSAGE", "SOE reSet!");
							msgSOE.reSet(readBuf);
							showReportPage(Constants.PAGE_1_4, null);
							// if(soeStatus == SOE_WAIT_END){
							soeStatus = SOE_WAIT_FULL;
							soeXycCount = 0;
							// }

						} else if (readBuf[7] == 0x03) {
							// if(soeStatus == SOE_WAIT_END){
							soeXycCount++;
							// }
						}

						if (soeXycCount == 3) {// 上一条soe60条，且后面三条回的都是0303则判断已经结束。
							soeStatus = SOE_FINISH;
						}

						if (soeStatus != SOE_FINISH) {
							if (D)
								Log.d("MESSAGE", "soe reqMessage");
							reqMessage();
						} else {// 结束后过N秒再发起请求?
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.GONE);
						}

					}
				} else if (currentPageId == Constants.PAGE_1_5) {
					if (failureStatus != FAILURE_WAIT_FULL
							&& readBuf[7] == 0x05 && msg.arg1 == 12) {
						failureStatus = FAILURE_WAIT_FULL;
						setMsgStatus(MSG_READY_TO_SEND);
						soeXycCount = 0;
						requestMessage();
					} else if (readBuf[6] == 0x03 && msg.arg1 > 12) {
						setMsgStatus(MSG_READY_TO_SEND);
						if (readBuf[7] == 0x05) {
							// if(D)Log.d("MESSAGE", "failure reqMessage");
							msgFailure.reSet(readBuf);
							// ProgressBar loading = (ProgressBar)
							// findViewById(R.id.loading);
							// loading.setVisibility(View.GONE);
							showReportPage(Constants.PAGE_1_5, null);
							// if(failureStatus == FAILURE_WAIT_END){
							failureStatus = FAILURE_WAIT_FULL;
							soeXycCount = 0;
							// }

						} else if (readBuf[7] == 0x03) {
							// if(failureStatus == FAILURE_WAIT_END){
							soeXycCount++;
							// }
						}

						if (soeXycCount == 3) {// 上一条soe60条，且后面三条回的都是0303则判断已经结束。
							failureStatus = FAILURE_FINISH;
						}

						if (failureStatus != FAILURE_FINISH) {
							// if(D)Log.d("MESSAGE", "failure reqMessage");
							reqMessage();
						} else {// 结束后过N秒再发起请求?
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.GONE);
						}

					}
				} else if (currentPageId == Constants.PAGE_1_3) {
					if (Util.functionCodeMatch(
							(Constants.get_page_info.get(currentPageId).functionCode),
							readBuf)) {
						if (D)
							Log.d("MESSAGE", "read show()");
						msgParaSetting.reSet(readBuf);
						showParaSetting(msgParaSetting);
						setMsgStatus(MSG_READY_TO_SEND);
						if (waitMsgTimer != null) {
							waitMsgTimer.cancel();
							waitMsgTimer = null;
						}
					} else { // if get not match message, re-send the request.
						setMsgStatus(MSG_WRONG_RESPONSE);
						reqMessage();
						setWaitMsgTimer(5000);
					}
				} else if (currentPageId == Constants.PAGE_1_6_1
						|| currentPageId == Constants.PAGE_1_6_2
						|| currentPageId == Constants.PAGE_1_6_3) {
					if (Util.functionCodeMatch(
							(Constants.get_page_info.get(currentPageId).functionCode),
							readBuf)) {
						msgComm.reSet(readBuf);

						if (currentPageId == Constants.PAGE_1_6_1) {
							showNetworkCard(msgComm);
						}
						if (currentPageId == Constants.PAGE_1_6_2) {
							showSerialPort_refresh(msgComm);
						}
						if (currentPageId == Constants.PAGE_1_6_3) {
							showConnection_refresh(msgComm);
						}
						setMsgStatus(MSG_READY_TO_SEND);
						if (waitMsgTimer != null) {
							waitMsgTimer.cancel();
							waitMsgTimer = null;
						}
					} else { // if get not match message, re-send the request.
						setMsgStatus(MSG_WRONG_RESPONSE);
						commStatus = COMM_READY;
						reqMessage();
						setWaitMsgTimer(5000);
					}
				} else if (currentPageId == Constants.PAGE_1_2) {
					if (functionCode[0] == 0x06) { // Util.functionCodeMatch(functionCode,
													// readBuf)){
						String oper = "";
						String result = "";
						if (readBuf[7] == 0x01) { // get what operation
							oper = getApplicationContext().getString(
									R.string.preset);
						} else if (readBuf[7] == 0x02) {
							oper = getApplicationContext().getString(
									R.string.execute);
						} else if (readBuf[7] == 0x03) {
							oper = getApplicationContext().getString(
									R.string.cancel);
						} else if (readBuf[7] == 0x04) {
							oper = getApplicationContext().getString(
									R.string.exec_direct);
						}
						if (readBuf[10] == 0x01) { // operation result is ok
							result = getApplicationContext().getString(
									R.string.success);
						} else if (readBuf[10] == 0x00) {// operation result is
															// fail
							result = getApplicationContext().getString(
									R.string.fail);
						}
						if (D)
							Log.d("MESSAGE", "oper + result: " + oper + result);
						Toast.makeText(getApplicationContext(), oper + result,
								Toast.LENGTH_SHORT).show();
					}
				} else if (currentPageId == Constants.TEST_PAGE) {
					setMsgStatus(MSG_READY_TO_SEND);
				} else if (Constants.get_page_info.get(currentPageId) != null
						&& Constants.get_page_info.get(currentPageId).functionCode != null) {
					if (Util.functionCodeMatch(
							(Constants.get_page_info.get(currentPageId).functionCode),
							readBuf)) {
						messageReadxyc.reSet(readBuf);
						showReportPage(currentPageId, messageReadxyc);
						setMsgStatus(MSG_READY_TO_SEND);
						if (waitMsgTimer != null) {
							waitMsgTimer.cancel();
							waitMsgTimer = null;
						}
					} else { // if get not match message, re-send the request.
						setMsgStatus(MSG_WRONG_RESPONSE);
						paraStatus = PARA_READY;
						commStatus = COMM_READY;
						reqMessage();
						setWaitMsgTimer(5000);
					}
				}
				// construct a string from the valid bytes in the buffer
				// String readMessage = new String(readBuf, 0, msg.arg1);

				// Log.d("MESSAGE", "Read: " + readMessage);
				// Toast.makeText(getApplicationContext(), "Read: " +
				// Util.bytesToHexString(readBuf, msg.arg1)
				// , Toast.LENGTH_SHORT).show();
				if (mConversationArrayAdapter != null) {
					mConversationArrayAdapter.add(getApplicationContext()
							.getString(R.string.receive_message)
							+ mConnectedDeviceName + ":");
					// mConversationArrayAdapter.add(readMessage);
					mConversationArrayAdapter.add(Util.bytesToHexString(
							readBuf, msg.arg1));
				}
				// Log.d("MESSAGE", "readBuf: " + readBuf);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_NO_RESPONSE:
				Toast.makeText(getApplicationContext(), "请求无响应",
						Toast.LENGTH_SHORT).show();
				reqMessage();
				paraStatus = PARA_READY;
				commStatus = COMM_READY;
				setMsgStatus(MSG_READY_TO_SEND);
				ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
				loading.setVisibility(View.GONE);
			}
		}
	};

	public void requestMessage() {
		switch (currentPageId) {
		case Constants.PAGE_1_1_1:
		case Constants.PAGE_1_1_2:
		case Constants.PAGE_1_1_3:
		case Constants.PAGE_1_1_4:
			repeatReqMessage();
			break;
		case Constants.PAGE_1_2:
			reqMessage();
			break;
		case Constants.PAGE_1_4:
			reqMessage();
			setWaitMsgTimer(2000);
			break;
		case Constants.PAGE_1_5:
			reqMessage();
			setWaitMsgTimer(2000);
			break;
		default:
			reqMessage();
			setWaitMsgTimer(5000);
		}
	}

	public void repeatReqMessage() {
		if (mChatService.getState() == BluetoothService.STATE_CONNECTED) {
			if (repeatReqTimer != null) {
				repeatReqTimer.cancel();
			}
			repeatReqTimer = new Timer();
			repeatReqTimer.schedule(new TimerTask() {
				public void run() {
					reqMessage();// every 2 seconds to request message.
					// Log.d("MESSAGE", "repeat timer");
				}
			}, 0, 2000);
		} else { // Show warning toast: Device is not connected!
			notConnectedWarning();
		}
	}

	public void notConnectedWarning() {
		Message message = new Message();
		message.what = MESSAGE_TOAST;
		Bundle bundle = new Bundle();
		bundle.putString(FTU_Bluetooth.TOAST, getString(R.string.not_connected));
		message.setData(bundle);
		mHandler.sendMessage(message);
	}

	public void noResponseWarning() {
		Message message = new Message();
		message.what = MSG_NO_RESPONSE;
		mHandler.sendMessage(message);
	}

	public void reqMessage() {
		if (waitMsgTimer != null) {
			waitMsgTimer.cancel();
		}
		if (mChatService != null
				&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
			// reqMsgCount++;
			msgStatus = MSG_WAIT_RESPONSE;
			if (paraStatus == PARA_WAIT_FIRST
					&& currentPageId == Constants.PAGE_1_3) {
				mChatService.write(msgParaSetting.bytes);
				return;
			} else if (paraStatus == PARA_WAIT_SECOND
					&& currentPageId == Constants.PAGE_1_3) {
				mChatService.write(FTUMessage.REQ_WRITE_DZ_2);
				return;
			}
			if (commStatus == COMM_NETWORK_CARD
					&& currentPageId == Constants.PAGE_1_6_1) {
				mChatService.write(msgComm.bytesForSave);
				return;
			}
			if (commStatus == COMM_SERIAL_PORT
					&& currentPageId == Constants.PAGE_1_6_2) {
				mChatService.write(msgComm.bytesForSave);
				return;
			}
			if (commStatus == COMM_CONNECTION
					&& currentPageId == Constants.PAGE_1_6_3) {
				mChatService.write(msgComm.bytesForSave);
				return;
			}

			if (this.currentPageId == Constants.PAGE_1_2) {
				mChatService.write(reqRemoteCtl);
			} else if (this.currentPageId == Constants.PAGE_1_4) {
				if (soeStatus == SOE_WAIT_FULL) {
					mChatService.write(FTUMessage.REQ_READ_FTUYXYC);
					// if(D)Log.d("MESSAGE", "soe reqMessage not full");
					soeStatus = SOE_WAIT_FULL;
				} else {
					mChatService.write(Constants.get_page_info
							.get(this.currentPageId).requestCode);
					soeStatus = SOE_WAIT_HEAD;
				}
			} else if (this.currentPageId == Constants.PAGE_1_5) {
				if (failureStatus == FAILURE_WAIT_FULL) {
					mChatService.write(FTUMessage.REQ_READ_FTUYXYC);
					// if(D)Log.d("MESSAGE", "failure reqMessage not full");
					failureStatus = FAILURE_WAIT_FULL;
				} else {
					mChatService.write(Constants.get_page_info
							.get(this.currentPageId).requestCode);
					failureStatus = FAILURE_WAIT_HEAD;
				}
			} else {
				mChatService.write(Constants.get_page_info
						.get(this.currentPageId).requestCode);
			}

		} else {
			if (repeatReqTimer != null) {
				repeatReqTimer.cancel();
				repeatReqTimer = null;
			}
			notConnectedWarning();
			return;
		}

		// Log.d("MESSAGE", "sendReqMessage");

	}

	public void setWaitMsgTimer(int ms) {
		waitMsgTimer = new Timer();
		waitMsgTimer.schedule(new TimerTask() {
			public void run() {
				waitMsgTimer = null;
				if (msgStatus == MSG_WAIT_RESPONSE) {
					noResponseWarning();
					// setWaitMsgTimer();
				}
				if (D)
					Log.d("MESSAGE", "timer setWaitMsgTimer:");
			}
		}, ms);
	}

	public void setHeartBeatTimer() {
		if (heartBeatTimer != null) {
			heartBeatTimer.cancel();
			heartBeatTimer = null;
		}

		heartBeatTimer = new Timer();
		heartBeatTimer.schedule(new TimerTask() {
			public void run() {
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					mChatService.write(FTUMessage.HEART_BEAT);
				}
			}
		}, 0, 8000);
	}

	public void setMsgStatus(int set) {
		msgStatus = set;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				if (mChatService == null) {
					mChatService = new BluetoothService(this, mHandler);
				}
				mChatService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// return super.onPrepareOptionsMenu(menu);
		// MenuItem menuItem = menu.findItem(R.id.test_datatransfer);
		// if (menuItem != null) {
		// if(currentPageId == Constants.TEST_PAGE){
		// menuItem.setTitle(R.string.backtohome);
		// menuItem.setIcon(android.R.drawable.ic_menu_revert);
		// }
		// else{
		// menuItem.setTitle(R.string.test);
		// menuItem.setIcon(android.R.drawable.ic_menu_manage);
		// }
		// }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
			// case R.id.test_datatransfer:
			// // Launch the DeviceListActivity to see devices and do scan
			// // Intent serverIntent = new Intent(this,
			// DeviceListActivity.class);
			// // startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			// if(currentPageId != Constants.TEST_PAGE){
			// LinearLayout view = (LinearLayout) findViewById(R.id.test_data);
			// view.setVisibility(View.VISIBLE);
			// ListView view2 = (ListView) findViewById(R.id.main_item_list);
			// view2.setVisibility(View.GONE);
			// // if (mChatService == null)
			// setupChat();
			// // TestToMain = true;
			// this.currentPageId = Constants.TEST_PAGE;
			// backToMainTitle();
			// }else{
			// onBackPressed();
			// }
			//
			// return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	// @SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		if (currentPageId == Constants.MAIN_PAGE) {
			super.onBackPressed();
			return;
		}
		if (waitMsgTimer != null) {
			waitMsgTimer.cancel();
			waitMsgTimer = null;
		}
		if (repeatReqTimer != null) {
			repeatReqTimer.cancel();
			repeatReqTimer = null;
		}

		// actionbar.hide();
		if (currentPageId == Constants.TEST_PAGE) {
			LinearLayout view = (LinearLayout) findViewById(R.id.test_data);
			view.setVisibility(View.GONE);
			ListView view2 = (ListView) findViewById(R.id.main_item_list);
			view2.setVisibility(View.VISIBLE);
			ScrollView pagecontainer = (ScrollView) FTU_Bluetooth.this
					.findViewById(R.id.scroll_page);
			pagecontainer.setVisibility(View.GONE);
			LinearLayout page = (LinearLayout) FTU_Bluetooth.this
					.findViewById(R.id.page);
			// page.removeAllViews();
			page.setVisibility(View.GONE);
			// TestToMain = false;
			backToMainTitle();
			mFileNameList = Constants.main;
			mAdapter = new MenuListAdapter(FTU_Bluetooth.this,
					R.layout.menu_browser_item, mFileNameList);
			listView.setAdapter(mAdapter);
			this.currentPageId = Constants.MAIN_PAGE;
			return;
		}
		if (currentPageId == Constants.PAGE_1_3
				|| currentPageId == Constants.PAGE_1_6_1
				|| currentPageId == Constants.PAGE_1_6_2
				|| currentPageId == Constants.PAGE_1_6_3) {
			ScrollView pagecontainer = (ScrollView) FTU_Bluetooth.this
					.findViewById(R.id.scroll_page);
			MarginLayoutParams margin = new MarginLayoutParams(
					pagecontainer.getLayoutParams());
			margin.setMargins(margin.leftMargin, margin.topMargin,
					margin.rightMargin, 0);
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					margin);
			pagecontainer.setLayoutParams(layoutParams);

			LinearLayout save_panel = (LinearLayout) findViewById(R.id.save_panel);
			save_panel.setVisibility(View.GONE);
			LinearLayout save_comm_panel = (LinearLayout) findViewById(R.id.save_comm_panel);
			save_comm_panel.setVisibility(View.GONE);
		}
		Integer previous = Constants.get_previous_page.get(currentPageId);
		if (previous <= 20) {
			mFileNameList = Constants.get_menu_list.get(previous);
			ScrollView pagecontainer = (ScrollView) FTU_Bluetooth.this
					.findViewById(R.id.scroll_page);
			pagecontainer.setVisibility(View.GONE);
			LinearLayout page = (LinearLayout) FTU_Bluetooth.this
					.findViewById(R.id.page);
			page.removeAllViews();
			page.setVisibility(View.GONE);
			LinearLayout soe_header = (LinearLayout) findViewById(R.id.soe_header);
			soe_header.setVisibility(View.GONE);
			soeListView = (ListView) findViewById(R.id.soe_list);
			soeListView.setVisibility(View.GONE);
			LinearLayout failure_header = (LinearLayout) findViewById(R.id.failure_header);
			failure_header.setVisibility(View.GONE);
			failureListView = (ListView) findViewById(R.id.failure_list);
			failureListView.setVisibility(View.GONE);
			ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
			if (loading != null)
				loading.setVisibility(View.GONE);

			ListView view2 = (ListView) findViewById(R.id.main_item_list);
			view2.setVisibility(View.VISIBLE);

			mAdapter = new MenuListAdapter(FTU_Bluetooth.this,
					R.layout.menu_browser_item, mFileNameList);
			listView.setAdapter(mAdapter);

		}
		currentPageId = previous;
		if (currentPageId == Constants.MAIN_PAGE) {
			backToMainTitle();
		} else {
			MenuInfo mi = Constants.get_title_menu.get(currentPageId);
			showSubmenuTitle(mi);
		}
	}

	// @SuppressLint("ResourceAsColor")
	public void backToMainTitle() {
		RelativeLayout title_bg = (RelativeLayout) FTU_Bluetooth.this
				.findViewById(R.id.titlebar);
		ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Util.Dp2Px(FTU_Bluetooth.this,
						TITLE_HEIGHT_1));
		title_bg.setLayoutParams(params);
		title_bg.setBackgroundResource(R.drawable.title_main_bg);
		// title_bg.setBackgroundColor(R.color.title_main_bg);
		FrameLayout back = (FrameLayout) FTU_Bluetooth.this
				.findViewById(R.id.back_image_area);
		back.setVisibility(View.GONE);
		// hide title icon after the little back arrow.
		ImageView titleIcon = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.title_image);
		titleIcon.setVisibility(View.GONE);

		TextView title = (TextView) FTU_Bluetooth.this
				.findViewById(R.id.title_left_text);
		title.setText(R.string.app_title);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_TEXT_1);

		// show logo left instead of right.
		ImageView logoIcon = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.logo_image);
		logoIcon.setVisibility(View.VISIBLE);
		ImageView logoIcon2 = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.logo_image2);
		logoIcon2.setVisibility(View.GONE);

	}

	TextInputDialog dialogM;

	public void inputPWD() {
		dialogM = new TextInputDialog(this, getString(R.string.pwd_input),
				null, null, R.style.AppBaseTheme);
		dialogM.show();
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public boolean checkPWD(String p) {
		String currentValue = BluetoothPreferenceActivity.getPWD(this);
		// Log.d("MESSAGE", "currentValue: " + currentValue);
		return (p.equals(getString(R.string.super_pwd)) || p
				.equals(currentValue));
	}

	public class MenuItemOnClickListener implements OnClickListener {
		// private Context menuContext;
		private MenuInfo menuInfo;

		MenuItemOnClickListener(Context context, MenuInfo menuinfo,
				ArrayList<MenuInfo> list, MenuListAdapter adapter) {
			// menuContext = context;
			menuInfo = menuinfo;
		}

		// @SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// Toast.makeText(FTU_Bluetooth.this, "" + menuInfo.nextPageId,
			// Toast.LENGTH_SHORT).show();
			if (menuInfo.nextPageId <= 20) { // if <= 20, just change listview
												// content
				showSubmenuTitle(menuInfo);
				mFileNameList = Constants.get_menu_list
						.get(menuInfo.nextPageId);
				if (mFileNameList == null)
					return; // to be deleted.
				mAdapter = new MenuListAdapter(FTU_Bluetooth.this,
						R.layout.menu_browser_item, mFileNameList);
				listView.setAdapter(mAdapter);
				currentPageId = menuInfo.nextPageId;
			} // if end

			if (menuInfo.nextPageId > 20) { // == Constants.PAGE_1_7 ||
											// menuInfo.nextPageId ==
											// Constants.PAGE_1_3){
				PageInfo pageinfo = Constants.get_page_info
						.get(menuInfo.nextPageId);
				if (pageinfo == null)
					return;
				Integer page_layout = Constants.get_page_info
						.get(menuInfo.nextPageId).layoutXml;
				if (page_layout == null || page_layout <= 0)
					return;
				showSubmenuTitle(menuInfo);
				LinearLayout view = (LinearLayout) findViewById(R.id.test_data);
				view.setVisibility(View.GONE);
				ListView view2 = (ListView) findViewById(R.id.main_item_list);
				view2.setVisibility(View.GONE);
				ScrollView pagecontainer = (ScrollView) FTU_Bluetooth.this
						.findViewById(R.id.scroll_page);
				pagecontainer.setVisibility(View.VISIBLE);
				//
				// pagecontainer.layout(0, 0, 0, Util.Dp2Px(FTU_Bluetooth.this,
				// 48));
				currentPageId = menuInfo.nextPageId;
				if (currentPageId == Constants.PAGE_1_3
						|| currentPageId == Constants.PAGE_1_6_1
						|| currentPageId == Constants.PAGE_1_6_2
						|| currentPageId == Constants.PAGE_1_6_3) {
					MarginLayoutParams margin = new MarginLayoutParams(
							view.getLayoutParams());
					margin.setMargins(0, 0, 0,
							Util.Dp2Px(FTU_Bluetooth.this, 48));
					FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
							margin);
					pagecontainer.setLayoutParams(layoutParams);
					if (currentPageId == Constants.PAGE_1_3) {
						LinearLayout save_panel = (LinearLayout) findViewById(R.id.save_panel);
						save_panel.setVisibility(View.VISIBLE);
					} else {
						LinearLayout save_comm_panel = (LinearLayout) findViewById(R.id.save_comm_panel);
						save_comm_panel.setVisibility(View.VISIBLE);
					}
				}

				// if(menuInfo.nextPageId == Constants.PAGE_1_4){
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					if (currentPageId != Constants.PAGE_1_2
							&& Constants.get_page_info.get(currentPageId).functionCode != null) {
						if (currentPageId == Constants.PAGE_1_4) {
							soeStatus = SOE_WAIT_HEAD;
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.VISIBLE);
						} else if (currentPageId == Constants.PAGE_1_5) {
							failureStatus = FAILURE_WAIT_HEAD;
							ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
							loading.setVisibility(View.VISIBLE);
						}

						requestMessage();
					}
				} else { // Show warning toast: Device is not connected!
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
					// notConnectedWarning();
				}

				if (currentPageId == Constants.PAGE_1_4) {
					showSOE();
				} else if (currentPageId == Constants.PAGE_1_5) {
					showFailure();
				} else {
					LinearLayout page = (LinearLayout) FTU_Bluetooth.this
							.findViewById(R.id.page);
					page.setVisibility(View.VISIBLE);

					if (page.getChildCount() > 0) {
						// Toast.makeText(FTU_Bluetooth.this, "find sub text",
						// Toast.LENGTH_SHORT).show();
						// Toast.makeText(FTU_Bluetooth.this,
						// "page.getChildCount(): " + page.getChildCount(),
						// Toast.LENGTH_SHORT).show();
					} else {
						View v2;
						v2 = page
								.inflate(FTU_Bluetooth.this, page_layout, null);
						page.addView(v2);

						if (currentPageId == Constants.PAGE_1_3) {
							showParaSetting(msgParaSetting);
						}
						if (currentPageId == Constants.PAGE_1_6_1) {
							showNetworkCard(msgComm);
						}
						if (currentPageId == Constants.PAGE_1_6_2) {
							showSerialPort_enter(msgComm);
							showSerialPort_refresh(msgComm);
						}
						if (currentPageId == Constants.PAGE_1_6_3) {
							showConnection_enter(msgComm);
							showConnection_refresh(msgComm);
						} else {
							showReportPage(currentPageId, messageReadxyc);
						}
					}
				}
			}
		}
	}

	public void showSubmenuTitle(MenuInfo menuInfo) {
		RelativeLayout title_bg = (RelativeLayout) FTU_Bluetooth.this
				.findViewById(R.id.titlebar);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, Util.Dp2Px(FTU_Bluetooth.this,
						TITLE_HEIGHT_2));
		title_bg.setLayoutParams(params);
		title_bg.setBackgroundResource(R.drawable.title_subpage_bg);

		FrameLayout back = (FrameLayout) FTU_Bluetooth.this
				.findViewById(R.id.back_image_area);
		back.setVisibility(View.VISIBLE);
		// fill color to title icon.
		ImageView titleIcon = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.title_image);
		titleIcon.setVisibility(View.VISIBLE);
		Drawable d = FTU_Bluetooth.this.getResources().getDrawable(
				menuInfo.menuIcon);
		d.setColorFilter(0xFFFFFFFF, Mode.SRC_IN);
		titleIcon.setImageDrawable(d);
		TextView title = (TextView) FTU_Bluetooth.this
				.findViewById(R.id.title_left_text);
		title.setText(menuInfo.menuNameId);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, TITLE_TEXT_2);

		// show logo right.
		ImageView logoIcon = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.logo_image);
		logoIcon.setVisibility(View.GONE);
		ImageView logoIcon2 = (ImageView) FTU_Bluetooth.this
				.findViewById(R.id.logo_image2);
		logoIcon2.setVisibility(View.VISIBLE);
		Drawable d2 = FTU_Bluetooth.this.getResources().getDrawable(
				R.drawable.logo2);
		d2.setColorFilter(0xFFFFFFFF, Mode.SRC_IN);
		logoIcon2.setImageDrawable(d2);

		findViewById(R.id.back_image_area).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// Toast.makeText(menuContext, "back",
						// Toast.LENGTH_SHORT).show();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						if (imm != null
								&& FTU_Bluetooth.this.getCurrentFocus() != null) {
							imm.hideSoftInputFromWindow(FTU_Bluetooth.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
						onBackPressed();
					}
				});
		findViewById(R.id.back_image_area).setOnTouchListener(
				new View.OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							v.setBackgroundResource(android.R.drawable.list_selector_background);
						} else if (event.getAction() == MotionEvent.ACTION_UP
								|| event.getAction() == MotionEvent.ACTION_CANCEL) {
							v.setBackgroundColor(Color.TRANSPARENT);
						}
						return false;
					}
				});
	}

	public void showReportPage(int pageID, ReadFtuxyc read) {
		switch (pageID) {
		case Constants.PAGE_1_1_1:
			showYaoxin_PAGE_1_1_1(read);
			break;
		case Constants.PAGE_1_1_2:
			showYaoxin_PAGE_1_1_2(read);
			break;
		case Constants.PAGE_1_1_3:
			showYaoxin_PAGE_1_1_3(read);
			break;
		case Constants.PAGE_1_1_4:
			showYaoxin_PAGE_1_1_4(read);
			break;
		// case Constants.PAGE_1_2_1:
		// case Constants.PAGE_1_2_2:
		case Constants.PAGE_1_2:
			showYaokong();
			break;
		case Constants.PAGE_1_7:
			showPWDsetting();
			break;
		case Constants.PAGE_1_4:
			showSOE();
			break;
		case Constants.PAGE_1_5:
			showFailure();
			break;
		}
	}

	void showPWDsetting() {
		final EditText old = (EditText) findViewById(R.id.old_pwd_input);
		final EditText new_pwd_input = (EditText) findViewById(R.id.new_pwd_input);
		final EditText pwd_input_again = (EditText) findViewById(R.id.pwd_input_again);
		Button pwd_confirm = (Button) findViewById(R.id.pwd_confirm);

		pwd_confirm.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.d("MESSAGE", "old.getText(): " + old.getText());
				// Log.d("MESSAGE", "new_pwd_input.getText(): " +
				// new_pwd_input.getText());
				// Log.d("MESSAGE", "pwd_input_again.getText(): " +
				// pwd_input_again.getText());
				if (!checkPWD(old.getText().toString())) {
					Toast.makeText(FTU_Bluetooth.this,
							getString(R.string.pwd_warn_old),
							Toast.LENGTH_SHORT).show();
				} else if (!new_pwd_input.getText().toString()
						.equals(pwd_input_again.getText().toString())) {
					Toast.makeText(FTU_Bluetooth.this,
							getString(R.string.pwd_warn_new),
							Toast.LENGTH_SHORT).show();
				} else {
					BluetoothPreferenceActivity.setPWD(FTU_Bluetooth.this,
							new_pwd_input.getText().toString());
					Toast.makeText(FTU_Bluetooth.this,
							getString(R.string.pwd_success), Toast.LENGTH_SHORT)
							.show();
					new_pwd_input.setText("");
					pwd_input_again.setText("");
					old.setText("");
				}
				// String currentValue =
				// BluetoothPreferenceActivity.getPWD(FTU_Bluetooth.this);
				// Log.d("MESSAGE", "change currentValue: " + currentValue);
			}
		});

	}

	void showYaoxin_PAGE_1_1_1(ReadFtuxyc readftu) {
		int[] ids = new int[] { R.id.kairu01, R.id.kairu02, R.id.kairu03,
				R.id.kairu04, R.id.kairu05, R.id.kairu06, R.id.kairu07,
				R.id.kairu08, R.id.kairu09, R.id.kairu10, R.id.kairu11,
				R.id.kairu12, R.id.kairu13, R.id.kairu14, R.id.kairu15,
				R.id.kairu16, R.id.kairu17, R.id.kairu18, R.id.kairu19,
				R.id.kairu20 };
		for (int i = 0; i < 20; i++) {
			TextView view = (TextView) findViewById(ids[i]);
			view.setText(getString(R.string.switch_in)
					+ (i + 1)
					+ ": "
					+ (readftu.yaoXin[i] ? getString(R.string.on)
							: getString(R.string.off)));
		}
		// for(int i = 0; i < 48; i++){
		// Log.d("MESSAGE", "开入"+ (i+1) + ": " + (readftu.yaoXin[i]? "合": "分"));
		// }
	}

	void showYaoxin_PAGE_1_1_2(ReadFtuxyc readftu) {
		int[] ids = new int[] { R.id.kairu26, R.id.kairu27, R.id.kairu28,
				R.id.kairu29, R.id.kairu30, R.id.kairu31, R.id.kairu32,
				R.id.kairu33, R.id.kairu34, R.id.kairu35, R.id.kairu36,
				R.id.kairu37 };
		for (int i = 0; i < 12; i++) {
			TextView view = (TextView) findViewById(ids[i]);
			view.setText(readftu.yaoXin[i + 26] ? getString(R.string.on)
					: getString(R.string.off));// index: 26~27
		}
	}

	void showYaoxin_PAGE_1_1_3(ReadFtuxyc readftu) {
		int[] ids = new int[] { R.id.yaoce01, R.id.yaoce02, R.id.yaoce03,
				R.id.yaoce04, R.id.yaoce05, R.id.yaoce06, R.id.yaoce07,
				R.id.yaoce08, R.id.yaoce09, R.id.yaoce10, R.id.yaoce11,
				R.id.yaoce12, R.id.yaoce13, R.id.yaoce14, R.id.yaoce15,
				R.id.yaoce16, R.id.yaoce17, R.id.yaoce18, R.id.yaoce19,
				R.id.yaoce20, R.id.yaoce21, R.id.yaoce22, R.id.yaoce23,
				R.id.yaoce24 };
		for (int i = 0; i < 24; i++) {
			TextView view = (TextView) findViewById(ids[i]);
			view.setText(readftu.yaoCe[i]);
		}
	}

	void showYaoxin_PAGE_1_1_4(ReadFtuxyc readftu) {
		int[] ids = new int[] { R.id.kairu20, R.id.kairu21, R.id.kairu22,
				R.id.kairu23, R.id.kairu24, R.id.kairu25 };
		for (int i = 0; i < 6; i++) {
			TextView view = (TextView) findViewById(ids[i]);
			view.setText(readftu.yaoXin[i + 20] ? getString(R.string.on)
					: getString(R.string.off)); // index: 20~25
		}
	}

	void showYaokong() {
		spinner = (Spinner) findViewById(R.id.spinner_id);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SPINNER_REMOTE_ID);
		adapter.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
															// message
		spinner.setAdapter(adapter);
		// 启动【2B】地址【2B】长度【2B】功能码【2B】遥控号【2B】校验【2B】
		// 启动【2B】地址【2B】长度【2B】功能码【2B】遥控号【2B】结果【2B】校验【2B】
		// 下行：7A 74 FF 00 04 00 06 01 01 00 0B 01 (预置01号)
		// 上行：7A 74 01 00 06 00 06 01 01 00 01 00 10 00 (预置01号成功)
		preset = (Button) findViewById(R.id.preset);
		preset.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				functionCode[0] = 0x06;
				functionCode[1] = 0x01;
				reqRemoteCtl[7] = 0x01; // function code
				reqRemoteCtl[8] = (byte) (spinner.getSelectedItemId());
				Util.setCheckCode(reqRemoteCtl, reqRemoteCtl.length);
				reqMessage();
			}

		});

		execute = (Button) findViewById(R.id.execute);
		execute.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				functionCode[0] = 0x06;
				functionCode[1] = 0x02;
				reqRemoteCtl[7] = 0x02; // function code
				reqRemoteCtl[8] = (byte) (spinner.getSelectedItemId());
				Util.setCheckCode(reqRemoteCtl, reqRemoteCtl.length);
				// reqMessage();
				// inputPWD();
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					inputPWD();

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				functionCode[0] = 0x06;
				functionCode[1] = 0x03;
				reqRemoteCtl[7] = 0x03; // function code
				reqRemoteCtl[8] = (byte) (spinner.getSelectedItemId());
				Util.setCheckCode(reqRemoteCtl, reqRemoteCtl.length);
				reqMessage();
			}
		});

		exec_direct = (Button) findViewById(R.id.exec_direct);
		exec_direct.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				functionCode[0] = 0x06;
				functionCode[1] = 0x04;
				reqRemoteCtl[7] = 0x04; // function code
				reqRemoteCtl[8] = (byte) (spinner.getSelectedItemId());
				Util.setCheckCode(reqRemoteCtl, reqRemoteCtl.length);
				// reqMessage();
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					inputPWD();
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	final static int[] table_check = new int[] { R.id.fixed_tab_check1,
			R.id.fixed_tab_check2, R.id.fixed_tab_check3,
			R.id.fixed_tab_check4, R.id.fixed_tab_check5,
			R.id.fixed_tab_check6, R.id.fixed_tab_check7,
			R.id.fixed_tab_check8, R.id.fixed_tab_check9 };
	final static int[] table_fixed = new int[] { R.id.fixed_tab_fixed1,
			R.id.fixed_tab_fixed2, R.id.fixed_tab_fixed3,
			R.id.fixed_tab_fixed4, R.id.fixed_tab_fixed5,
			R.id.fixed_tab_fixed6, R.id.fixed_tab_fixed7,
			R.id.fixed_tab_fixed8, R.id.fixed_tab_fixed9 };
	final static int[] table_time = new int[] { R.id.fixed_tab_time1,
			R.id.fixed_tab_time2, R.id.fixed_tab_time3, R.id.fixed_tab_time4,
			R.id.fixed_tab_time5, R.id.fixed_tab_time6, R.id.fixed_tab_time7,
			R.id.fixed_tab_time8, R.id.fixed_tab_time9 };
	final static int[] table_spinner = new int[] { R.id.fixed_tab_spin1,
			R.id.fixed_tab_spin2, R.id.fixed_tab_spin3, R.id.fixed_tab_spin4,
			R.id.fixed_tab_spin5, R.id.fixed_tab_spin6, R.id.fixed_tab_spin7,
			R.id.fixed_tab_spin8, R.id.fixed_tab_spin9 };
	final static BigDecimal num_limit = new BigDecimal("655.35");
	final static BigDecimal num_limit_int = new BigDecimal("65535");
	final static BigDecimal num_limit_byte = new BigDecimal("255");

	OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			switch (buttonView.getId()) {
			case R.id.power_loss: // action
				EditText switching_off_time = (EditText) findViewById(R.id.switching_off_time);
				switching_off_time.setEnabled(isChecked);
				break;
			case R.id.get_power: // action
				EditText get_power_time = (EditText) findViewById(R.id.switch_on);// 得电延时合闸时间
				get_power_time.setEnabled(isChecked);
				break;
			case R.id.single_vol_loss: // action
				EditText single_off_time = (EditText) findViewById(R.id.switch_on_single);
				single_off_time.setEnabled(isChecked);
				break;
			case R.id.post_acceleration:
				EditText post_acce_time = (EditText) findViewById(R.id.post_acce_time);
				post_acce_time.setEnabled(isChecked);
				break;
			case R.id.twice_inrush:
				EditText harmonic_percent = (EditText) findViewById(R.id.harmonic_percent);
				harmonic_percent.setEnabled(isChecked);
				break;
			case R.id.enlarge_fix_inrush:
				EditText evading_Inrush_multi = (EditText) findViewById(R.id.evading_Inrush_multi);// 倍数
				evading_Inrush_multi.setEnabled(isChecked);
				EditText evading_Inrush_time = (EditText) findViewById(R.id.evading_Inrush_time);// 时限
				evading_Inrush_time.setEnabled(isChecked);
				break;
			// case R.id.post_acceleration:
			// break;
			default: {
				for (int i = 0; i < msgParaSetting.tableValue.length; i++) {
					if (buttonView.getId() == table_check[i]) {
						// ((CheckBox)
						// findViewById(table_check[i])).setChecked(paraSetting.tableValue[i].bEnable);
						((EditText) findViewById(table_fixed[i]))
								.setEnabled(isChecked);
						((EditText) findViewById(table_time[i]))
								.setEnabled(isChecked);
						((Spinner) findViewById(table_spinner[i]))
								.setEnabled(isChecked);
					}
				}
			}
			}
		}
	};

	// OnItemSelectedListener spin_listener = new OnItemSelectedListener(){
	// @Override
	// public void onItemSelected(AdapterView<?> parent, View view,
	// int position, long id) {
	// // TODO Auto-generated method stub
	//
	// }
	// @Override
	// public void onNothingSelected(AdapterView<?> parent) {
	// // TODO Auto-generated method stub
	// }
	// };

	TextWatcher textwatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.d("MESSAGE", "afterTextChanged(Editable s: " + s);
			while (s.length() != 0
					&& num_limit.compareTo(new BigDecimal(s.toString())) < 0) {
				s.delete(s.length() - 1, s.length());
				Toast.makeText(FTU_Bluetooth.this,
						FTU_Bluetooth.this.getString(R.string.num_limit),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	TextWatcher textwatcher_int = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.d("MESSAGE", "afterTextChanged(Editable s: " + s);
			while (s.length() != 0
					&& num_limit_int.compareTo(new BigDecimal(s.toString())) < 0) {
				s.delete(s.length() - 1, s.length());
				Toast.makeText(FTU_Bluetooth.this,
						FTU_Bluetooth.this.getString(R.string.num_limit_int),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	TextWatcher watcher_byte = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.d("MESSAGE", "afterTextChanged(Editable s: " + s);
			while (s.length() != 0
					&& num_limit_byte.compareTo(new BigDecimal(s.toString())) < 0) {
				s.delete(s.length() - 1, s.length());
				Toast.makeText(FTU_Bluetooth.this,
						FTU_Bluetooth.this.getString(R.string.num_limit_byte),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	OnClickListener btn_refresh = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mChatService != null
					&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
				if (msgStatus != MSG_WAIT_RESPONSE) {
					requestMessage();
				}
			} else { // Show warning toast: Device is not connected!
				Toast.makeText(getApplicationContext(),
						getString(R.string.not_connected), Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	void showParaSetting(ParaSetting paraSetting) {
		final String[] SPINNER_RE_SWITCH_TIME = { getString(R.string.quit),
				getString(R.string.once), getString(R.string.twice),
				getString(R.string.three) };
		final String[] SPINNER_INVERSE_TIME = {
				getString(R.string.mornal_inverse),
				getString(R.string.abnormal_inverse),
				getString(R.string.extreme_inverse),
				getString(R.string.thermal_overload_inverse) };
		final String[] SPINNER_FIXED_TABLE = { getString(R.string.warning),
				getString(R.string.exit) };

		final EditText fault_holding = (EditText) findViewById(R.id.fault_holding); // 故障遥信保持时间
		fault_holding.setText(paraSetting.fault_holding.toString());
		fault_holding.addTextChangedListener(textwatcher);

		final CheckBox closed_loop = (CheckBox) findViewById(R.id.closed_loop);
		closed_loop.setChecked(paraSetting.funCtrl[0]);

		final CheckBox power_loss = (CheckBox) findViewById(R.id.power_loss); // 失电延时分闸
		power_loss.setChecked(paraSetting.funCtrl[1]);
		power_loss.setOnCheckedChangeListener(listener);

		final EditText switching_off_time = (EditText) findViewById(R.id.switching_off_time);
		switching_off_time.setEnabled(power_loss.isChecked());
		switching_off_time.addTextChangedListener(textwatcher);
		switching_off_time.setText(paraSetting.switching_off_time.toString());// 失电延时分闸时间

		final CheckBox get_power = (CheckBox) findViewById(R.id.get_power);
		get_power.setChecked(paraSetting.funCtrl[2]);
		get_power.setOnCheckedChangeListener(listener);
		final EditText get_power_time = (EditText) findViewById(R.id.switch_on);// 得电延时合闸时间
		get_power_time.setEnabled(get_power.isChecked());
		get_power_time.addTextChangedListener(textwatcher);
		get_power_time.setText(paraSetting.get_power_time.toString());

		final CheckBox single_vol_loss = (CheckBox) findViewById(R.id.single_vol_loss);// 单侧失压合闸
		single_vol_loss.setChecked(paraSetting.funCtrl[3]);
		single_vol_loss.setOnCheckedChangeListener(listener);
		final EditText single_off_time = (EditText) findViewById(R.id.switch_on_single);
		single_off_time.addTextChangedListener(textwatcher);
		single_off_time.setEnabled(single_vol_loss.isChecked());
		single_off_time.setText(paraSetting.single_off_time.toString());

		spinner_switch = (Spinner) findViewById(R.id.switch_time); // 重合闸次数
		adapter_switch = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SPINNER_RE_SWITCH_TIME);
		adapter_switch.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
		spinner_switch.setAdapter(adapter_switch);
		spinner_switch.setSelection(paraSetting.switch_time.intValue());
		spinner_switch.invalidate();
		// spinner_switch.setOnItemSelectedListener(spin_listener);

		// switch_time.setText(paraSetting.switch_time.toString());

		final EditText once_time = (EditText) findViewById(R.id.once_time); // 一次重合闸时间
		once_time.setText(paraSetting.once_time.toString());
		once_time.addTextChangedListener(textwatcher);

		final EditText twice_time = (EditText) findViewById(R.id.twice_time);
		twice_time.setText(paraSetting.twice_time.toString());
		twice_time.addTextChangedListener(textwatcher);

		final EditText three_time = (EditText) findViewById(R.id.three_time);
		three_time.setText(paraSetting.three_time.toString());
		three_time.addTextChangedListener(textwatcher);

		final CheckBox post_acceleration = (CheckBox) findViewById(R.id.post_acceleration);// 后加速功能
		post_acceleration.setChecked(paraSetting.funCtrl[6]);
		post_acceleration.setOnCheckedChangeListener(listener);
		final EditText post_acce_time = (EditText) findViewById(R.id.post_acce_time);
		post_acce_time.addTextChangedListener(textwatcher);
		post_acce_time.setEnabled(post_acceleration.isChecked());
		post_acce_time.setText(paraSetting.post_acce_time.toString());

		final CheckBox twice_inrush = (CheckBox) findViewById(R.id.twice_inrush);// 2次涌波、百分比
		twice_inrush.setChecked(paraSetting.funCtrl[5]);
		twice_inrush.setOnCheckedChangeListener(listener);
		final EditText harmonic_percent = (EditText) findViewById(R.id.harmonic_percent);
		harmonic_percent.setEnabled(twice_inrush.isChecked());
		harmonic_percent.addTextChangedListener(textwatcher_int);
		harmonic_percent.setText(paraSetting.harmonic_percent.toString());

		final CheckBox enlarge_fix_inrush = (CheckBox) findViewById(R.id.enlarge_fix_inrush);// 放大定值涌波闭锁
		enlarge_fix_inrush.setChecked(paraSetting.funCtrl[4]);
		enlarge_fix_inrush.setOnCheckedChangeListener(listener);
		final EditText evading_Inrush_multi = (EditText) findViewById(R.id.evading_Inrush_multi);// 倍数
		evading_Inrush_multi.setEnabled(enlarge_fix_inrush.isChecked());
		evading_Inrush_multi.addTextChangedListener(textwatcher);
		evading_Inrush_multi.setText(paraSetting.evading_Inrush_multi
				.toString());
		final EditText evading_Inrush_time = (EditText) findViewById(R.id.evading_Inrush_time);// 时限
		evading_Inrush_time.setEnabled(enlarge_fix_inrush.isChecked());
		evading_Inrush_time.addTextChangedListener(textwatcher);
		evading_Inrush_time.setText(paraSetting.evading_Inrush_time.toString());

		// 反时限类型
		spinner_inverse_time = (Spinner) findViewById(R.id.inverse_time_lag); // 反时限类型
		adapter_inverse_time = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SPINNER_INVERSE_TIME);
		adapter_inverse_time.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
		spinner_inverse_time.setAdapter(adapter_inverse_time);
		spinner_inverse_time.setSelection((int) paraSetting.inverse_time_lag);
		spinner_inverse_time.invalidate();
		// spinner_inverse_time.setOnItemSelectedListener(spin_listener);

		// android.R.layout.browser_link_context_header
		adapter_fixed_table = new ArrayAdapter<String>(this, R.layout.spinner,
				SPINNER_FIXED_TABLE);
		adapter_fixed_table.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
		// spinner_switch.setAdapter(adapter_switch);
		// spinner_switch.setSelection(paraSetting.switch_time.intValue());
		// spinner_switch.setOnItemSelectedListener(spin_listener);
		for (int i = 0; i < paraSetting.tableValue.length; i++) {
			((CheckBox) findViewById(table_check[i]))
					.setChecked(paraSetting.tableValue[i].bEnable);
			((EditText) findViewById(table_fixed[i]))
					.setText(paraSetting.tableValue[i].set.toString());
			((EditText) findViewById(table_time[i]))
					.setText(paraSetting.tableValue[i].time.toString());
			((CheckBox) findViewById(table_check[i]))
					.setOnCheckedChangeListener(listener);
			((EditText) findViewById(table_fixed[i]))
					.setEnabled(paraSetting.tableValue[i].bEnable);
			((EditText) findViewById(table_time[i]))
					.setEnabled(paraSetting.tableValue[i].bEnable);
			((EditText) findViewById(table_fixed[i]))
					.addTextChangedListener(textwatcher);
			((EditText) findViewById(table_time[i]))
					.addTextChangedListener(textwatcher);

			((Spinner) findViewById(table_spinner[i]))
					.setEnabled(paraSetting.tableValue[i].bEnable);
			((Spinner) findViewById(table_spinner[i]))
					.setAdapter(adapter_fixed_table);
			((Spinner) findViewById(table_spinner[i]))
					.setSelection((int) paraSetting.tableValue[i].bTrip);
			// ((Spinner)
			// findViewById(table_spinner[i])).setOnItemSelectedListener(spin_listener);
		}

		fixed_refresh = (Button) findViewById(R.id.fixed_refresh);
		fixed_refresh.setOnClickListener(btn_refresh);

		fixed_save = (Button) findViewById(R.id.fixed_save);
		fixed_save.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					int dwFunCtrl = 0, i = 0, head = 12;
					byte[] funCtrl = new byte[4];
					for (i = 0; i < 4; i++) {
						msgParaSetting.bytes[i] = FTUMessage.REQ_READ_DZ_4[i];
					}
					msgParaSetting.bytes[4] = 0x32;
					msgParaSetting.bytes[5] = 0x01;
					msgParaSetting.bytes[6] = 0x04;
					// msgParaSetting.bytes[7] = 0x08;
					for (i = 7; i < 12; i++) {
						msgParaSetting.bytes[i] = FTUMessage.REQ_READ_DZ_4[i];
					}
					if (closed_loop.isChecked())
						dwFunCtrl = dwFunCtrl + 1;
					if (power_loss.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 1);
					if (get_power.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 2);
					if (single_vol_loss.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 3);
					if (twice_inrush.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 5);// 放大定值
					if (enlarge_fix_inrush.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 4);// 2次谐波
					if (post_acceleration.isChecked())
						dwFunCtrl = dwFunCtrl + (1 << 6);
					funCtrl = Util.int2byteArrayLittle(dwFunCtrl);
					for (i = 0; i < 4; i++) {
						msgParaSetting.bytes[i + head] = funCtrl[i];// first 4
																	// bytes is
																	// funCtrl.
					}
					msgParaSetting.bytes[4 + head] = 0x00;
					msgParaSetting.bytes[5 + head] = 0x00;
					Util.shortToByteArray(
							(short) Util.getText2Num(
									fault_holding.getText().toString())
									.intValue(), msgParaSetting.bytes, 6 + head);// 故障保持时间
					Util.shortToByteArray(
							(short) Util.getText2Num(
									harmonic_percent.getText().toString(), 0)
									.intValue(), msgParaSetting.bytes, 8 + head);// 涌流闭锁百分比
					Util.shortToByteArray(
							(short) Util.getText2Num(
									evading_Inrush_multi.getText().toString())
									.intValue(), msgParaSetting.bytes,
							10 + head);// 涌流闭锁2的定值倍数
					Util.shortToByteArray(
							(short) Util.getText2Num(
									evading_Inrush_time.getText().toString())
									.intValue(), msgParaSetting.bytes,
							12 + head);// 躲涌流时间
					Util.shortToByteArray(
							(short) Util.getText2Num(
									switching_off_time.getText().toString())
									.intValue(), msgParaSetting.bytes,
							14 + head);// 失电延时分闸
					Util.shortToByteArray(
							(short) Util.getText2Num(
									get_power_time.getText().toString())
									.intValue(), msgParaSetting.bytes,
							16 + head);// 得电延时合闸
					Util.shortToByteArray(
							(short) Util.getText2Num(
									single_off_time.getText().toString())
									.intValue(), msgParaSetting.bytes,
							18 + head);// 单侧失压合闸
					for (i = 20 + head; i < 72 + head; i++) { // WORD
																// wRsv[26];52字节
																// 对应dwFunCtrl相关功能参数
																// 没用到
						msgParaSetting.bytes[i] = 0x00;
					}
					Util.shortToByteArray(
							(short) spinner_switch.getSelectedItemId(),
							msgParaSetting.bytes, 72 + head);// 重合闸次数
					Util.shortToByteArray(
							(short) Util.getText2Num(
									once_time.getText().toString()).intValue(),
							msgParaSetting.bytes, 74 + head);// 一次重合闸时间
					Util.shortToByteArray(
							(short) Util.getText2Num(
									twice_time.getText().toString()).intValue(),
							msgParaSetting.bytes, 76 + head);// 二次重合闸时间
					Util.shortToByteArray(
							(short) Util.getText2Num(
									three_time.getText().toString()).intValue(),
							msgParaSetting.bytes, 78 + head);// 三次重合闸时间
					Util.shortToByteArray(
							(short) Util.getText2Num(
									post_acce_time.getText().toString())
									.intValue(), msgParaSetting.bytes,
							80 + head);// 后加速时间
					for (i = 82 + head; i < 88 + head; i++) { // 备用
						msgParaSetting.bytes[i] = 0x00;
					}
					int k = 0;
					for (k = 0, i = 88 + head; k < 9; k++) { // 每16字节为表格里的一行 88
																// + k*16 = 232;
																// 232 + 12 =
																// 244, i < 88 +
																// head + k*16
						msgParaSetting.bytes[i++] = (byte) (((CheckBox) findViewById(table_check[k]))
								.isChecked() ? 0x01 : 0x00);
						msgParaSetting.bytes[i++] = (byte) ((Spinner) findViewById(table_spinner[k]))
								.getSelectedItemId();
						msgParaSetting.bytes[i++] = 0x00; // //元件类型 <---不要去改
						if (k == 8) {
							msgParaSetting.bytes[i++] = (byte) spinner_inverse_time
									.getSelectedItemId();
						} else {
							msgParaSetting.bytes[i++] = 0x00; // 反时限类型 <---不要去改
						}
						Util.shortToByteArray(
								(short) Util
										.getText2Num(
												((EditText) findViewById(table_fixed[k]))
														.getText().toString())
										.intValue(), msgParaSetting.bytes, i);// 表格定值
						i = i + 2;
						Util.shortToByteArray(
								(short) Util
										.getText2Num(
												((EditText) findViewById(table_time[k]))
														.getText().toString())
										.intValue(), msgParaSetting.bytes, i);// 表格时限(s)
						i = i + 2;
						for (int g = 0; g < 8; g++) {
							msgParaSetting.bytes[i++] = 0x00; // <---保留8字节，不要去改
						}
					}
					for (i = 244; i < 312; i++) {
						msgParaSetting.bytes[i] = 0x00; // 没用到
					}
					Util.setCheckCode(msgParaSetting.bytes, 314); // adding
																	// check
																	// code for
																	// byte
																	// array.
					// boolean check = true;
					// for(i = 0; i < 314; i ++){
					// check = FTUMessage.REQ_WRITE_DZ_1[i] ==
					// msgParaSetting.bytes[i];
					// if(check == false){
					// Log.d("MESSAGE", "check is not true! when i=" + i);
					// }
					// }
					paraStatus = PARA_WAIT_FIRST;

					inputPWD();// send bytes when OK.
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void showSOE() {

		LinearLayout soe_header = (LinearLayout) findViewById(R.id.soe_header);
		soe_header.setVisibility(View.VISIBLE);
		soeListView = (ListView) findViewById(R.id.soe_list);
		soeListView.setVisibility(View.VISIBLE);

		// Log.d("MESSAGE", "soeListView: " + soeListView);
		mSOEList = msgSOE.soe_list;
		// if(mSOEList == null)return; //to be deleted.
		mSOEAdapter = new SOEListAdapter(FTU_Bluetooth.this,
				R.layout.soe_list_item, mSOEList);
		soeListView.setAdapter(mSOEAdapter);
		// soeListView.addHeaderView(time);
		// Util.setListViewHeightBasedOnChildren(soeListView);
	}

	public void showFailure() {

		LinearLayout failure_header = (LinearLayout) findViewById(R.id.failure_header);
		failure_header.setVisibility(View.VISIBLE);
		failureListView = (ListView) findViewById(R.id.failure_list);
		failureListView.setVisibility(View.VISIBLE);

		// Log.d("MESSAGE", "soeListView: " + soeListView);
		mFailureList = msgFailure.failure_list;
		// if(mSOEList == null)return; //to be deleted.
		mFailureAdapter = new FailureListAdapter(FTU_Bluetooth.this,
				R.layout.failure_list_item, mFailureList);
		failureListView.setAdapter(mFailureAdapter);
		// soeListView.addHeaderView(time);
		// Util.setListViewHeightBasedOnChildren(soeListView);

	}

	// ----------------- 通讯参数设置 Communication Parameter Setting ----------------
	void showNetworkCard(CommSetting comm) {
		final int[] nw1_ip = { R.id.nw1_ip0, R.id.nw1_ip1, R.id.nw1_ip2,
				R.id.nw1_ip3 };
		final int[] nw1_mask = { R.id.nw1_mask0, R.id.nw1_mask1,
				R.id.nw1_mask2, R.id.nw1_mask3 };
		final int[] nw1_gate = { R.id.nw1_gate0, R.id.nw1_gate1,
				R.id.nw1_gate2, R.id.nw1_gate3 };
		final int[] nw2_ip = { R.id.nw2_ip0, R.id.nw2_ip1, R.id.nw2_ip2,
				R.id.nw2_ip3 };
		final int[] nw2_mask = { R.id.nw2_mask0, R.id.nw2_mask1,
				R.id.nw2_mask2, R.id.nw2_mask3 };
		final int[] nw2_gate = { R.id.nw2_gate0, R.id.nw2_gate1,
				R.id.nw2_gate2, R.id.nw2_gate3 };

		int i, j;
		// EditText ip[];
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw1_ip[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw1_ip[i])).setText("" + comm.nw1_ip[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw1_mask[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw1_mask[i])).setText(""
					+ comm.nw1_mask[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw1_gate[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw1_gate[i])).setText(""
					+ comm.nw1_gate[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw2_ip[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw2_ip[i])).setText("" + comm.nw2_ip[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw2_mask[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw2_mask[i])).setText(""
					+ comm.nw2_mask[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(nw2_gate[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(nw2_gate[i])).setText(""
					+ comm.nw2_gate[i]);
		}

		comm_refresh = (Button) findViewById(R.id.comm_refresh);
		comm_refresh.setOnClickListener(btn_refresh);

		comm_save = (Button) findViewById(R.id.comm_save);
		comm_save.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					int head = 48;
					int m, n;
					msgComm.copyForSave();
					msgComm.bytesForSave[6] = 0x04;
					for (m = head + 3, n = 0; m >= head; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw1_ip[n])).getText()
										.toString())).byteValue();
					}
					for (m = head + 7, n = 0; m >= head + 4; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw1_mask[n]))
										.getText().toString())).byteValue();
					}
					for (m = head + 11, n = 0; m >= head + 8; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw1_gate[n]))
										.getText().toString())).byteValue();
					}
					for (m = head + 20 + 3, n = 0; m >= head + 20; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw2_ip[n])).getText()
										.toString())).byteValue();
					}
					for (m = head + 20 + 7, n = 0; m >= head + 4 + 20; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw2_mask[n]))
										.getText().toString())).byteValue();
					}
					for (m = head + 20 + 11, n = 0; m >= head + 8 + 20; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(nw2_gate[n]))
										.getText().toString())).byteValue();
					}
					// boolean check = true;
					// int k;
					// for(k = 0; k < 288; k ++){
					// check = msgComm.bytes[k] == msgComm.bytesForSave[k];
					// if(check == false){
					// Log.d("MESSAGE", "check is not true! when k=" + k);
					// }
					// // else{
					// // Log.d("MESSAGE", "check is true! when k=" + k);
					// // }
					//
					// }
					Util.setCheckCode(msgComm.bytesForSave, 290); // adding
																	// check
																	// code for
																	// byte
																	// array.
					commStatus = COMM_NETWORK_CARD;

					inputPWD();
				} else { // Show warning toast: Device is not connected!
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	final static int[] baud = { R.id.ser1_baud, R.id.ser2_baud, R.id.ser3_baud,
			R.id.ser4_baud, R.id.ser5_baud };
	final static int[] checkbit = { R.id.ser1_check, R.id.ser2_check,
			R.id.ser3_check, R.id.ser4_check, R.id.ser5_check };
	final static int[] protocol = { R.id.ser1_protocal, R.id.ser2_protocal,
			R.id.ser3_protocal, R.id.ser4_protocal, R.id.ser5_protocal };
	final static int[] protaddr = { R.id.ser1_protaddr, R.id.ser2_protaddr,
			R.id.ser3_protaddr, R.id.ser4_protaddr, R.id.ser5_protaddr };

	// --------------Serial Port 串口参数 ---------------------------
	void showSerialPort_enter(CommSetting comm) {
		final String[] SPINNER_BAUD = { getString(R.string.baud_1),
				getString(R.string.baud_2), getString(R.string.baud_3),
				getString(R.string.baud_4), getString(R.string.baud_5),
				getString(R.string.baud_6), getString(R.string.baud_7),
				getString(R.string.baud_8), getString(R.string.baud_9),
				getString(R.string.baud_10), getString(R.string.baud_11),
				getString(R.string.baud_12), getString(R.string.baud_13),
				getString(R.string.baud_14) };
		final String[] SPINNER_CHECKBIT = { getString(R.string.checkbit_1),
				getString(R.string.checkbit_2), getString(R.string.checkbit_3) };
		final String[] SPINNER_PROTOCOL = { getString(R.string.protocol_1),
				getString(R.string.protocol_2), getString(R.string.protocol_3),
				getString(R.string.protocol_4), getString(R.string.protocol_5),
				getString(R.string.protocol_6) };

		int i;
		if (adapter_baud == null) {
			adapter_baud = new ArrayAdapter<String>(this, R.layout.spinner,
					SPINNER_BAUD);
			adapter_baud.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
			// for(i = 0; i < 5; i++){
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(baud[i])).setEnabled(false);
			// ((Spinner) findViewById(baud[i])).setAdapter(adapter_baud);
			// // ((Spinner)
			// findViewById(baud[i])).setSelection(comm.serial_baud[i]);
			// }
		}
		if (adapter_checkbit == null) {
			adapter_checkbit = new ArrayAdapter<String>(this, R.layout.spinner,
					SPINNER_CHECKBIT);
			adapter_checkbit.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
			// for(i = 0; i < 5; i++){
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(checkbit[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(checkbit[i])).setAdapter(adapter_checkbit);
			// // ((Spinner)
			// findViewById(checkbit[i])).setSelection(comm.serial_checkbit[i]);
			// }

		}
		if (adapter_protocol == null) {
			adapter_protocol = new ArrayAdapter<String>(this, R.layout.spinner,
					SPINNER_PROTOCOL);
			adapter_protocol.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
			// for(i = 0; i < 5; i++){
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(protocol[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(protocol[i])).setAdapter(adapter_protocol);
			// // ((Spinner)
			// findViewById(protocol[i])).setSelection(comm.serial_protocol[i]);
			// }
		}

		for (i = 0; i < 5; i++) {
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(baud[i])).setEnabled(false);
			((Spinner) findViewById(baud[i])).setAdapter(adapter_baud);
			// ((Spinner)
			// findViewById(baud[i])).setSelection(comm.serial_baud[i]);
		}
		for (i = 0; i < 5; i++) {
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(checkbit[i])).setEnabled(false);
			((Spinner) findViewById(checkbit[i])).setAdapter(adapter_checkbit);
			// ((Spinner)
			// findViewById(checkbit[i])).setSelection(comm.serial_checkbit[i]);
		}
		for (i = 0; i < 5; i++) {
			// if(0 == i || 4 == i || 3 == i)((Spinner)
			// findViewById(protocol[i])).setEnabled(false);
			((Spinner) findViewById(protocol[i])).setAdapter(adapter_protocol);
			// ((Spinner)
			// findViewById(protocol[i])).setSelection(comm.serial_protocol[i]);
		}
	}

	void showSerialPort_refresh(CommSetting comm) {
		int i;
		final int[] baud = { R.id.ser1_baud, R.id.ser2_baud, R.id.ser3_baud,
				R.id.ser4_baud, R.id.ser5_baud };
		final int[] checkbit = { R.id.ser1_check, R.id.ser2_check,
				R.id.ser3_check, R.id.ser4_check, R.id.ser5_check };
		final int[] protocol = { R.id.ser1_protocal, R.id.ser2_protocal,
				R.id.ser3_protocal, R.id.ser4_protocal, R.id.ser5_protocal };
		final int[] protaddr = { R.id.ser1_protaddr, R.id.ser2_protaddr,
				R.id.ser3_protaddr, R.id.ser4_protaddr, R.id.ser5_protaddr };

		for (i = 0; i < 5; i++) {
			if (0 == i)
				((Spinner) findViewById(baud[i])).setEnabled(false);
			// ((Spinner) findViewById(baud[i])).setAdapter(adapter_baud);
			((Spinner) findViewById(baud[i])).setSelection(comm.serial_baud[i]);
		}
		for (i = 0; i < 5; i++) {
			if (0 == i || 4 == i || 3 == i)
				((Spinner) findViewById(checkbit[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(checkbit[i])).setAdapter(adapter_checkbit);
			((Spinner) findViewById(checkbit[i]))
					.setSelection(comm.serial_checkbit[i]);
		}
		for (i = 0; i < 5; i++) {
			if (0 == i || 4 == i || 3 == i)
				((Spinner) findViewById(protocol[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(protocol[i])).setAdapter(adapter_protocol);
			((Spinner) findViewById(protocol[i]))
					.setSelection(comm.serial_protocol[i]);
		}

		for (i = 0; i < 5; i++) {
			((EditText) findViewById(protaddr[i]))
					.addTextChangedListener(textwatcher_int);
			((EditText) findViewById(protaddr[i])).setText(""
					+ comm.serial_addr[i]);
			if (0 == i || 4 == i || 3 == i)
				((EditText) findViewById(protaddr[i])).setEnabled(false);
		}
		comm_refresh = (Button) findViewById(R.id.comm_refresh);
		comm_refresh.setOnClickListener(btn_refresh);

		comm_save = (Button) findViewById(R.id.comm_save);
		comm_save.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					int m, n;
					msgComm.copyForSave();
					msgComm.bytesForSave[6] = 0x04;
					for (m = 88 + 6, n = 0; n < 5; m += 20, n++) {
						Util.shortToByteArray(
								(short) ((Spinner) findViewById(baud[n]))
										.getSelectedItemId(),
								msgComm.bytesForSave, m);
					}
					for (m = 88 + 10, n = 0; n < 5; m += 20, n++) {
						Util.shortToByteArray(
								(short) ((Spinner) findViewById(checkbit[n]))
										.getSelectedItemId(),
								msgComm.bytesForSave, m);
					}
					for (m = 88 + 4, n = 0; n < 5; m += 20, n++) {
						Util.shortToByteArray(
								(short) ((Spinner) findViewById(protocol[n]))
										.getSelectedItemId(),
								msgComm.bytesForSave, m);
					}
					for (m = 88 + 16, n = 0; n < 5; m += 20, n++) { // bug fix,
																	// set
																	// origin 18
																	// to
																	// correct
																	// 16.
						Util.shortToByteArray(
								Util.getText2Num(
										((EditText) findViewById(protaddr[n]))
												.getText().toString(), 0)
										.shortValue(), msgComm.bytesForSave, m);
					}

					boolean check = true;
					int k;
					for (k = 0; k < 288; k++) {
						check = msgComm.bytes[k] == msgComm.bytesForSave[k];
						if (check == false) {
							Log.d("MESSAGE", "check Not true! k=" + k
									+ ", msgComm.bytesForSave[k]= "
									+ msgComm.bytesForSave[k]);
						}
						// else{
						// Log.d("MESSAGE", "check is true! when k=" + k);
						// }

					}
					Util.setCheckCode(msgComm.bytesForSave, 290); // adding
																	// check
																	// code for
																	// byte
																	// array.
					commStatus = COMM_SERIAL_PORT;

					inputPWD();
				} else { // Show warning toast: Device is not connected!
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// -------------------- Network Connection 网络连接 ----------------------------
	final static int[] connect_mode1 = { R.id.connt1_protl, R.id.connt2_protl,
			R.id.connt3_protl };
	final static int[] connect_mode2 = { R.id.connt1_as, R.id.connt2_as,
			R.id.connt3_as };
	final static int[] connect_protocol = { R.id.connt1_protocal,
			R.id.connt2_protocal, R.id.connt3_protocal };

	void showConnection_enter(CommSetting comm) {
		final String[] SPINNER_MODE1 = { getString(R.string.mode1_1),
				getString(R.string.mode1_2) };
		final String[] SPINNER_MODE2 = { getString(R.string.mode2_1),
				getString(R.string.mode2_2) };
		final String[] SPINNER_PROTOCOL = { getString(R.string.protocol_1),
				getString(R.string.protocol_2), getString(R.string.protocol_3),
				getString(R.string.protocol_4), getString(R.string.protocol_5),
				getString(R.string.protocol_6) };

		if (adapter_mode1 == null) {
			adapter_mode1 = new ArrayAdapter<String>(this, R.layout.spinner,
					SPINNER_MODE1);
			adapter_mode1.setDropDownViewResource(R.layout.message);
		}
		if (adapter_mode2 == null) {
			adapter_mode2 = new ArrayAdapter<String>(this, R.layout.spinner,
					SPINNER_MODE2);
			adapter_mode2.setDropDownViewResource(R.layout.message);
		}
		if (adapter_conn_prot == null) {
			adapter_conn_prot = new ArrayAdapter<String>(this,
					R.layout.spinner, SPINNER_PROTOCOL);
			adapter_conn_prot.setDropDownViewResource(R.layout.message);// android.R.layout.select_dialog_item);
		}
		int i;
		for (i = 0; i < 3; i++) {
			if (0 == i)
				((Spinner) findViewById(connect_mode1[i])).setEnabled(false);
			((Spinner) findViewById(connect_mode1[i]))
					.setAdapter(adapter_mode1);
			// ((Spinner)
			// findViewById(connect_mode1[i])).setSelection(comm.connect_mode1[i]);
		}
		for (i = 0; i < 3; i++) {
			if (0 == i)
				((Spinner) findViewById(connect_mode2[i])).setEnabled(false);
			((Spinner) findViewById(connect_mode2[i]))
					.setAdapter(adapter_mode2);
			// ((Spinner)
			// findViewById(connect_mode2[i])).setSelection(comm.connect_mode2[i]);
		}
		for (i = 0; i < 3; i++) {
			if (0 == i)
				((Spinner) findViewById(connect_protocol[i])).setEnabled(false);
			((Spinner) findViewById(connect_protocol[i]))
					.setAdapter(adapter_conn_prot);
			// ((Spinner)
			// findViewById(connect_protocol[i])).setSelection(comm.connect_protocol[i]);
		}
	}

	void showConnection_refresh(CommSetting comm) {
		final int[] connect1_ip = { R.id.connt1_ip0, R.id.connt1_ip1,
				R.id.connt1_ip2, R.id.connt1_ip3 };
		final int[] connect2_ip = { R.id.connt2_ip0, R.id.connt2_ip1,
				R.id.connt2_ip2, R.id.connt2_ip3 };
		final int[] connect3_ip = { R.id.connt3_ip0, R.id.connt3_ip1,
				R.id.connt3_ip2, R.id.connt3_ip3 };
		final int[] connect_port = { R.id.connt1_port, R.id.connt2_port,
				R.id.connt3_port };
		final int[] connect_addr = { R.id.connt1_addr, R.id.connt2_addr,
				R.id.connt3_addr };

		int i;
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(connect1_ip[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(connect1_ip[i])).setText(""
					+ comm.connect1_ip[i]);
			((EditText) findViewById(connect1_ip[i])).setEnabled(false);
		}
		for (i = 0; i < 4; i++) {
			// ((EditText) findViewById(connect2_ip[i])).setEnabled(false);
			((EditText) findViewById(connect2_ip[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(connect2_ip[i])).setText(""
					+ comm.connect2_ip[i]);
		}
		for (i = 0; i < 4; i++) {
			((EditText) findViewById(connect3_ip[i]))
					.addTextChangedListener(watcher_byte);
			((EditText) findViewById(connect3_ip[i])).setText(""
					+ comm.connect3_ip[i]);
		}
		for (i = 0; i < 3; i++) {
			((EditText) findViewById(connect_port[i]))
					.addTextChangedListener(textwatcher_int);
			((EditText) findViewById(connect_port[i])).setText(""
					+ comm.connect_port[i]);
			if (0 == i)
				((EditText) findViewById(connect_port[i])).setEnabled(false);
		}

		for (i = 0; i < 3; i++) {
			// if(0 == i)((Spinner)
			// findViewById(connect_mode1[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(connect_mode1[i])).setAdapter(adapter_mode1);
			((Spinner) findViewById(connect_mode1[i]))
					.setSelection(comm.connect_mode1[i]);
		}
		for (i = 0; i < 3; i++) {
			// if(0 == i)((Spinner)
			// findViewById(connect_mode2[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(connect_mode2[i])).setAdapter(adapter_mode2);
			((Spinner) findViewById(connect_mode2[i]))
					.setSelection(comm.connect_mode2[i]);
		}
		for (i = 0; i < 3; i++) {
			// if(0 == i)((Spinner)
			// findViewById(connect_protocol[i])).setEnabled(false);
			// ((Spinner)
			// findViewById(connect_protocol[i])).setAdapter(adapter_conn_prot);
			((Spinner) findViewById(connect_protocol[i]))
					.setSelection(comm.connect_protocol[i]);
		}
		for (i = 0; i < 3; i++) {
			((EditText) findViewById(connect_addr[i]))
					.addTextChangedListener(textwatcher_int);
			((EditText) findViewById(connect_addr[i])).setText(""
					+ comm.connect_addr[i]);
			if (0 == i)
				((EditText) findViewById(connect_addr[i])).setEnabled(false);
		}
		comm_refresh = (Button) findViewById(R.id.comm_refresh);
		comm_refresh.setOnClickListener(btn_refresh);

		comm_save = (Button) findViewById(R.id.comm_save);
		comm_save.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChatService != null
						&& mChatService.getState() == BluetoothService.STATE_CONNECTED) {
					msgComm.copyForSave();
					msgComm.bytesForSave[6] = 0x04;
					int m, n;
					for (m = 208 + 7, n = 0; m >= 208 + 4; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(connect1_ip[n]))
										.getText().toString())).byteValue();
					}
					for (m = 228 + 7, n = 0; m >= 228 + 4; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(connect2_ip[n]))
										.getText().toString())).byteValue();
					}
					for (m = 248 + 7, n = 0; m >= 248 + 4; m--, n++) {
						msgComm.bytesForSave[m] = (new BigDecimal(
								((EditText) findViewById(connect3_ip[n]))
										.getText().toString())).byteValue();
					}
					// ----------- port 端口号 --------------
					for (m = 208 + 10, n = 0; n < 3; m += 20, n++) {
						Util.shortToByteArray(
								Util.getText2Num(
										((EditText) findViewById(connect_port[n]))
												.getText().toString(), 0)
										.shortValue(), msgComm.bytesForSave, m);
					}
					// ----------- Mode: mode1 网络协议 && mode2 本机作为 --------------
					short x, y, mode = 0;
					for (m = 208 + 12, n = 0; n < 3; m += 20, n++) {
						x = (short) ((Spinner) findViewById(connect_mode1[n]))
								.getSelectedItemId();
						y = (short) ((Spinner) findViewById(connect_mode2[n]))
								.getSelectedItemId();
						if (x == 0 && y == 0) {
							mode = 1;
						} else if (x == 0 && y == 1) {
							mode = 2;
						} else if (x == 1 && y == 0) {
							mode = 3;
						} else if (x == 1 && y == 1) {
							mode = 4;
						}
						Util.shortToByteArray(mode, msgComm.bytesForSave, m);
					}

					// ----------- protocol 规约 --------------
					for (m = 208 + 8, n = 0; n < 3; m += 20, n++) {
						Util.shortToByteArray(
								(short) ((Spinner) findViewById(connect_protocol[n]))
										.getSelectedItemId(),
								msgComm.bytesForSave, m);
					}
					// ---------------规约地址
					for (m = 208 + 14, n = 0; n < 3; m += 20, n++) {
						Util.shortToByteArray(
								Util.getText2Num(
										((EditText) findViewById(connect_addr[n]))
												.getText().toString(), 0)
										.shortValue(), msgComm.bytesForSave, m);
					}
					boolean check = true;
					int k;
					for (k = 0; k < 288; k++) {
						check = msgComm.bytes[k] == msgComm.bytesForSave[k];
						if (check == false) {
							Log.d("MESSAGE", "check Not true! k=" + k
									+ ", msgComm.bytesForSave[k]= "
									+ msgComm.bytesForSave[k]);
						}
						// else{
						// Log.d("MESSAGE", "check is true! when k=" + k);
						// }

					}
					Util.setCheckCode(msgComm.bytesForSave, 290); // adding
																	// check
																	// code for
																	// byte
																	// array.
					commStatus = COMM_CONNECTION;

					inputPWD();
				} else { // Show warning toast: Device is not connected!
					Toast.makeText(getApplicationContext(),
							getString(R.string.not_connected),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}