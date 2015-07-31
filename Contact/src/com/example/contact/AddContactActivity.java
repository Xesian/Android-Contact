package com.example.contact;

import com.example.contact.MainActivity.MyListAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		 
			ActionBar actionBar=getActionBar();
	        actionBar.show();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	}
	//��ActionBar�е��¼�������Ӧ
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {   
        case android.R.id.home:finish();  
        	return true;
        case R.id.addperson_done:
        	addContact();
        	finish();
            return true; 
        default:  
            return super.onOptionsItemSelected(item);  
        }  
    }  
	private void addContact()
	{
		EditText name=(EditText)findViewById(R.id.name);
		EditText number=(EditText)findViewById(R.id.number1);
		
	   if(name.length()>0 && number.length()>0 )
	   {
		ContentValues values=new ContentValues();
		
		Uri rewContactUri=getContentResolver().insert(RawContacts.CONTENT_URI, values);
		
		long rawContactId=ContentUris.parseId(rewContactUri);
		
		
		
		//��ողż�¼�в����������
		values.clear();
		//��data �� ��������
		values.put(Data.RAW_CONTACT_ID, rawContactId);//��¼
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, name.getText().toString());
		getContentResolver().insert(
		android.provider.ContactsContract.Data.CONTENT_URI, values);
		
		//��data ����绰����
		values.clear();
		values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, number.getText().toString());
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,values);
		
		Toast.makeText(this, "�����ϵ��"+name.getText().toString()+"�ɹ�",Toast.LENGTH_SHORT ).show();
	   }
	   else {
		Toast.makeText(this, "����������Ϣ",Toast.LENGTH_SHORT).show();
	}
		
	}
	//����ActionBar �˵���
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.add_contact, menu);
       return super.onCreateOptionsMenu(menu);  
    }      
}
