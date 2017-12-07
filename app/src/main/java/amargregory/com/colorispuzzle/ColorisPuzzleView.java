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

   private static Map<Integer, Paint> PaintColor= new HashMap<Integer, Paint>();
   private static Map<Integer, RectF> PaintRectVect= new HashMap<Integer, RectF>();


  // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources  mRes;    
    private Context   mContext;
    private RectF petiteMat, grandeMat, testRect, rectRouge, rectNoir, rectVert, rectMagenta,rectYellow,rectVide,rectVideNoContour ;
    private float unEspace;



    // tableau modelisant la carte du jeu
    int[][] carte;
  

     Vecteur[] MonVecteur = new Vecteur[3];

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





    static final int CST_vide = 0;
    static final int CST_noir = 1;
    static final int CST_rouge = 2;
    static final int CST_magenta = 3;
    static final int CST_yellow = 4;
    static final int CST_vert = 5;
    static final int CST_vide_No_Contour = 10;

    float margeVect=0;



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





        // thread utiliser pour animer les zones de depot des diamants
        private     boolean in      = true;
        private     Thread  cv_thread;
        SurfaceHolder holder;


    float a=0, b=0 , c=250,d=250;

        float tailleCarreauSansEspace1= getWidth()/8;
        float unEspace1=tailleCarreauSansEspace1*8/100;
        Float tailleCarreauAvecEspace1=(getWidth()-(unEspace1*9))/8;

        float DebutTopDeuxiemeMatrice1=PremiereMargeTop+unEspace1+tailleCarreauAvecEspace1*carteHeight;

        float matTailleCarreauSansEspace1= getWidth()/10;
        float matUnEspace1=matTailleCarreauSansEspace1*8/100;
        Float matTailleCarreauAvecEspace1=(getWidth()-(matUnEspace1*11))/10;
        float EspaceGrand1=(getWidth()-(matTailleCarreauAvecEspace1*9+matUnEspace1*10))/2;


            //canvas.drawRect(new Rect(left, top,right, bottom), paint1);
        float leftVect=matUnEspace1 ;
        float topVect= PremiereMargeTop+DebutTopDeuxiemeMatrice1+matUnEspace1+700;
        float rightVect=matUnEspace1+ matTailleCarreauAvecEspace1;
        float bottomVect=PremiereMargeTop+DebutTopDeuxiemeMatrice1 +matTailleCarreauAvecEspace1 ;
      
      
       

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



        rectNoir= new RectF();
        rectRouge= new RectF();
        rectVert= new RectF();
        rectMagenta= new RectF();
        rectYellow= new RectF();
        rectVide= new RectF();
        rectVideNoContour= new RectF();

        Paint paintVectNoir = new Paint();
        paintVectNoir.setColor(Color.BLACK);

        Paint paintVectRouge = new Paint();
        paintVectRouge.setColor(Color.RED);

        Paint paintVectVert = new Paint();
        paintVectVert.setColor(Color.GREEN);

        Paint paintVectMAGENTA = new Paint();
        paintVectMAGENTA.setColor(Color.MAGENTA);

        Paint paintVectYellow = new Paint();
        paintVectYellow.setColor(Color.YELLOW);

        Paint paintVectVide = new Paint();
        paintVectVide.setColor(Color.TRANSPARENT);
        paintVectVide.setStyle(Paint.Style.STROKE);
        paintVectVide.setColor(Color.BLACK);

        Paint paintVectVideNoContour = new Paint();
        paintVectVideNoContour.setColor(Color.TRANSPARENT);


        PaintColor.put(CST_vide,paintVectVide);
        PaintColor.put(CST_noir,paintVectNoir);
        PaintColor.put(CST_rouge,paintVectRouge);
        PaintColor.put(CST_magenta,paintVectMAGENTA);
        PaintColor.put(CST_yellow,paintVectYellow);
        PaintColor.put(CST_vert,paintVectVert);
        PaintColor.put(CST_vide_No_Contour,paintVectVideNoContour);


        PaintRectVect.put(CST_vide,rectVide);
        PaintRectVect.put(CST_noir,rectNoir);
        PaintRectVect.put(CST_rouge,rectRouge);
        PaintRectVect.put(CST_magenta,rectMagenta);
        PaintRectVect.put(CST_yellow,rectYellow);
        PaintRectVect.put(CST_vert,rectVert);
        PaintRectVect.put(CST_vide_No_Contour,rectVideNoContour);


 for (int i = 0; i < 3; i++) {
            float marg=71;

        if (i!=0)margeVect=margeVect+35+70;
            MonVecteur[i] = new Vecteur();
            
            MonVecteur[i].x1 =  marg+leftVect+142*i;
            MonVecteur[i].y1 = topVect;
            MonVecteur[i].x2 = MonVecteur[i].x1 + 71;
            MonVecteur[i].y2 = MonVecteur[i].y1 + 3 * 57;


            MonVecteur[i].isHorizontal = false;
        }






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
         for (int k = 0; k < 3; k++) {
            getrandomVector(MonVecteur[k]);
        }

        carteTopAnchor = (getHeight() - carteHeight * carteTileSize) / 2;
        carteLeftAnchor = (getWidth() - carteWidth * carteTileSize) / 2;

        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
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


    //--------------------------------------------------------
 private void getrandomVector(Vecteur vect) {
        Random generateur = new Random();

        //Min + (Math.random() * (Max - Min))
        ////rand.nextInt(max - min + 1) + min

        int randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor1 = randomint;


        randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor2 = randomint;


        randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor3 = randomint;



        vect.isHorizontal=false;
    }



    //--------------------------------------------------------

