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
    private Bitmap blue;
    private Bitmap vert;
    private Bitmap vide;

    private Bitmap blanc;
    private Bitmap rouge;
    private Bitmap jaune;
    private Bitmap mauve;

    private Bitmap cellVect_wite;
    private Bitmap cellVect_red;
    private Bitmap cellVect_yelow;
    private Bitmap cellVect_mauve;
    private Bitmap cellVect_blue;
    private Bitmap cellVect_green;
    
  // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources  mRes;    
    private Context   mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;
    int[][] PetiteMatrice;

    // ancres pour pouvoir centrer la carte du jeu
    int carteTopAnchor;                   // coordonn�es en Y du point d'ancrage de notre carte
    int carteLeftAnchor;                  // coordonn�es en X du point d'ancrage de notre carte

                   // coordonn�es en X du point d'ancrage de notre carte



      // taille de la carte
    static final int carteWidth = 8;
    static final int carteHeight = 8;
    static final int MatWidth = 11;
    static final int MatHeight = 3;
    // taille de la carte
    static final int carteTileSize = 50;
    // taille de la celelule de vecteur
    static final int vectCellSize = 30;

    // constante modelisant les differentes types de cases

    static final int CST_mauve = 6;
    static final int CST_vert = 5;
    static final int CST_blue = 4;
    static final int CST_blanc = 3;
    static final int CST_rouge = 2;
    static final int CST_jaune = 1;
    static final int CST_vide = 0;

    // tableau de reference du terrain
    int[][] ref = {
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide}
    };
    int[][] refMat = {

            {CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide},
            {CST_rouge, CST_rouge, CST_rouge, CST_vide, CST_rouge, CST_rouge, CST_rouge, CST_vide, CST_rouge, CST_rouge, CST_rouge},
            {CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide}
    };
  
 



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

             // chargement des images
        mContext = context;
        mRes = mContext.getResources();
        vide = BitmapFactory.decodeResource(mRes, R.drawable.apture1);
        blue = BitmapFactory.decodeResource(mRes, R.drawable.blue);
        rouge = BitmapFactory.decodeResource(mRes, R.drawable.rouge);
        jaune = BitmapFactory.decodeResource(mRes, R.drawable.jeune);
        vert = BitmapFactory.decodeResource(mRes, R.drawable.vert);
        blanc = BitmapFactory.decodeResource(mRes, R.drawable.blanc);
        mauve = BitmapFactory.decodeResource(mRes, R.drawable.mauve);
        cellVect_blue = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_blue);
        cellVect_red = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_rouge);
        cellVect_yelow = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_jaune);
        cellVect_green = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_vert);
        cellVect_wite = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_blanc);
        cellVect_mauve = BitmapFactory.decodeResource(mRes, R.drawable.cellvect_mauve);   
        


        // initialisation des parmametres du jeu
        initparameters();

      // creation du thread
        cv_thread   = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true); 
    }   


    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel() {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                carte[j][i] = ref[j][i];
            }
        }
     
    }

  // initialisation du jeu
    public void initparameters() {
Log.e("-FCT-", "initparameters()");


        carte = new int[carteHeight][carteWidth];

        loadlevel();
        
        carteTopAnchor = (getHeight() - carteHeight * carteTileSize) / 2;
        carteLeftAnchor = (getWidth() - carteWidth * carteTileSize) / 2;

        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }


// dessin de la carte du jeu


    
    private void paintcarte(Canvas canvas) {

        int tailleCarre=blue.getHeight();

       
       // canvas.drawBitmap(blue, 20, 20, null);
       for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                
                canvas.drawBitmap(blue,j*getWidth()/8, 80+i*getWidth()/8, null);
               
            }
        }

         for (int i = 0; i < MatHeight; i++) {
            for (int j = 0; j < MatWidth; j++) {
                        canvas.drawBitmap(rouge, j * getWidth() / 11, tailleCarre * 8 + 80 + blue.getHeight() + (i * getWidth() / 11), null);
                        
                


            }
        }
    }


       // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
      
    canvas.drawRGB(250,200,250);
    paintcarte(canvas);
  
    } 

    // callback sur le cycle de vie de la surfaceview
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
       initparameters();
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
        Log.i("-> FCT <-", "event.getX: "+ event.getX());
        Log.i("-> FCT <-", "event.getY: "+ event.getY());
        Log.i("-> FCT <-", "getWidth: "+ blue.getWidth());
       





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
