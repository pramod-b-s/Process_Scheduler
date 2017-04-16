package com.google.pramodbs.process_scheduling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

import static android.graphics.Color.*;

public class SimulationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnfcfs,btnsjf,btnsrtf,btnnpprio,btnpempprio,btnrr,btnmlq,processShow;
    private TextView txtfcfs,txtsjf,txtsrtf,txtnpprio,txtpempprio,txtrr,txtmlq;
    private EditText rrtq,nopro;
    private TextView fcfspopupmsg,sjfpopupmsg,srtfpopupmsg,nppriopopupmsg,pemppriopopupmsg,rrpopupmsg,mlqpopupmsg;

    private TextView[] heading=new TextView[100];
    private EditText[] at=new EditText[100];
    private EditText[] bt=new EditText[100];
    private EditText[] prio=new EditText[100];

    private TextView[] atResult=new TextView[100];
    private TextView[] btResult=new TextView[100];
    private TextView[] ctResult=new TextView[100];
    private TextView[] tatResult=new TextView[100];
    private TextView[] wtResult=new TextView[100];

    private int procount=0,removed=1,localsize=0;
    private float avgwt=0,avgtat=0;
    private LinearLayout linearLayout,linearLayoutprio;

    private int f=-1,r=-1,size=0,k=0,x,time=0,i,tq,n1,n2,localsizehigh,localsizelow,k1,k2;
    private int f1=-1,r1=-1,f2=-1,r2=-1,size1,size2,startvar=0,endvar=0,count=0;
    private int[] q=new int[100];
    //private int[] q1=new int[100];
    //private int[] q2=new int[100];
    private int[] number=new int[100];
    private int[] atArray=new int[100];
    private int[] btArray=new int[100];
    private int[] ctArray=new int[100];
    private int[] tatArray=new int[100];
    private int[] done=new int[100];
    private int[] vis=new int[100];
    private int[] wtArray=new int[100];
    private int[] tsArray=new int[100];
    private int[] prioArray=new int[100];
    private int[] startArray=new int[100];
    private int[] endArray=new int[100];

    //private fcfs[] process=new fcfs[100];

    //private PopupWindow fcfspopup,sjfpopup,srtfpopup,nppriopopup,pemppriopopup,rrpopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        nopro=(EditText)findViewById(R.id.numpro);
        rrtq=(EditText)findViewById(R.id.tq);

        removed=1;
        linearLayout=(LinearLayout)findViewById(R.id.linear2);
        linearLayoutprio=(LinearLayout)findViewById(R.id.linear_prio);

        processShow=(Button)findViewById(R.id.showpro);
        processShow.setOnClickListener(this);

        btnfcfs=(Button)findViewById(R.id.fcfsbtn);
        btnsjf=(Button)findViewById(R.id.sjfbtn);
        btnsrtf=(Button)findViewById(R.id.srtfbtn);
        btnnpprio=(Button)findViewById(R.id.nppriobtn);
        btnpempprio=(Button)findViewById(R.id.pemppriobtn);
        btnrr=(Button)findViewById(R.id.rrbtn);
        //btnmlq=(Button)findViewById(R.id.mlqbtn);

        btnfcfs.setOnClickListener(this);
        btnsjf.setOnClickListener(this);
        btnsrtf.setOnClickListener(this);
        btnnpprio.setOnClickListener(this);
        btnpempprio.setOnClickListener(this);
        btnrr.setOnClickListener(this);
        //btnmlq.setOnClickListener(this);

        txtfcfs=(TextView)findViewById(R.id.fcfsabt);
        txtsjf=(TextView)findViewById(R.id.sjfabt);
        txtsrtf=(TextView)findViewById(R.id.srtfabt);
        txtnpprio=(TextView)findViewById(R.id.npprioabt);
        txtpempprio=(TextView)findViewById(R.id.pempprioabt);
        txtrr=(TextView)findViewById(R.id.rrabt);
        //txtmlq=(TextView)findViewById(R.id.mlqabt);

        txtsjf.setOnClickListener(this);
        txtfcfs.setOnClickListener(this);
        txtrr.setOnClickListener(this);
        txtpempprio.setOnClickListener(this);
        txtnpprio.setOnClickListener(this);
        txtsrtf.setOnClickListener(this);
        //txtmlq.setOnClickListener(this);

    }

    public void nq(int x){
        q[++r]=x;
        size++;
    }

    public int dq(){
        size--;
        return q[++f];
    }


    public int done(int n){
        int i;
        for(i=0;i<n;i++){
            if(done[i]==0){
                return 0;
            }
        }
        return 1;
    }

    public int sjf(int n){
        int i,min=999,mini=0;
        for(i=0;i<n;i++){
            if((btArray[i]<min) && (done[i]==0) && (vis[i]==1)){
                min=btArray[i];
                mini=i;
            }
        }
        return mini;
    }

    int srtf(int n){
        int i,min=999,mini=0;
        for(i=0;i<n;i++){
            if((btArray[i]-tsArray[i]<min) && (done[i]==0) && (vis[i]==1)){
                min=btArray[i]-tsArray[i];
                mini=i;
            }
        }
        return mini;
    }

    int pickprio(int n){
        int i,min=999,mini=0;
        for(i=0;i<n;i++){
            if((prioArray[i]<min) && (done[i]==0) && (vis[i]==1)){
                min=prioArray[i];
                mini=i;
            }
        }
        return mini;
    }

    @Override
    public void onClick(View v) {

        if(v==processShow) {
            if(removed==0){
                linearLayout.removeAllViews();
            }

            procount = Integer.parseInt(nopro.getText().toString());
            for (int i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process "+i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#000000"));
                linearLayout.addView(headText);

                EditText atText = new EditText(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setHint("Enter arrival time of Process "+i);
                atText.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayout.addView(atText);
                at[i]=atText;

                EditText btText = new EditText(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setHint("Enter burst time of Process "+i);
                btText.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayout.addView(btText);
                bt[i]=btText;

                TextView headTextprio = new TextView(this);
                headTextprio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headTextprio.setText("Priority of Process "+i+" (optional)");
                headTextprio.setTextSize(15);
                linearLayout.addView(headTextprio);

                EditText prioText = new EditText(this);
                prioText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                prioText.setHint("Enter priority (default priority=1)");
                prioText.setInputType(InputType.TYPE_CLASS_NUMBER);
                linearLayout.addView(prioText);
                prio[i]=prioText;
            }
            removed=0;
        }

        if(v==btnfcfs) {
            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            procount = Integer.parseInt(nopro.getText().toString());
            String test="";
            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());

                number[i]=i;
            }

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);
            //  canvas.drawRect(left, top, left+width, top+height, paint);
            while (done(procount) == 0) {
                while (k < procount) {
                    if (atArray[k] <= time) {
                        nq(number[k]);
                        k++;
                    } else {
                        break;
                    }
                }

                if (size != 0) {
                    x = dq();
                    startArray[x]=time;
                    time += btArray[x];
                    while (k < procount) {
                        if (atArray[k] <= time) {
                            nq(number[k]);
                            k++;
                        } else {
                            break;
                        }
                    }

                    ctArray[x] = time;
                    endArray[x]=time;
                    tatArray[x] = ctArray[x]-atArray[x];
                    wtArray[x] = tatArray[x]- btArray[x];
                    done[x] = 1;
                } else {
                    time++;
                    while (k < procount) {
                        if (atArray[k] <= time) {
                            nq(number[k]);
                            k++;
                        } else {
                            break;
                        }
                    }
                }
            }

            for (i = 0; i < procount; i++) {
                //Toast.makeText(this,ctArray[i]+" ", Toast.LENGTH_SHORT).show();
            }

            linearLayout.removeAllViews();

            for (int i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            avgwt/=procount;
            avgtat/=procount;

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            Paint paint = new Paint();
            int i;

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            for(i=0;i<procount;i++) {
                if (i == 0) {
                    paint.setColor(parseColor("#FF0000"));
                    canvas.drawRect(startArray[i] * 10, 10, endArray[i] * 10, 50, paint);
                }
                if (i == 1) {
                    paint.setColor(parseColor("#FF8C00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 2) {
                    paint.setColor(parseColor("#FFD700"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 3) {
                    paint.setColor(parseColor("#808000"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 4) {
                    paint.setColor(parseColor("#7CFC00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 5) {
                    paint.setColor(parseColor("#3CB371"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 6) {
                    paint.setColor(parseColor("#00FF7F"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 7) {
                    paint.setColor(parseColor("#00CED1"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 8) {
                    paint.setColor(parseColor("#191970"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 9) {
                    paint.setColor(parseColor("#DA70D6"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);

        }

        if(v==btnnpprio){
            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            procount = Integer.parseInt(nopro.getText().toString());
            String test="";
            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());
                if(prio[i].getText().toString().length()>=1) {
                    prioArray[i] = Integer.parseInt(prio[i].getText().toString());
                }
                else{
                    prioArray[i]=1;
                }
                tsArray[i]=0;
                number[i]=i;
            }

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);

            while(done(procount)==0){
                while(k<procount){
                    if(atArray[k]<=time){
                        //nq(p[k].no);
                        localsize++;
                        vis[k]=1;
                        k++;
                    }
                    else{
                        break;
                    }
                }

                if(localsize!=0){
                    x=pickprio(procount);
                    startArray[x]=time;
                    //printf("x=%d\n",x);
                    time+=btArray[x];
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }

                    ctArray[x] = time;
                    tatArray[x] = ctArray[x] - atArray[x];
                    wtArray[x] = tatArray[x] - btArray[x];
                    done[x] = 1;
                    localsize--;
                    endArray[x]=time;
                }
                else{
                    time++;
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }

            linearLayout.removeAllViews();

            for (int i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView prioText = new TextView(this);
                prioText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                prioText.setText("Priority  \t"+prioArray[i]);
                prioText.setTextColor(parseColor("#000000"));
                prioText.setTextSize(15);
                linearLayout.addView(prioText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            Paint paint = new Paint();
            int i;

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            for(i=0;i<procount;i++) {
                if (i == 0) {
                    paint.setColor(parseColor("#FF0000"));
                    canvas.drawRect(startArray[i] * 10, 10, endArray[i] * 10, 50, paint);
                }
                if (i == 1) {
                    paint.setColor(parseColor("#FF8C00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 2) {
                    paint.setColor(parseColor("#FFD700"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 3) {
                    paint.setColor(parseColor("#808000"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 4) {
                    paint.setColor(parseColor("#7CFC00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 5) {
                    paint.setColor(parseColor("#3CB371"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 6) {
                    paint.setColor(parseColor("#00FF7F"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 7) {
                    paint.setColor(parseColor("#00CED1"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 8) {
                    paint.setColor(parseColor("#191970"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 9) {
                    paint.setColor(parseColor("#DA70D6"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);

            avgwt/=procount;
            avgtat/=procount;
        }

        if(v==btnpempprio){
            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            startvar=0;
            endvar=0;
            count=0;
            procount = Integer.parseInt(nopro.getText().toString());
            String test="";

            Paint paint = new Paint();
            int i;

            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());
                if(prio[i].getText().toString().length()>=1) {
                    prioArray[i] = Integer.parseInt(prio[i].getText().toString());
                }
                else{
                    prioArray[i]=1;
                }
                tsArray[i]=0;
                number[i]=i;
            }

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);

            while(done(procount)==0){
                while(k<procount){
                    if(atArray[k]<=time){
                        //nq(p[k].no);
                        localsize++;
                        vis[k]=1;
                        k++;
                    }
                    else{
                        break;
                    }
                }

                if(localsize!=0){
                    x=pickprio(procount);
                    //printf("x=%d\n",x);
                    startvar=time;
                    time++;
                    count+=1;

                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }

                    tsArray[x]++;
                    if(tsArray[x]>=btArray[x]) {
                        ctArray[x] = time;
                        tatArray[x] = ctArray[x] - atArray[x];
                        wtArray[x] = tatArray[x] - btArray[x];
                        done[x] = 1;
                        localsize--;
                    }

                    endvar=time;

                    if (x == 0) {
                        paint.setColor(parseColor("#FF0000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 1) {
                        paint.setColor(parseColor("#FF8C00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 2) {
                        paint.setColor(parseColor("#FFD700"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 3) {
                        paint.setColor(parseColor("#808000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 4) {
                        paint.setColor(parseColor("#7CFC00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 5) {
                        paint.setColor(parseColor("#3CB371"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 6) {
                        paint.setColor(parseColor("#00FF7F"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 7) {
                        paint.setColor(parseColor("#00CED1"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 8) {
                        paint.setColor(parseColor("#191970"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 9) {
                        paint.setColor(parseColor("#DA70D6"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }

                }
                else{
                    time++;
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }

            linearLayout.removeAllViews();

            for (i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView prioText = new TextView(this);
                prioText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                prioText.setText("Priority  \t"+prioArray[i]);
                prioText.setTextColor(parseColor("#000000"));
                prioText.setTextSize(15);
                linearLayout.addView(prioText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            avgwt/=procount;
            avgtat/=procount;

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);

        }

        if(v==btnsjf){
            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            procount = Integer.parseInt(nopro.getText().toString());

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);

            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());

                number[i]=i;
            }

            int tmp=0,tmp1=20;
            while(done(procount)==0){
                while(k<procount){
                    if(atArray[k]<=time){
                        //nq(p[k].no);
                        localsize++;
                        vis[k]=1;
                        k++;
                    }
                    else{
                        break;
                    }
                }

                if(localsize!=0){
                    x=sjf(procount);
                    startArray[x]=time;
                    time+=btArray[x];
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }

                    ctArray[x] = time;
                    endArray[x]=time;
                    tatArray[x] = ctArray[x]-atArray[x];
                    wtArray[x] = tatArray[x]- btArray[x];
                    done[x] = 1;
                    localsize--;
                }
                else{
                    time++;
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }
                    tmp=time;
                }
            }

            //canvas.drawRe


            linearLayout.removeAllViews();

            for (int i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            Paint paint = new Paint();
            int i;

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            for(i=0;i<procount;i++) {
                if (i == 0) {
                    paint.setColor(parseColor("#FF0000"));
                    canvas.drawRect(startArray[i] * 10, 10, endArray[i] * 10, 50, paint);
                }
                if (i == 1) {
                    paint.setColor(parseColor("#FF8C00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 2) {
                    paint.setColor(parseColor("#FFD700"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 3) {
                    paint.setColor(parseColor("#808000"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 4) {
                    paint.setColor(parseColor("#7CFC00"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 5) {
                    paint.setColor(parseColor("#3CB371"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 6) {
                    paint.setColor(parseColor("#00FF7F"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 7) {
                    paint.setColor(parseColor("#00CED1"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 8) {
                    paint.setColor(parseColor("#191970"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
                if (i == 9) {
                    paint.setColor(parseColor("#DA70D6"));
                    canvas.drawRect(startArray[i] * 10 , 10, endArray[i] * 10, 50, paint);
                }
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);


            avgwt/=procount;
            avgtat/=procount;

        }

        if(v==btnsrtf){
            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            startvar=0;
            endvar=0;
            count=0;
            procount = Integer.parseInt(nopro.getText().toString());
            String test="";

            Paint paint = new Paint();
            int i;

            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());
                tsArray[i]=0;
                vis[i]=0;
                done[i]=0;
                number[i]=i;
            }

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);

            while(done(procount)==0){
                while(k<procount){
                    if(atArray[k]<=time){
                        //nq(p[k].no);
                        localsize++;
                        vis[k]=1;
                        k++;
                    }
                    else{
                        break;
                    }
                }

                if(localsize!=0){
                    x=srtf(procount);
                    //printf("x=%d\n",x);
                    startvar=time;
                    time++;
                    count+=1;

                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }

                    tsArray[x]++;
                    if(tsArray[x]>=btArray[x]) {
                        ctArray[x] = time;
                        tatArray[x] = ctArray[x] - atArray[x];
                        wtArray[x] = tatArray[x] - btArray[x];
                        done[x] = 1;
                        localsize--;
                    }

                    endvar=time;

                    if (x == 0) {
                        paint.setColor(parseColor("#FF0000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 1) {
                        paint.setColor(parseColor("#FF8C00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 2) {
                        paint.setColor(parseColor("#FFD700"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 3) {
                        paint.setColor(parseColor("#808000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 4) {
                        paint.setColor(parseColor("#7CFC00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 5) {
                        paint.setColor(parseColor("#3CB371"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 6) {
                        paint.setColor(parseColor("#00FF7F"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 7) {
                        paint.setColor(parseColor("#00CED1"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 8) {
                        paint.setColor(parseColor("#191970"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 9) {
                        paint.setColor(parseColor("#DA70D6"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }

                }
                else{
                    time++;
                    while(k<procount){
                        if(atArray[k]<=time){
                            //nq(p[k].no);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }

            linearLayout.removeAllViews();

            for (i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            avgwt/=procount;
            avgtat/=procount;

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);

        }

        if(v==btnrr){
            tq= Integer.parseInt(rrtq.getText().toString());

            time=0;
            localsize=0;
            size=0;
            avgwt=0;
            avgtat=0;
            startvar=0;
            endvar=0;
            count=0;
            procount = Integer.parseInt(nopro.getText().toString());
            String test="";

            Paint paint = new Paint();
            int i;

            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());
                tsArray[i]=0;
                number[i]=i;
            }

            Bitmap bg = Bitmap.createBitmap(600, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg);
            canvas.drawColor(LTGRAY);

            while(done(procount)==0){
                while(k<procount){
                    if(atArray[k]<=time){
                        nq(number[i]);
                        localsize++;
                        vis[k]=1;
                        k++;
                    }
                    else{
                        break;
                    }
                }

                if(localsize!=0){
                    x=dq();
                    startvar=time;
                    time+=tq;
                    count+=1;

                    while(k<procount){
                        if(atArray[k]<=time){
                            nq(number[k]);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }

                    tsArray[x]+=tq;
                    if(tsArray[x]>=btArray[x]) {
                        time-=(tsArray[x]-btArray[x]);
                        ctArray[x] =time;
                        tatArray[x] = ctArray[x] - atArray[x];
                        wtArray[x] = tatArray[x] - btArray[x];
                        done[x] = 1;
                        localsize--;
                    }
                    else{
                        nq(x);
                    }

                    endvar=time;

                    if (x == 0) {
                        paint.setColor(parseColor("#FF0000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 1) {
                        paint.setColor(parseColor("#FF8C00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 2) {
                        paint.setColor(parseColor("#FFD700"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 3) {
                        paint.setColor(parseColor("#808000"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 4) {
                        paint.setColor(parseColor("#7CFC00"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 5) {
                        paint.setColor(parseColor("#3CB371"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 6) {
                        paint.setColor(parseColor("#00FF7F"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 7) {
                        paint.setColor(parseColor("#00CED1"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 8) {
                        paint.setColor(parseColor("#191970"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }
                    if (x == 9) {
                        paint.setColor(parseColor("#DA70D6"));
                        canvas.drawRect(startvar * 10, 10, endvar * 10, 50, paint);
                    }

                }
                else{
                    time++;
                    while(k<procount){
                        if(atArray[k]<=time){
                            nq(number[k]);
                            localsize++;
                            vis[k]=1;
                            k++;
                        }
                        else{
                            break;
                        }
                    }
                }
            }

            linearLayout.removeAllViews();

            for (i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#008000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            avgwt/=procount;
            avgtat/=procount;

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#008000"));
            linearLayout.addView(avgwtText);

            String[] colors=new String[100];
            colors[0]="Red";
            colors[1]="dark orange";
            colors[2]="gold";
            colors[3]="olive";
            colors[4]="lawn green";
            colors[5]="medium sea green";
            colors[6]="spring green";
            colors[7]="dark turquoise";
            colors[8]="midnight blue";
            colors[9]="orchid";

            TextView ganttText = new TextView(this);
            ganttText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            ganttText.setText("Gantt Chart");
            ganttText.setTextSize(20);
            ganttText.setTextColor(parseColor("#FF8C00"));
            linearLayout.addView(ganttText);

            for(i=0;i<procount;i++) {
                TextView colorText = new TextView(this);
                colorText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                colorText.setText("Process " + i+"  -  "+colors[i]);
                colorText.setTextSize(15);
                linearLayout.addView(colorText);
            }

            ImageView iV = new ImageView(this);
            iV.setImageBitmap(bg);

            linearLayout.addView(iV);

        }

        if(v==txtfcfs) {
            Intent intent=new Intent(SimulationActivity.this,fcfsActivity.class);
            startActivity(intent);
        }
        if(v==txtsjf) {
            Intent intent=new Intent(SimulationActivity.this,sjfActivity.class);
            startActivity(intent);
        }
        if(v==txtsrtf) {
            Intent intent=new Intent(SimulationActivity.this,srtfActivity.class);
            startActivity(intent);
        }
        if(v==txtnpprio) {
            Intent intent=new Intent(SimulationActivity.this,npprioActivity.class);
            startActivity(intent);
        }
        if(v==txtpempprio) {
            Intent intent=new Intent(SimulationActivity.this,pempprioActivity.class);
            startActivity(intent);
        }
        if(v==txtrr) {
            Intent intent=new Intent(SimulationActivity.this,rrActivity.class);
            startActivity(intent);
        }
        /*if(v==txtmlq) {
            Intent intent=new Intent(SimulationActivity.this,mlqActivity.class);
            startActivity(intent);
        }*/
    }
}



        /*if(v==btnmlq){
            tq= Integer.parseInt(rrtq.getText().toString());

            time=0;
            localsizehigh=0;
            localsizelow=0;
            size1=0;
            size2=0;
            avgwt=0;
            avgtat=0;
            n1=0;
            n2=0;

            procount = Integer.parseInt(nopro.getText().toString());
            String test="";
            for(i=0;i<procount;i++) {
                //test+=at[i].getText()+" "+bt[i].getText()+" ";
                atArray[i]= Integer.parseInt(at[i].getText().toString());
                btArray[i]= Integer.parseInt(bt[i].getText().toString());
                prioArray[i] = Integer.parseInt(prio[i].getText().toString());
                if(prioArray[i]==1){
                    n1++;
                }
                if(prioArray[i]==2){
                    n2++;
                }

                tsArray[i]=0;
                number[i]=i;
            }
            k1=0;
            k2=n1;
            Queue<Integer> q1=new LinkedList<Integer>();
            Queue<Integer> q2=new LinkedList<Integer>();

            while(done(procount)==0){
                while(k1<n1){
                    if(atArray[k1]<=time){
                        q1.add(number[i]);
                        localsizehigh++;
                        vis[k1]=1;
                        k1++;
                    }
                    else{
                        break;
                    }
                }
                while(k2<procount){
                    if(atArray[k2]<=time){
                        q2.add(number[i]);
                        localsizelow++;
                        vis[k2]=1;
                        k2++;
                    }
                    else{
                        break;
                    }
                }

                if(localsizehigh!=0){
                    x=q1.remove();
                    time+=tq;
                    while(k1<n1){
                        if(atArray[k1]<=time){
                            q1.add(number[i]);
                            localsizehigh++;
                            vis[k1]=1;
                            k1++;
                        }
                        else{
                            break;
                        }
                    }
                    while(k2<procount){
                        if(atArray[k2]<=time){
                            q2.add(number[i]);
                            localsizelow++;
                            vis[k2]=1;
                            k2++;
                        }
                        else{
                            break;
                        }
                    }
                    tsArray[x]+=tq;
                    if(tsArray[x]>=btArray[x]) {
                        time-=(tsArray[x]-btArray[x]);
                        ctArray[x] =time;
                        tatArray[x] = ctArray[x] - atArray[x];
                        wtArray[x] = tatArray[x] - btArray[x];
                        done[x] = 1;
                        localsizehigh--;
                    }
                    else{
                        q1.add(x);
                    }
                }
                else if(localsizelow!=0){
                    x = q2.remove();
                    time += btArray[x];
                    while(k1<n1){
                        if(atArray[k1]<=time){
                            q1.add(number[i]);
                            localsizehigh++;
                            vis[k1]=1;
                            k1++;
                        }
                        else{
                            break;
                        }
                    }
                    while(k2<procount){
                        if(atArray[k2]<=time){
                            q2.add(number[i]);
                            localsizelow++;
                            vis[k2]=1;
                            k2++;
                        }
                        else{
                            break;
                        }
                    }

                    ctArray[x] = time;
                    tatArray[x] = ctArray[x]-atArray[x];
                    wtArray[x] = tatArray[x]- btArray[x];
                    done[x] = 1;
                    localsizelow--;
                }
                else{
                    time++;
                }
            }

            linearLayout.removeAllViews();

            for (int i = 0; i < procount; i++) {
                TextView headText = new TextView(this);
                headText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                headText.setText("Process " + i);
                headText.setTextSize(20);
                headText.setTextColor(parseColor("#000000"));
                linearLayout.addView(headText);

                TextView atText = new TextView(this);
                atText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                atText.setText("Arrival Time  \t"+atArray[i]);
                atText.setTextColor(parseColor("#000000"));
                atText.setTextSize(15);
                linearLayout.addView(atText);

                TextView btText = new TextView(this);
                btText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                btText.setText("Burst Time \t"+btArray[i]);
                btText.setTextColor(parseColor("#000000"));
                btText.setTextSize(15);
                linearLayout.addView(btText);

                TextView ctText = new TextView(this);
                ctText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                ctText.setText("Completion Time \t"+ctArray[i]);
                ctText.setTextSize(15);
                ctText.setTextColor(parseColor("#000000"));
                linearLayout.addView(ctText);

                TextView tatText = new TextView(this);
                tatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                tatText.setText("Turn-Around Time \t"+tatArray[i]);
                tatText.setTextSize(15);
                tatText.setTextColor(parseColor("#000000"));
                linearLayout.addView(tatText);
                avgtat+=tatArray[i];

                TextView wtText = new TextView(this);
                wtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
                wtText.setText("Waiting Time \t"+wtArray[i]);
                wtText.setTextSize(15);
                wtText.setTextColor(parseColor("#000000"));
                linearLayout.addView(wtText);
                avgwt+=wtArray[i];
            }

            avgwt/=procount;
            avgtat/=procount;

            TextView avgtatText = new TextView(this);
            avgtatText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgtatText.setText("Average Turn-Around Time \t"+avgtat);
            avgtatText.setTextSize(20);
            avgtatText.setTextColor(parseColor("#000000"));
            linearLayout.addView(avgtatText);

            TextView avgwtText = new TextView(this);
            avgwtText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)); // Pass two args; must be LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, or an integer pixel value.
            avgwtText.setText("Average Waiting Time \t"+avgwt);
            avgwtText.setTextSize(20);
            avgwtText.setTextColor(parseColor("#000000"));
            linearLayout.addView(avgwtText);
        }
        */

;





//popup window part

/*
inflator=(LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup container=(ViewGroup)inflator.inflate(R.layout.fcfs,null);

            fcfspopup=new PopupWindow(container,750,1000,true);
            fcfspopup.showAtLocation(linearLayout,Gravity.NO_GRAVITY,0,500);

            container.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    fcfspopup.dismiss();
                    return true;
                }
            });
 */

