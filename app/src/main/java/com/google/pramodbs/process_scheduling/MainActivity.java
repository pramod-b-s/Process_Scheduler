package com.google.pramodbs.process_scheduling;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnfcfs,btnsjf,btnsrtf,btnnpprio,btnpempprio,btnrr,btnmlq,simulate;
    private TextView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnfcfs=(Button)findViewById(R.id.fcfsbtn);
        btnsjf=(Button)findViewById(R.id.sjfbtn);
        btnsrtf=(Button)findViewById(R.id.srtfbtn);
        btnnpprio=(Button)findViewById(R.id.nppriobtn);
        btnpempprio=(Button)findViewById(R.id.pemppriobtn);
        btnrr=(Button)findViewById(R.id.rrbtn);
        btnmlq=(Button)findViewById(R.id.mlqbtn);
        simulate=(Button)findViewById(R.id.simul);

        btnfcfs.setOnClickListener(this);
        btnsjf.setOnClickListener(this);
        btnsrtf.setOnClickListener(this);
        btnnpprio.setOnClickListener(this);
        btnpempprio.setOnClickListener(this);
        btnrr.setOnClickListener(this);
        btnmlq.setOnClickListener(this);
        simulate.setOnClickListener(this);

        more=(TextView)findViewById(R.id.learnmore);
        more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==simulate){
            Intent intent=new Intent(MainActivity.this,SimulationActivity.class);
            startActivity(intent);
        }
        if(v==btnfcfs) {
            Intent intent=new Intent(MainActivity.this,fcfsActivity.class);
            startActivity(intent);
        }
        if(v==btnsjf) {
            Intent intent=new Intent(MainActivity.this,sjfActivity.class);
            startActivity(intent);
        }
        if(v==btnsrtf) {
            Intent intent=new Intent(MainActivity.this,srtfActivity.class);
            startActivity(intent);
        }
        if(v==btnnpprio) {
            Intent intent=new Intent(MainActivity.this,npprioActivity.class);
            startActivity(intent);
        }
        if(v==btnpempprio) {
            Intent intent=new Intent(MainActivity.this,pempprioActivity.class);
            startActivity(intent);
        }
        if(v==btnrr) {
            Intent intent=new Intent(MainActivity.this,rrActivity.class);
            startActivity(intent);
        }
        if(v==btnmlq) {
            Intent intent=new Intent(MainActivity.this,mlqActivity.class);
            startActivity(intent);
        }
        if(v==more){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cs.uic.edu/~jbell/CourseNotes/OperatingSystems/5_CPU_Scheduling.html\n"));
            startActivity(browserIntent);
        }
    }
}
