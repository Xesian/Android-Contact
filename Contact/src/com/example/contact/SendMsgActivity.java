package com.example.contact;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class SendMsgActivity extends Activity {

	private String number;
	private EditText e1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_send_msg);
		number=getIntent().getExtras().getString("num");
		e1=(EditText)findViewById(R.id.sendto);
		e1.setText(number);
		
		ActionBar actionBar=getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		super.onCreate(savedInstanceState);
	}
	
	//创建ActionBar 菜单栏
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.send_msg, menu);
       return super.onCreateOptionsMenu(menu);  
    }      
  //对ActionBar中的事件进行响应
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {   
        case android.R.id.home:finish();  
        	return true; 
        	case R.id.sengMsg:
        		EditText e2=(EditText)findViewById(R.id.msg);
        		sendSMS(number,e2.getText().toString());
        		Toast.makeText(this, "发送消息"+e2.getText().toString()+"成功!", Toast.LENGTH_SHORT).show();
        		e2.setText("");
        		finish();
        		return true;
        default:  
            return super.onOptionsItemSelected(item);  
        }  
    }
    /**
     * 直接调用短信接口发短信，不含发送报告和接受报告
     * 
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }
}