// dessin de la carte du jeu

    private void paintcarte(Canvas canvas) {
         /*---------------------------------------------------------*/

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
                    canvas.drawRect( grandeMat,PaintColor.get(CST_vide));
                    break;
                    case CST_rouge:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_rouge));
                    break;
                    case CST_noir:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_noir));
                    break;
                }


                //canvas.drawRect(new Rect(left, top,right, bottom), paint1);
            }
        }







    }
  private void paintvect(Canvas canvas) {
     /*---------------------------------------------------------*/

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

int grego=1;
        for (int k = 0; k < 3; k++) {
            int [] couleurDelaCaseVecteur =new int [3];
            couleurDelaCaseVecteur[0]=MonVecteur[k].codeColor1;
            couleurDelaCaseVecteur[1]=MonVecteur[k].codeColor2;
            couleurDelaCaseVecteur[2]=MonVecteur[k].codeColor3;
            int NumberCouleur=0;

        int number=0;
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {


  
   

        canvas.drawRect(new RectF(MonVecteur[k].x1 + (j * (matTailleCarreauAvecEspace + matUnEspace)),
                        MonVecteur[k].y1 + (i * (matTailleCarreauAvecEspace + matUnEspace)),
                        MonVecteur[k].x1 + matTailleCarreauAvecEspace + (j * (matTailleCarreauAvecEspace + matUnEspace)),
                        MonVecteur[k].y1 + matTailleCarreauAvecEspace + (i * (matTailleCarreauAvecEspace + matUnEspace))),
                PaintColor.get(couleurDelaCaseVecteur[NumberCouleur]));
        NumberCouleur++;
    





                        //canvas.drawBitmap(rouge, marge + j * tailleCarre, (tailleCarre * 8) + 80 + blue.getHeight() + (i * tailleCarre), null);

                    number++;

                }}
               }
        
        
    }

       // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
      
    canvas.drawRGB(250,200,250);
    paintcarte(canvas);
       paintvect(canvas);
  
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
        float DebutTopDeuxiemeMatrice=PremiereMargeTop+unEspace+tailleCarreauAvecEspace*carteHeight;

        float matTailleCarreauSansEspace= getWidth()/10;
        float matUnEspace=matTailleCarreauSansEspace*8/100;
        Float matTailleCarreauAvecEspace=(getWidth()-(matUnEspace*11))/10;
        float EspaceGrand=(getWidth()-(matTailleCarreauAvecEspace*9+matUnEspace*10))/2;
        float marge;
        Log.i("-> FCT <-", "EspaceGrand: "+ EspaceGrand);
        Log.i("-> FCT <-", "premier espace: "+( matUnEspace+matTailleCarreauAvecEspace));

        int monJ =(int) (x/tailleCarreauSansEspace);

      int monI=(int) ((y/(tailleCarreauSansEspace-unEspace))-((y/(tailleCarreauSansEspace-unEspace))/(y/40)));






 //Log.i("-> FCT <-", "[x,y]= : "+CoordonneeGrandeMatrice[1]+" "+CoordonneeGrandeMatrice[0] );

        Log.i("-> FCT <-", "i: "+monI);
        Log.i("-> FCT <-", "j: "+monJ);



        if (monI==7 && monJ==5){
                        carte[0][0]=CST_rouge;
            
        }

        //float xx=testRect.width()/2;
        //float yy=testRect.height()/2;

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
               /* a=x-xx;
               b=y-yy;
                c=x+400-xx;
               d=y+400-yy;*/
            break;
        }
        return true;
      //return super.onTouchEvent(event);
    }
}
