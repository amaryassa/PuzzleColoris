package amargregory.com.colorispuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by AMAR on 18/11/2017.
 */


 public class ColorisPuzzleView extends SurfaceView implements SurfaceHolder.Callback, Runnable {


  // Declaration des images

    
  // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
 

    // tableau modelisant la carte du jeu
    int[][] carte;
    
    // ancres pour pouvoir centrer la carte du jeu



    // taille de la carte
    static final int    carteWidth    = 10;
    static final int    carteHeight   = 10;
    static final int    carteTileSize = 20;

    // constante modelisant les differentes types de cases

    // tableau de reference du terrain
  
 


        // thread utiliser pour animer les zones de depot des diamants
        private     boolean in      = true;
        private     Thread  cv_thread;        
        SurfaceHolder holder;
        
        Paint paint;
        





    /*
     * @param context 
     * @param attrs 
     */
    public ColorisPuzzleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            
            
            // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed        
          holder = getHolder();
            holder.addCallback(this);    
        

      // creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true); 
    }   

       // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
    canvas.drawRGB(44,44,44);

            canvas.drawRGB(10,10,10);



        
    } 

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
      
            }

    public void surfaceCreated(SurfaceHolder arg0) {
      Log.i("-> FCT <-", "surfaceCreated");             
    }

    
    public void surfaceDestroyed(SurfaceHolder arg0) {
      Log.i("-> FCT <-", "surfaceDestroyed");             
    }    

    /**
     * run (run du thread cr��)
     * on endort le thread, on modifie le compteur d'animation, on prend la main pour dessiner et on dessine puis on lib�re le canvas
     */
    public void run() {
      Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(40);
                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                  if (c != null) {
                    holder.unlockCanvasAndPost(c);
                    }
                }
            } catch(Exception e) {
              //Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }
    
   

 
  

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      Log.i("-> FCT <-", "onKeyUp: "+ keyCode);       
      return true;   
    }
    
    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {






      if (event.getY()<50) {
        onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
      } else if (event.getY()>getHeight()-50) {
        if (event.getX()>getWidth()-50) {
            onKeyDown(KeyEvent.KEYCODE_0, null);
          } else {
            onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null);
          }
      } else if (event.getX()<50) {
        onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
      } else if (event.getX()>getWidth()-50) {
        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
      }

      return super.onTouchEvent(event);     
    }
}
