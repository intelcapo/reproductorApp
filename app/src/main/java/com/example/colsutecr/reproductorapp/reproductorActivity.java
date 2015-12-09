package com.example.colsutecr.reproductorapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View.*;
import android.widget.TextView;
import android.widget.Toast;
import android.media.*;

public class reproductorActivity extends AppCompatActivity implements OnClickListener,AdapterView.OnItemSelectedListener{

    Spinner lista_reproduccion;
    SeekBar seek_volumen,seek_progreso;
    ImageView img_banda,btn_play,btn_volume,btn_pause,btn_stop;
    String[] canciones;
    MediaPlayer reproductor;
    TextView lbl_tiempo;
    private int iArtistas[];
    private int arrayCanciones[];
    int pos;
    int posMusica = 0;
    String segun="",min="";
    AudioManager mngAudio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        lista_reproduccion = (Spinner) findViewById(R.id.spn_lista_canciones);

        /*Manipulacion de volumen de la app*/
        //<--Inicio-->
        //Audio manager es una clase que permite el control del servicio de stream de audio de android
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mngAudio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //
        seek_volumen = (SeekBar) findViewById(R.id.sek_volumen);
        seek_volumen.setMax(mngAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));//Colocamos el valor máximo del stream para música
        seek_volumen.setProgress(mngAudio.getStreamVolume(AudioManager.STREAM_MUSIC));


        seek_volumen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //Implementamos los metodos del escuchador cuando la barra de progreso cambie
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progreso = i;
                mngAudio.setStreamVolume(AudioManager.STREAM_MUSIC,progreso,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //<--Fin manipulacion sonido-->

        canciones = new String[]{
                "Seleccione...",
                "One - Metallica",
                "Wiskey in the jar - Metallica",
                "Du Hast - Ramstein",
                "Welcome to the jungle - Guns"
        };
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,canciones);
        lista_reproduccion.setAdapter(adaptador);
        lista_reproduccion.setOnItemSelectedListener(this);
        reproductor = new MediaPlayer();
        img_banda = (ImageView) findViewById(R.id.img_grupo);
        iArtistas=new int [4];
        iArtistas[0]=R.drawable.rock_band;
        iArtistas[1]=R.drawable.metallica;
        iArtistas[2]=R.drawable.rammstein;
        iArtistas[3]=R.drawable.gunsandroses;
        btn_play = (ImageView) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        btn_pause = (ImageView) findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);
        btn_stop = (ImageView) findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(this);
        lbl_tiempo = (TextView) findViewById(R.id.lbl_tiempo);

        seek_progreso= (SeekBar) findViewById(R.id.seek_progreso);


    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_play:
                if(reproductor != null && reproductor.isPlaying() == false){
                    Toast.makeText(this,"pos: "+posMusica,Toast.LENGTH_SHORT).show();
                    reproductor.seekTo(posMusica);
                    reproductor.start();
                    seek_progreso.setMax(reproductor.getDuration());
                    seek_progreso.setProgress(reproductor.getCurrentPosition());

                }
                break;
            case R.id.btn_pause:

                if(reproductor != null && reproductor.isPlaying()){
                    //Se debe
                    posMusica = reproductor.getCurrentPosition();
                    reproductor.pause();
                }else{
                    reproductor.seekTo(posMusica);
                    reproductor.start();
                }

                break;
            case R.id.btn_stop:
                reproductor.stop();
                break;
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        imprimeCancion(pos);
        cambiaImagen(pos);
        posMusica = 0;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void imprimeCancion(int posicion){
        Toast.makeText(this, "Se va reproducir la canción: "+posicion, Toast.LENGTH_SHORT).show();
    }

    public void cambiaImagen(int posicion){
       int resource=0;
        int tiempo = 0;

        if(reproductor != null)
            reproductor.release();//Borra si el reproductor tiene algo en memoria
        switch(posicion) {
            case 0:
                resource = iArtistas[0];

                break;
            case 1:
                resource = iArtistas[1];
                reproductor = MediaPlayer.create(this,R.raw.metallica_one);
                tiempo = reproductor.getDuration();
                calculaSegundos(tiempo);
                break;
            case 2:
                resource = iArtistas[1];
                reproductor = MediaPlayer.create(this,R.raw.metallica_wiskey_in_the_jar);
                tiempo = reproductor.getDuration();
                calculaSegundos(tiempo);
                break;
            case 3:
                resource = iArtistas[2];
                reproductor = MediaPlayer.create(this,R.raw.rammstein_duhast);
                tiempo = reproductor.getDuration();
                calculaSegundos(tiempo);
                break;
            case 4:
                resource = iArtistas[3];
                reproductor = MediaPlayer.create(this,R.raw.guns_welcome);
                tiempo = reproductor.getDuration();
                calculaSegundos(tiempo);
                break;
            default:
                resource = iArtistas[0];
                break;

        }
        img_banda.setImageResource(resource);


    }

    public void onPause(){
        super.onPause();
        if(reproductor!=null){
            reproductor.release();
        }

    }

    public void calculaSegundos(int tiempo){
        int segundos = tiempo / 1000;
        int minutos = segundos / 60;
        int segMostrar = segundos %60;


        if(minutos<10){
            min = "0"+minutos;
        }
        if(segMostrar < 10){
            segun="0"+segMostrar;
        }
        lbl_tiempo.setText(min+":"+segun);
        //Toast.makeText(this," "+minutos+":"+segMostrar,Toast.LENGTH_SHORT).show();
    };
}
