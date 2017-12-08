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

import java.util.ArrayList;
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
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
            {CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide, CST_vide},
         
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
        float leftVect=0 ;
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


//initia 
 for (int i = 0; i < 3; i++) {
            float marg=13;
        margeVect=35+(70*3);
            MonVecteur[i] = new Vecteur();

            MonVecteur[i].x1 =  marg+leftVect+margeVect*i;
            MonVecteur[i].y1 = topVect;

            MonVecteur[i].x1_1 =  MonVecteur[i].x1 ;
            MonVecteur[i].y1_1 = MonVecteur[i].y1;

            MonVecteur[i].x2 = MonVecteur[i].x1 + 71*3;
            MonVecteur[i].y2 = MonVecteur[i].y1 + 3 * 71;

             MonVecteur[i].codeColor0=10;
             MonVecteur[i].codeColor3=10;
             MonVecteur[i].codeColor5=10;
             MonVecteur[i].codeColor6=10;
             MonVecteur[i].codeColor2=10;
             MonVecteur[i].codeColor8=10;
            MonVecteur[i].isHorizontal = false;
            MonVecteur[i].position = 1;
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
/*/************************************************************************************/
  private void rotate (Vecteur mvect){
        if(mvect.position==1){//top 
      
            mvect.codeColor5=mvect.codeColor1;
            mvect.codeColor3=mvect.codeColor7;
            mvect.codeColor1=10;
            mvect.codeColor7=10;
            mvect.position=2;

        }
        else if (mvect.position==2) {//right 
            mvect.codeColor7=mvect.codeColor5;
            mvect.codeColor1=mvect.codeColor3;
            mvect.codeColor5=10;
            mvect.codeColor3=10;
            mvect.position=3;
        }
        else if (mvect.position==3) { // bottom
            mvect.codeColor3=mvect.codeColor7;
            mvect.codeColor5=mvect.codeColor1;
            mvect.codeColor7=10;
           mvect.codeColor1=10;
            mvect.position=4;
        }
        else if (mvect.position==4) { //left
              mvect.codeColor1=mvect.codeColor3;
            mvect.codeColor7=mvect.codeColor5;
            mvect.codeColor3=10;
            mvect.codeColor5=10;
            mvect.position=1;
        }

      Log.i("-> FCT <-", "position "+( mvect.position));
}

/*/************************************************************************************/

    //--------------------------------------------------------
 private void getrandomVector(Vecteur vect) {
        Random generateur = new Random();

        //Min + (Math.random() * (Max - Min))
        ////rand.nextInt(max - min + 1) + min

        int randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor1 = randomint;


        randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor4 = randomint;


        randomint = 1 + generateur.nextInt(5 - 1);
        vect.codeColor7 = randomint;



        vect.codeColor0=10;
        vect.codeColor3=10;
        vect.codeColor5=10;
        vect.codeColor6=10;
        vect.codeColor2=10;
        vect.codeColor8=10;
        vect.isHorizontal=false;
        vect.position=1;
    }


    /********************************************************************/
    private int getVectSelect(int x, int y) {
        int VecteurSelectionnee = -1;

        for (int i = 0; i < 3; i++) {
            if (x > MonVecteur[i].x1_1 && x < MonVecteur[i].x2 && y > MonVecteur[i].y1_1 && y < MonVecteur[i].y2) {
                VecteurSelectionnee = i;
            }
        }

        return VecteurSelectionnee;

    }
    /********************************************************************/

/*---------------------------------------------------------------------------*/
void SupMoreThreeSameColor() {
    ArrayList<Integer> mesI = new ArrayList<Integer>();
    ArrayList<Integer> mesJ = new ArrayList<Integer>();
        int i=0;
        int j=0;
        int nbOfsimilarH=0;
        int nbOfsimilarV=0;
        int nbOfsimilarT=0;

        while (i < carteHeight) {
            j=0;

            while (j < carteWidth)
            {
                int k=j+1;// le J suivant
                int l=i+1;// le i suivant
                if(carte[i][j] != CST_vide) {
                        while ( k < carteWidth && carte[i][j] == carte[i][k]){
                            k++;
                        }
                    while (  l < carteHeight && carte[i][j] == carte[l][j] ){
                        l++;
                    }
                    nbOfsimilarH=k-j;
                    if(nbOfsimilarH >=3){
                        for(int m=j;m <k;m++)
                        {
                            nbOfsimilarT++;
                            mesI.add(i);
                            mesJ.add(m);
                        }
                    }
                    nbOfsimilarV = l-i;
                    if(nbOfsimilarV >=3){
                        Log.i("SimCarte V",Integer.toString(nbOfsimilarV));
                        for(int m=i;m <l;m++)
                        {
                            nbOfsimilarT++;
                            mesI.add(m);
                            mesJ.add(j);
                        }
                    }

                    nbOfsimilarH=0;
                    nbOfsimilarV=0;
                }
                j=k;
            }

            i++;
        }
        
            for (int ij = 0; ij < mesI.size(); ij++) { 
                    carte[mesI.get(ij)][mesJ.get(ij)]=CST_vide;

}
    }

/*---------------------------------------------------------------------------*/

    
    /********************************************************************/
        private void itsFree(Vecteur mVect, int i, int j){
            int maPosition=mVect.position;
            //vertical
            if (maPosition==1 || maPosition==3){
                if (i>1 && i<8){
                    if(carte[i][j]==CST_vide && carte[i-1][j]==CST_vide && carte[i-2][j]==CST_vide){
                        carte[i][j]=mVect.codeColor7;
                        carte[i-1][j]=mVect.codeColor4;
                        carte[i-2][j]=mVect.codeColor1;
                        //une fois insérer, on change les couleurs du vecteur qu'on a remets en place à la fin de la fonction
                        getrandomVector(mVect);
                    }
                }
            //horizontal
            }else if (maPosition==2 || maPosition==4){
                if (i>0 && i<9 &&  j>0 && j<7   ){
                    if(carte[i-1][j-1]==CST_vide && carte[i-1][j]==CST_vide && carte[i-1][j+1]==CST_vide){
                    carte[i-1][j-1]=mVect.codeColor3;
                        carte[i-1][j]=mVect.codeColor4;
                        carte[i-1][j+1]=mVect.codeColor5;
                        //une fois insérer, on change les couleurs du vecteur qu'on a remets en place à la fin de la fonction
                        getrandomVector(mVect);
                    }
                }

              
            }

            
            mVect.x1=mVect.x1_1;
            mVect.y1=mVect.y1_1;
    }
    /********************************************************************/



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
                    case CST_vide_No_Contour:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_vide_No_Contour));
                    break;case CST_vert:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_vert));
                    break;case CST_magenta:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_magenta));
                    break;case CST_yellow:
                    canvas.drawRect( grandeMat,PaintColor.get(CST_yellow));
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
            int [] couleurDelaCaseVecteur =new int [9];
            couleurDelaCaseVecteur[0]=MonVecteur[k].codeColor0;
            couleurDelaCaseVecteur[1]=MonVecteur[k].codeColor1;
            couleurDelaCaseVecteur[2]=MonVecteur[k].codeColor2;
            couleurDelaCaseVecteur[3]=MonVecteur[k].codeColor3;
            couleurDelaCaseVecteur[4]=MonVecteur[k].codeColor4;
            couleurDelaCaseVecteur[5]=MonVecteur[k].codeColor5;
            couleurDelaCaseVecteur[6]=MonVecteur[k].codeColor6;
            couleurDelaCaseVecteur[7]=MonVecteur[k].codeColor7;
            couleurDelaCaseVecteur[8]=MonVecteur[k].codeColor8;
            int NumberCouleur=0;


        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    canvas.drawRect(new RectF(MonVecteur[k].x1 + (j * (matTailleCarreauAvecEspace + matUnEspace)),
                                    MonVecteur[k].y1 + (i * (matTailleCarreauAvecEspace + matUnEspace)),
                                    MonVecteur[k].x1 + matTailleCarreauAvecEspace + (j * (matTailleCarreauAvecEspace + matUnEspace)),
                                    MonVecteur[k].y1 + matTailleCarreauAvecEspace + (i * (matTailleCarreauAvecEspace + matUnEspace))),
                             PaintColor.get(couleurDelaCaseVecteur[NumberCouleur]));
                    NumberCouleur++;


                }
        }
        }

        
        
    }

       // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
      
    canvas.drawRGB(250,200,250);
    paintcarte(canvas);
       paintvect(canvas);
       SupMoreThreeSameColor();
  
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
    
