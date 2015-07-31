package com.example.contact;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ListActivity {
	
	private SearchView mSearchView;
	//private SearchView mSearchView=null;
	Context mcontext=null;
	
	//��ϵ����ʾ��������
		private static final int PHONES_DISPLAY_NAME_INDEX=0;
	//��ϵ�˵绰��������
	private static final int PHONES_NUMBER_INDEX=1;
	
	//��ϵ��ͷ�� ID����
	private static final int PHONES_PHOTO_ID_INDEX=2;
	
	private static final int PHONES_CONTACT_ID_INDEX=3;
	
	private ArrayList<String> mContactName=new ArrayList<String>();
	
	private ArrayList<String>mContactNumber=new ArrayList<String>();
	
	private ArrayList<Bitmap>mContactPhoto=new ArrayList<Bitmap>();
	
	private ArrayList<Long>mContactId=new ArrayList<Long>();
	//Ҫ��ʾ����ϵ�˵���Ϣ��
	private static final String[] PhoneProjection=new String[]
			{
				Phone.DISPLAY_NAME,
				Phone.NUMBER,
				Phone.PHOTO_ID,
				Phone.CONTACT_ID
			};
	
	 ListView mListView = null;  

	 MyListAdapter myAdapter = null;
	 
	 
	 public  void  onResume() {
		super.onResume();
		getPhoneContacts();
		myAdapter.notifyDataSetChanged();
	}
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        mcontext = this;  

	        mListView = this.getListView();  

	        /**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/  

	        getPhoneContacts();  

	        myAdapter = new MyListAdapter(this);  

	        
	        setListAdapter(myAdapter);      
	        ActionBar actionBar=getActionBar();
	        actionBar.show();
	        //actionBar.setDisplayHomeAsUpEnabled(true);//��ͼ����е���	        
	        this.registerForContextMenu(mListView);
	        super.onCreate(savedInstanceState); 
	    }	 
	 private void OnAddContactClick(View v)
	 {
		 Intent intent=new Intent();
		 intent.setClass(this, AddContactActivity.class);
		 startActivity(intent);
	 }
	 private void OnModifyContact(final String mName,final String mNumber,final String mContactId)
	 {
		 Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("������Ϣ");
			builder.setMessage("�Ƿ������Ϣ?");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					 Intent intent=new Intent();
					 intent.setClass(MainActivity.this, ModifyContactActivity.class);
					 if(mName.length()>0 && mNumber.length()>0&& mContactId.length()>0 )
					 {
						 intent.putExtra("name", mName);
						 intent.putExtra("number",mNumber);
						 intent.putExtra("contactId",mContactId);
						 startActivity(intent);
					 }
				}
			});
			
			
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();
				}
			});
			
			builder.setCancelable(false);		
			Dialog dialog = builder.create();
			dialog.show();		 
	 }
	 
	 
	 private void OnSendMsg(final String num)
	 {
		 Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("���Ͷ���");
			builder.setMessage("�Ƿ��Ͷ�����?");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					 Intent intent=new Intent();
					 intent.setClass(MainActivity.this, SendMsgActivity.class);
					 intent.putExtra("num", num);
					 startActivity(intent);
				}
			});
			
			
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();
				}
			});	
			builder.setCancelable(false);		
			Dialog dialog = builder.create();
			dialog.show();
		
	 }
	 //��ȡ��ϵ��
	private void  getPhoneContacts() {
		
		mContactName.clear();
		mContactNumber.clear();
		mContactPhoto.clear();
		mContactId.clear();
		ContentResolver resolver=mcontext.getContentResolver();
		
		Cursor c=resolver.query(Phone.CONTENT_URI,PhoneProjection, null,null,Data.DISPLAY_NAME);
		
		if(c!=null)
		{
			while(c.moveToNext())
			{
				//�õ��ֻ�����
				String phoneNumber=c.getString(PHONES_NUMBER_INDEX);
				
				//���ֻ�����Ϊ�ջ���Ϊ���ֶ�ʱ������ǰѭ��
				if(TextUtils.isEmpty(phoneNumber))
				{
					continue;
				}
				
				//�õ���ϵ������
				String contactName=c.getString(PHONES_DISPLAY_NAME_INDEX);
				
				//�õ���ϵ��ID
				Long contactID=c.getLong(PHONES_CONTACT_ID_INDEX);
				
				//�õ���ϵ����ƬID
				Long photoID=c.getLong(PHONES_PHOTO_ID_INDEX);
				
				Bitmap contactPhoto=null;
				BitmapFactory.Options opts = new BitmapFactory.Options();  
			    //opts.inJustDecodeBounds = true;
				
				opts.inSampleSize=4;
				//photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�  
				if(photoID>0)
				{
					Uri uri=ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactID);
					InputStream input=ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
					contactPhoto=BitmapFactory.decodeStream(input,null,opts);
				}
				else {
				contactPhoto=BitmapFactory.decodeResource(getResources(),R.drawable.pic,opts);
				}
				mContactName.add(contactName);
				mContactNumber.add(phoneNumber);
				mContactPhoto.add(contactPhoto);
				mContactId.add(contactID);
			}
			c.close();
		}
		
	}
	
	//�Զ���ListAdapter
	class MyListAdapter extends BaseAdapter {  

	    public MyListAdapter(Context context) {  

	        mcontext = context;  

	    }  

	    public int getCount() {  

	        //���û�������  

	        return mContactName.size();  

	    }  

	    @Override  

	    public boolean areAllItemsEnabled() {  

	        return false;  

	    }  

	    public Object getItem(int position) {  

	        return position;  

	    }  

	    public long getItemId(int position) {  

	        return position;  

	    }  

	    public View getView(int position, View convertView, ViewGroup parent) { 
	        View view = convertView;
	        ViewHolder holder; 
	     if (view == null) { 
	          holder = new ViewHolder();
	          view = LayoutInflater.from(getApplicationContext()).inflate( 
	         R.layout.list_contacts, null); 
	     
	     holder.image = (ImageView) view.findViewById(R.id.color_image); 
	     holder.title = (TextView) view.findViewById(R.id.color_title); 
	     holder.text = (TextView) view.findViewById(R.id.color_text);
	     holder.title.setTextColor(Color.BLACK);
	     view.setTag(holder);
	     }else{
	               holder = (ViewHolder) view.getTag();      
	              // Log.i(TAR, "ʹ�û����view");
	        }
	     //������ϵ������ 
	     holder.title.setText(mContactName.get(position)); 
	     //������ϵ�˺��� 
	     holder.text.setText(mContactNumber.get(position)); 
	     //������ϵ��ͷ�� 
	     holder.image.setImageBitmap(mContactPhoto.get(position)); 
	     return view; 
	   } 

	   } 
	   
	   static class ViewHolder{
	         ImageView image;
	         TextView title;      
	         TextView text;
	        
	   }
    
    
    //����ActionBar �˵���
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.main, menu); 
        
        MenuItem menuItem=menu.findItem(R.id.action_search);
        
        mSearchView=(SearchView)menuItem.getActionView();
        mSearchView.setQueryHint("info");
        
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        
        
        
       mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
		    	   
		@Override
		public boolean onQueryTextSubmit(String query) {
			
			//QueryContact(query);
			// TODO Auto-generated method stub
			return false;
		}
		
		
		@Override
		public boolean onQueryTextChange(String newText) {
			if(newText.length()>0)
			{
				mContactName.clear();
				mContactNumber.clear();
				mContactPhoto.clear();
				mContactId.clear();
				myAdapter.notifyDataSetChanged();
				QueryContact(newText);
			}	
			else {
			
				getPhoneContacts();
				myAdapter.notifyDataSetChanged();
			}
			//setListAdapter(myAdapter);
			// TODO Auto-generated method stub
			return false;
		}
	});
        return super.onCreateOptionsMenu(menu);
    }  
    
	public void QueryContact(String search)
	{
		/*if(search.length()>0)
		{
			mContactName.clear();
			mContactNumber.clear();
			mContactPhoto.clear();
			mContactId.clear();
			myAdapter.notifyDataSetChanged();
			*/
			//Uri uri = Uri.parse("content://com.android.contacts/data/phone/filter/" + search); 
			ContentResolver resolver =getContentResolver(); 
			//Cursor cursor = resolver.query(uri,PhoneProjection, null, null, null); 
			Cursor cursor = resolver.query(Phone.CONTENT_URI,PhoneProjection, 
					ContactsContract.Contacts.DISPLAY_NAME + " LIKE ? "+" OR " + Phone.NUMBER+" LIKE? ",new String[] {"%"+search+"%", "%"+search+"%"}, null); 
			if(cursor!=null)
			{
				while(cursor.moveToNext()) { 
					
					
				String name = cursor.getString(0); 
				String number=cursor.getString(PHONES_NUMBER_INDEX);
				Long contactID=cursor.getLong(PHONES_CONTACT_ID_INDEX);
				
				//�õ���ϵ����ƬID
				Long photoID=cursor.getLong(PHONES_PHOTO_ID_INDEX);
				
				Bitmap contactPhoto=null;
				
				BitmapFactory.Options opts = new BitmapFactory.Options();  
				
				opts.inSampleSize=4;
				//photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�  
				if(photoID>0)
				{
					Uri uri1=ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactID);
					InputStream input=ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri1);
					contactPhoto=BitmapFactory.decodeStream(input,null,opts);
				}
				else {
					contactPhoto=BitmapFactory.decodeResource(getResources(),R.drawable.pic,opts);
				}
				mContactName.add(name);
				mContactNumber.add(number);
				mContactPhoto.add(contactPhoto);
				mContactId.add(contactID);
				myAdapter.notifyDataSetChanged();
				} 
				cursor.close(); 
			}
			else {
				
				getPhoneContacts();
				myAdapter.notifyDataSetChanged();
			}
			/*}
		else {
			getPhoneContacts();
			myAdapter.notifyDataSetChanged();
		}*/
	}
	
	
	 private void CallSomeOne(final String number)
	 {
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("����绰");
			builder.setMessage("�Ƿ񲦴�绰��?");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri  
				             .parse("tel:" + number)); 
					 startActivity(dialIntent);	
					 Toast.makeText(MainActivity.this, "����绰", Toast.LENGTH_SHORT).show();
				}
			});
			
			
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();
				}
			});
			
			builder.setCancelable(false);		
			Dialog dialog = builder.create();
			dialog.show();
	 }
    //�û������ַ�ʱ�����÷���
    //public  boolean  onQueryTextChange(String newtext) {
    	
    //	Toast.makeText(this, newtext+"AA" , Toast.LENGTH_SHORT);
	//	return true;
   // }
    
    //��ActionBar�е��¼�������Ӧ
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
    	
        switch (item.getItemId()) {   
        //case android.R.id.home:
        //	getActionBar().setDisplayHomeAsUpEnabled(true);
       // return true;
        
        case R.id.addPerson:  
            this.OnAddContactClick(getListView());
            return true;  
        default:  
            return super.onOptionsItemSelected(item);  
        }  
    }  
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		 ContextMenuInfo menuInfo) {
    	 menu.setHeaderTitle("ѡ��");//���������Ĳ˵���Ŀ
    	 menu.setHeaderIcon(R.drawable.dianhua);//���������Ĳ˵���Ŀͼ��
    	 menu.add(1,0,1,"����绰");
    	 menu.add(1,1,1,"���Ͷ���");
    	 menu.add(1,2,1,"�޸���ϵ��");
    	 menu.add(1, 3, 1, "ɾ����ϵ��");
    }
    @Override
    
    public boolean onContextItemSelected(MenuItem item)
    {
    	ContextMenuInfo info = item.getMenuInfo();
    	AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo)info;
    	  // ��ȡѡ����λ��
    	int position = contextMenuInfo.position;
    	Log.v("pos", String.valueOf(position));
    	switch (item.getItemId()) {
		
    	case 0:
    		CallSomeOne(mContactNumber.get(position));
    		return true;
    	case 1:
    		OnSendMsg(mContactNumber.get(position));
    		return true;
		case 2:
			OnModifyContact(mContactName.get(position),mContactNumber.get(position),String.valueOf(mContactId.get(position)));
			return true;
		case 3:
			delContact(String.valueOf(mContactId.get(position)));
			return true;
			
		default:
			return false;
		}
    }
    
    private void delContact(final String contatID)
    {
    	Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ɾ����ϵ��");
		builder.setMessage("�Ƿ�ɾ����ϵ����?");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			        ContentResolver resolver = getContentResolver();  
			        Cursor cursor = resolver.query(Phone.CONTENT_URI, PhoneProjection,"raw_contact_id=?", new String[]{contatID}, null);
			        if(cursor!=null)
			        {
				        while(cursor.moveToNext()){   
				            
				            //ɾ��data��������  
				            String where = ContactsContract.Data.CONTACT_ID + " =?";  
				            String[] whereparams = new String[] { cursor.getString(3) };  
				            resolver.delete(ContactsContract.Data.CONTENT_URI, where, whereparams);  
				            
				          //ɾ��rawContact��������  
				            where = ContactsContract.RawContacts.CONTACT_ID + " =?";  
				            whereparams = new String[] { cursor.getString(3) };  
				            resolver.delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams);            
				            Toast.makeText(MainActivity.this, "ɾ��"+cursor.getString(0)+"�ɹ�", Toast.LENGTH_SHORT).show();
				        }  
			        }
			        getPhoneContacts();
					myAdapter.notifyDataSetChanged();
			}
		});	
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "ȡ��", Toast.LENGTH_SHORT).show();
			}
		});
		
		builder.setCancelable(false);		
		Dialog dialog = builder.create();
		dialog.show();
    	
    }
}
