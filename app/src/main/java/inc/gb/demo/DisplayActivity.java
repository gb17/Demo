package inc.gb.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayActivity extends Activity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;

    private ArrayList<String> userId = new ArrayList<String>();
    ;
    private ArrayList<String> user_fName = new ArrayList<String>();
    private ArrayList<String> user_lName = new ArrayList<String>();


    private ListView userList;
    private AlertDialog.Builder build;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        userList = (ListView) findViewById(R.id.List);

        mHelper = new DbHelper(this);
        displayData();

        findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {//add new record

            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),
                        AddActivity.class);
                i.putExtra("update", false);
                startActivity(i);

            }
        });


        userList.setOnItemClickListener(new OnItemClickListener() {//click to update data

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent i = new Intent(getApplicationContext(),
                        AddActivity.class);
                i.putExtra("Fname", user_fName.get(arg2));
                i.putExtra("Lname", user_lName.get(arg2));
                i.putExtra("ID", userId.get(arg2));
                i.putExtra("update", true);
                startActivity(i);

            }
        });


        userList.setOnItemLongClickListener(new OnItemLongClickListener() {    //long click to delete data

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {

                build = new AlertDialog.Builder(DisplayActivity.this);
                build.setTitle("Delete " + user_fName.get(arg2) + " "
                        + user_lName.get(arg2));
                build.setMessage("Do you want to delete ?");
                build.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        user_fName.get(arg2) + " "
                                                + user_lName.get(arg2)
                                                + " is deleted.", Toast.LENGTH_LONG).show();

                                dataBase.delete(
                                        DbHelper.TABLE_NAME,
                                        DbHelper.KEY_ID + "="
                                                + userId.get(arg2), null);
                                displayData();
                                dialog.cancel();
                            }
                        });

                build.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = build.create();
                alert.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    //displays data from SQLite
    private void displayData() {
        dataBase = mHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "
                + DbHelper.TABLE_NAME, null);




        userId = new ArrayList<String>();
        user_fName = new ArrayList<String>();
        user_lName = new ArrayList<String>();
        if (mCursor.moveToFirst()) {
            do {
                userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
                user_fName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_FNAME)));
                user_lName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_LNAME)));

            } while (mCursor.moveToNext());
        }
        DisplayAdapter disadpt = new DisplayAdapter(DisplayActivity.this, userId, user_fName, user_lName);
        userList.setAdapter(disadpt);
        mCursor.close();


    }


}
