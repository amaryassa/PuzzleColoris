package amargregory.com.colorispuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.graphics.Bitmap.createBitmap;

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
    private RectF petiteMat, grandeMat, testRect ;
    private float unEspace;

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
    static final int MatWidth = 9;
    static final int MatHeight = 3;
    static final int PremiereMargeTop=40;
    // taille de la carte
    static final int carteTileSize = 50;
    // taille de la celelule de vecteur
    static final int vectCellSize = 30;

    // constante modelisant les differentes types de cases


    static final int CST_rouge = 2;
    static final int CST_noir = 1;
    static final int CST_vide = 0;



    // tableau de reference du terrain
    int[][] ref = {
            {CST_noir, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_noir, CST_vide, CST_noir, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_rouge, CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_rouge, CST_vide, CST_vide}
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
        
        Paint paint, paint1;
    private Rect rectRouge, rectBleu, rectVert;


    float a=0, b=0 , c=250,d=250;



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


    private void paintRect(Paint namePaint,Rect nameRect, Canvas canvas, int x, int y , int taille){

        nameRect= new Rect(x, y, taille, taille);
        namePaint = new Paint();
        namePaint.setColor(Color.GRAY);
        canvas.drawRect(nameRect, namePaint);


    }


// récupérer le La position sur la matric
     private int[] getIandJOfMat(float x, float y) {
    int i , j;
    String[] arrI=String.valueOf(x).split("\\.");
    String[] arrJ=String.valueOf(y).split("\\.");
    i=Integer.parseInt(arrI[0]); 
    j=Integer.parseInt(arrJ[0]);  

    int[] position = {i, j};
        return position;



    } 

// dessin de la carte du jeu


    private void paintcarte(Canvas canvas) {
         /*---------------------------------------------------------*/

         int tailleCarre=blue.getWidth();
         int espace =(getWidth()-tailleCarre*9)/3;

         float tailleCarreauSansEspace= getWidth()/8;
         float unEspace=tailleCarreauSansEspace*8/100;
         Float tailleCarreauAvecEspace=(getWidth()-(unEspace*9))/8;

         float DebutTopDeuxiemeMatrice=PremiereMargeTop+unEspace+tailleCarreauAvecEspace*carteHeight;

         float matTailleCarreauSansEspace= getWidth()/10;
         float matUnEspace=matTailleCarreauSansEspace*8/100;
         Float matTailleCarreauAvecEspace=(getWidth()-(matUnEspace*11))/10;
         float EspaceGrand=(getWidth()-(matTailleCarreauAvecEspace*9+matUnEspace*10))/2;
         float marge;
    /*---------------------------------------------------------*/
        


        Paint paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);
        Paint paintRed = new Paint();
        paintRed.setColor(Color.RED);
        Paint paintVide = new Paint();
        paintVide.setColor(Color.GREEN);
        Paint paintYellow = new Paint();
        paintYellow.setColor(Color.YELLOW);






       // canvas.drawBitmap(blue, 20, 20, null);
       for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                 grandeMat=new RectF(
                        unEspace+j*(tailleCarreauAvecEspace+unEspace),
                        PremiereMargeTop+unEspace+i*(tailleCarreauAvecEspace),
                        unEspace+j*(tailleCarreauAvecEspace+unEspace)+tailleCarreauAvecEspace,
                        PremiereMargeTop+i*(tailleCarreauAvecEspace) +tailleCarreauAvecEspace);

                switch (carte[i][j]) {

                    case CST_vide:
                    canvas.drawRect( grandeMat,paintVide);
                    break;
                    case CST_rouge:
                    canvas.drawRect( grandeMat,paintRed);
                    break;
                    case CST_noir:
                    canvas.drawRect( grandeMat,paintBlack);
                    break;
                }





               

                    
                //canvas.drawRect(new Rect(left, top,right, bottom), paint1);
            }
        }

        
         for (int i = 0; i < MatHeight; i++) {
             marge=0;
            for (int j = 0; j < MatWidth; j++) {
                if (j %3==0 && j!=0){
                    marge=marge+EspaceGrand;
                }

                petiteMat= new RectF(
                                    marge+matUnEspace+j*(matTailleCarreauAvecEspace+matUnEspace),
                                    PremiereMargeTop+DebutTopDeuxiemeMatrice+matUnEspace+i*(matTailleCarreauAvecEspace),
                                    marge+ matUnEspace+j*(matTailleCarreauAvecEspace+matUnEspace)+matTailleCarreauAvecEspace,
                                    PremiereMargeTop+DebutTopDeuxiemeMatrice+i*(matTailleCarreauAvecEspace) +matTailleCarreauAvecEspace );
                canvas.drawRect(     petiteMat,       paintRed);





                    //canvas.drawBitmap(rouge, marge + j * tailleCarre, (tailleCarre * 8) + 80 + blue.getHeight() + (i * tailleCarre), null);
            }



         }

        testRect= new RectF(a,b,c,d);
        canvas.drawRect(     testRect,       paintYellow);


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
        Log.i("-> FCT <-", "getWidth: "+ getWidth());

        //Log.i("-> FCT <-", "getWidth: "+ blue.getWidth());


        float x=event.getX();
        float y=event.getY();

        float tailleCarreauSansEspace= getWidth()/8;
        float unEspace=tailleCarreauSansEspace*8/100;
        float tailleCarreauAvecEspace=(getWidth()-(unEspace*9))/8;


        int monJ =(int) (x/tailleCarreauSansEspace);

      int monI=(int) ((y/(tailleCarreauSansEspace-unEspace))-((y/(tailleCarreauSansEspace-unEspace))/(y/40)));






 //Log.i("-> FCT <-", "[x,y]= : "+CoordonneeGrandeMatrice[1]+" "+CoordonneeGrandeMatrice[0] );

        Log.i("-> FCT <-", "i: "+monI);
        Log.i("-> FCT <-", "j: "+monJ);



        if (monI==7 && monJ==5){
                        carte[0][0]=CST_rouge;
            
        }



          switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            Log.i("-> FCT <-", "onTouchEvent: Down Amar ");


                break;
            case MotionEvent.ACTION_UP:
              Log.i("-> FCT <-", "onTouchEvent: ACTION_UP ");
            break;
            case MotionEvent.ACTION_POINTER_DOWN:
              Log.i("-> FCT <-", "onTouchEvent: ACTION_POINTER_DOWN ");
                break;
            case MotionEvent.ACTION_POINTER_UP:
              Log.i("-> FCT <-", "onTouchEvent: ACTION_POINTER_UP ");
                break;
            case MotionEvent.ACTION_MOVE:
            Log.i("-> FCT <-", "onTouchEvent: ACTION_MOVE ");

                float xx=testRect.width()/2;
                float yy=testRect.height()/2;
                a=x-xx;
                b=y-yy;
                c=x+250-xx;
                d=y+250-yy;
            break;
        }
        return true;
      //return super.onTouchEvent(event);
    }
}
