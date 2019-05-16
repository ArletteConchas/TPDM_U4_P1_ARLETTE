package com.example.tpdm_u4_practica1_arletteconchas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    CanvasView canvas;
    Handler handler;
    Timer timer;
    List<Circulo> circulos;
    int colores[] = {Color.BLUE,Color.BLACK, Color.GRAY, Color.GREEN, Color.RED, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    Display display;
    Point tamaño;
    public int velocidad_inicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        circulos=new ArrayList<Circulo>();
        velocidad_inicial=500;
        Toast.makeText(MainActivity.this,"Presiona la pantalla para generar un nuevo circulo",Toast.LENGTH_LONG).show();
        canvas = new CanvasView(MainActivity.this);
        setContentView(canvas);
        display = getWindowManager().getDefaultDisplay();
        tamaño=new Point();
        display.getSize(tamaño);
        canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tam=(int)(Math.random()*200)+50;
                velocidad_inicial*=-1;
                int x_=(int)(Math.random()*tamaño.x);
                int y_=(int)(Math.random()*tamaño.y);
                Circulo c = new Circulo(x_,y_,tam,(int)(Math.random()*6), velocidad_inicial/tam,
                        velocidad_inicial/tam);
                circulos.add(c);
            }
        });

        CirculoInicio();

        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                canvas.invalidate();
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for(Circulo c : circulos){
                    c.x-=c.velocidad_x;
                    c.y+=c.velocidad_y;
                    if(c.x<=c.tamaño) c.velocidad_x=c.velocidad_x*-1;
                    if(c.y<=c.tamaño) c.velocidad_y=c.velocidad_y*-1;
                    if(c.x>=tamaño.x-(c.tamaño/2)) c.velocidad_x=c.velocidad_x*-1;
                    if(c.y>=tamaño.y-(c.tamaño/2)) c.velocidad_y=c.velocidad_y*-1;
                }
                handler.sendEmptyMessage(0);
            }
        },0,30);
    }

    private class CanvasView extends View {
        private Paint p;
        public CanvasView(Context context){
            super(context);
            setFocusable(true);
            p=new Paint();
        }
        public void onDraw(Canvas c){
            p.setStyle(Paint.Style.FILL);
            p.setAntiAlias(true);
            p.setTextSize(30f);
            for(Circulo cr : circulos) {
                p.setColor(colores[cr.color]);
                c.drawCircle(cr.x, cr.y, cr.tamaño, p);
            }
        }
    }

    private void CirculoInicio(){
        int tam=(int)(Math.random()*200)+50;
        velocidad_inicial*=-1;
        int x_=(int)(Math.random()*tamaño.x);
        int y_=(int)(Math.random()*tamaño.y);
        Circulo c = new Circulo(x_,y_,tam,(int)(Math.random()*8), velocidad_inicial/tam,
                velocidad_inicial/tam);
        circulos.add(c);
    }
}
