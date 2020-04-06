package com.example.myapplication;



import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    EditText editRollno,editName,editMarks,Discount,Flavours,nameadd ;
    Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo,btnSend;
    SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editRollno=(EditText)findViewById(R.id.editRollno);
       // editDob=(EditText)findViewById(R.id.editRollno);
        editName=(EditText)findViewById(R.id.editName);
        editMarks=(EditText)findViewById(R.id.editMarks);
        //editemail=(EditText)findViewById(R.id.editMarks);
        Discount=(EditText)findViewById(R.id.Discount);
        Flavours=(EditText)findViewById(R.id.Flavours);
        nameadd=(EditText)findViewById(R.id.nameadd);
        btnAdd=(Button)findViewById(R.id.btnAdd);

        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnModify=(Button)findViewById(R.id.btnModify);
        btnView=(Button)findViewById(R.id.btnView);
        btnViewAll=(Button)findViewById(R.id.btnViewAll);
        btnShowInfo=(Button)findViewById(R.id.btnShowInfo);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnShowInfo.setOnClickListener(this);
        String message = "";
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }
    public void onClick(View view)
    {
        if(view==btnAdd)

        {
            if(editRollno.getText().toString().trim().length()==0 ||
                    editName.getText().toString().trim().length()==0 ||
                    editMarks.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO student VALUES('"+editRollno.getText()+"','"+editName.getText()+
                    "','"+editMarks.getText()+"');");
            showMessage("Success", "Record added");
            clearText();
        }
        if(view==btnDelete)
        {
            if(editRollno.getText().toString().trim().length()==0||
                    editName.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter DOB,NAME");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"' AND name ='"+editName.getText()+"'",   null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM student  WHERE rollno='"+editRollno.getText()+"' AND name ='"+editName.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid DOB or NAME");
            }
            clearText();
        }
        if(view==btnModify)
        {
            if(editRollno.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter DOB,NAME");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student  WHERE rollno='"+editRollno.getText()+"' AND name ='"+editName.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE student SET name='"+editName.getText()+"',marks='"+editMarks.getText()+
                        "'  WHERE rollno='"+editRollno.getText()+"' AND name ='"+editName.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else
            {
                showMessage("Error", "Invalid DOB or NAME ");
            }
            clearText();
        }
        if(view==btnView)
        {
            if(editRollno.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter DOB");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            int i=c.getCount();
            while(c.moveToNext())
            {
                buffer.append("DOB: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Friends Mail: "+c.getString(2)+"\n\n");
            }
            showMessage("TODAYS BIRTHDAY ", "count:"+ i +"\n" + buffer.toString());
        }
        if(view==btnSend)
        {
            if(editRollno.getText().toString().trim().length()==0||
                    Discount.getText().toString().trim().length()==0||
                    Flavours.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter DOB,DISCOUNT AND FLAVOURS");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editRollno.getText()+"'", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            while(c.moveToNext())
            {
               String message="We at "+ nameadd.getText()+" are glad to inform you that today is your friend " +c.getString(1)+ "'s"
            + " Birthday !"+"\n"+ "Hence we are offering you a "+ Discount.getText()+ " % Discount on our amazing Cake Flavours :"+ Flavours.getText()+"\n"+" THANK YOU..." ;
               String[] TO= { c.getString(2) };
               String[] CC={};
               Intent emailIntent=new Intent(Intent.ACTION_SEND);
               emailIntent.setData(Uri.parse("mailto:"));
               emailIntent.setType("text/plan");

               emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "BIRTHDAY OF YOUR FRIEND");
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);

                try{
                    startActivity(Intent.createChooser(emailIntent, "send email...."));
                    finish();
                    Log.i("Finished sending email","");
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this, "there is no Messenger field", Toast.LENGTH_SHORT).show();
                }


            }
        }
        if(view==btnViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();

            while(c.moveToNext())
            {
                buffer.append("DOB: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Friends Mail: "+c.getString(2)+"\n\n");
            }
            showMessage("DATA", buffer.toString());
        }
        if(view==btnShowInfo)
        {
            showMessage("CAKE SHOP APPLICATION", "Developed By AKASH KURUND , KUNAL BHALGAT , AADITYA PARDESHI"+ "\n"
            + "Guided By : Prof. Kirti Panmand ");
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        editRollno.setText("");

        editName.setText("");
        editMarks.setText("");

        editRollno.requestFocus();

    }
}
