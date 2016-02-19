package inc.gb.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends Activity implements OnClickListener {
private Button btn_save;
private EditText edit_first,edit_last;
private DbHelper mHelper;
private SQLiteDatabase dataBase;
private String id,fname,lname;
private boolean isUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        
        btn_save=(Button)findViewById(R.id.save_btn);
        edit_first=(EditText)findViewById(R.id.frst_editTxt);
        edit_last=(EditText)findViewById(R.id.last_editTxt);
         
       isUpdate=getIntent().getExtras().getBoolean("update");
        if(isUpdate)
        {
        	id=getIntent().getExtras().getString("ID");
        	fname=getIntent().getExtras().getString("Fname");
        	lname=getIntent().getExtras().getString("Lname");
        	edit_first.setText(fname);
        	edit_last.setText(lname);
        	
        }
         
         btn_save.setOnClickListener(this);
         
         mHelper=new DbHelper(this);
        
    }

    
	public void onClick(View v) {// saveButton click event 
		fname=edit_first.getText().toString().trim();
		lname=edit_last.getText().toString().trim();
		if(fname.length()>0 && lname.length()>0)
		{
			saveData();
		}
		else
		{
			AlertDialog.Builder alertBuilder=new AlertDialog.Builder(AddActivity.this);
			alertBuilder.setTitle("Invalid Data");
			alertBuilder.setMessage("Please, Enter valid data");
			alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
					
				}
			});
			alertBuilder.create().show();
		}
		
	}

	/**
	 * save data into SQLite
	 */
	private void saveData(){
		dataBase=mHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(DbHelper.KEY_FNAME,fname);
		values.put(DbHelper.KEY_LNAME,lname );
		
		System.out.println("");
		if(isUpdate)
		{    
			 
			dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID+"="+id, null);//update database with new data
		}
		else
		{
			
			dataBase.insert(DbHelper.TABLE_NAME, null, values);//insert data into database
		}

		dataBase.close();		//close database
		finish();
		
		
	}

}