int clickeDownY = 0;
int clickeDownX= 0;
int prevX= 0;
int prevY= 0;
int clickeUpY= 0;
int clickeUpX= 0;
int differenceXUpDown= 0;
int differenceYUpDown= 0;
    int selectedVect=-1;





    
    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
       /* Log.i("-> FCT <-", "event.getX: "+ event.getX());
        Log.i("-> FCT <-", "event.getY: "+ event.getY());
        Log.i("-> FCT <-", "getWidth: "+ getWidth());*/


        //Log.i("-> FCT <-", "getWidth: "+ blue.getWidth())
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
       //Log.i("-> FCT <-", "EspaceGrand: "+ EspaceGrand);
        //Log.i("-> FCT <-", "premier espace: "+( matUnEspace+matTailleCarreauAvecEspace));

        int monJ =(int) (x/tailleCarreauSansEspace);

      int monI=(int) ((y/(tailleCarreauSansEspace-unEspace))-((y/(tailleCarreauSansEspace-unEspace))/(y/40)));



        Log.i("-> FCT <-", "i: "+monI);
        Log.i("-> FCT <-", "j: "+monJ);

            float x11=(71*3)/2;
            float y11=(71*3);

          switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                clickeDownY = (int) event.getY();
                clickeDownX = (int) event.getX();
                 selectedVect= getVectSelect(clickeDownX,clickeDownY);
                break;
            case MotionEvent.ACTION_UP:
                    Log.i("-> FCT <-", "onTouchEvent: ACTION_UP ");
                    clickeUpY = (int) event.getY();
                    clickeUpX = (int) event.getX();
                    Log.i("-> FCT <-", "selectedVect: "+selectedVect);

                differenceXUpDown = clickeUpX- clickeDownX;
                differenceYUpDown = clickeUpY - clickeDownY;

                if (differenceXUpDown==0 && differenceYUpDown==0){
                   if (selectedVect != -1) rotate(MonVecteur[selectedVect]);
                }

                if (selectedVect != -1) itsFree (MonVecteur[selectedVect], monI, monJ);

                //MonVecteur[selectedVect].x1=MonVecteur[selectedVect].x1_1;
                //MonVecteur[selectedVect].y1=MonVecteur[selectedVect].y1_1;


                break;
            case MotionEvent.ACTION_POINTER_DOWN:
              Log.i("-> FCT <-", "onTouchEvent: ACTION_POINTER_DOWN ");
                break;
            case MotionEvent.ACTION_POINTER_UP:
              Log.i("-> FCT <-", "onTouchEvent: ACTION_POINTER_UP ");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("-> FCT <-", "onTouchEvent: ACTION_MOVE ");
                if (selectedVect != -1){ MonVecteur[selectedVect].x1=x-x11;
                MonVecteur[selectedVect].y1=y-y11;}
               

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
