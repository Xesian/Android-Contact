package com.example.contact;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyContactActivity extends Activity {

	
	private EditText e1,e2;
	private String name,number,contactId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_contact);
		
		
		name=getIntent().getExtras().getString("name");
		number=getIntent().getExtras().getString("number");
		contactId=getIntent().getExtras().getString("contactId");
		
		Log.v("Id", contactId);
		
		e1=(EditText)findViewById(R.id.nam);
		e2=(EditText)findViewById(R.id.num);
		
		e1.setText(name);
		e2.setText(number);
		
		ActionBar actionBar=getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	//对ActionBar中的事件进行响应
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {   
        case android.R.id.home:finish();  
        	return true; 
        	case R.id.modifyDone:
        		updateContact();
        		finish();
        		return true;
        default:  
            return super.onOptionsItemSelected(item);  
        }  
    }
    
    private boolean updateContact()
    {
    	if(e1.getText().toString().length()>0&& e2.getText().toString().length()>0)
    	{
	    	//ContentValues values = new ContentValues();      
	        Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作 
	        ContentResolver resolver = getContentResolver(); 
	        ContentValues values = new ContentValues();  
	        
	        values.clear();
	        
	        values.put(StructuredName.GIVEN_NAME, e1.getText().toString());
	        
	        int rc1=resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{StructuredName.CONTENT_ITEM_TYPE,contactId+""});
	        
	        values.clear();
	        values.put("data1", e2.getText().toString());  
	        int rc2=resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2",contactId+""});
	    	if((rc1>0 || rc2 >0))
	    		Toast.makeText(this,"更新联系人"+e1.getText().toString()+"成功",Toast.LENGTH_SHORT).show();
	        return (rc1>0 ||rc2 >0) ? true : false;  
    	}
    	else 
    	{
    		Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	
    	
    }
  //创建ActionBar 菜单栏
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.modify_contact, menu);
       return super.onCreateOptionsMenu(menu);  
    }      
}
