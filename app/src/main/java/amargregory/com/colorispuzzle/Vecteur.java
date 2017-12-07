package amargregory.com.colorispuzzle;


public class Vecteur {

    public float x1;
    public float y1;
    public float x2;
    public float y2;
    
    /*012
    345
    678
*/
    public int codeColor0;
    public int codeColor1;
    public int codeColor2;
    public int codeColor3;
    public int codeColor4;
    public int codeColor5;
    public int codeColor6;
    public int codeColor7;
    public int codeColor8;
  
    public boolean isHorizontal;
    public int position;


}


/*
canvas.drawRect( new RectF (MonVecteur[i].x1,MonVecteur[i].y1,MonVecteur[i].x1+matTailleCarreauAvecEspace,MonVecteur[i].y1+matTailleCarreauAvecEspace),     PaintColor.get(2));
canvas.drawRect( new RectF (MonVecteur[i].x1,MonVecteur[i].y1+(matTailleCarreauAvecEspace+matUnEspace),MonVecteur[i].x1+matTailleCarreauAvecEspace,MonVecteur[i].y1+matTailleCarreauAvecEspace+(matTailleCarreauAvecEspace+matUnEspace)),     PaintColor.get(2));
canvas.drawRect( new RectF (MonVecteur[i].x1,MonVecteur[i].y1+2*(matTailleCarreauAvecEspace+matUnEspace),MonVecteur[i].x1+matTailleCarreauAvecEspace,MonVecteur[i].y1+matTailleCarreauAvecEspace+2*(matTailleCarreauAvecEspace+matUnEspace)),     PaintColor.get(2));

            canvas.drawRect( new RectF (MonVecteur[i].x1-(matTailleCarreauAvecEspace+matUnEspace),
                            MonVecteur[i].y1+(matTailleCarreauAvecEspace+matUnEspace),
                            MonVecteur[i].x1+matTailleCarreauAvecEspace-(matTailleCarreauAvecEspace+matUnEspace),
                            MonVecteur[i].y1+matTailleCarreauAvecEspace+(matTailleCarreauAvecEspace+matUnEspace)),     PaintColor.get(3));
canvas.drawRect( new RectF (
                    MonVecteur[i].x1,
                    MonVecteur[i].y1+(matTailleCarreauAvecEspace+matUnEspace),
                    MonVecteur[i].x1+matTailleCarreauAvecEspace,
                    MonVecteur[i].y1+matTailleCarreauAvecEspace+(matTailleCarreauAvecEspace+matUnEspace)),     PaintColor.get(3));
            canvas.drawRect( new RectF (
                    MonVecteur[i].x1+(matTailleCarreauAvecEspace+matUnEspace),
                    MonVecteur[i].y1+(matTailleCarreauAvecEspace+matUnEspace),
                    MonVecteur[i].x1+matTailleCarreauAvecEspace+(matTailleCarreauAvecEspace+matUnEspace),
                    MonVecteur[i].y1+matTailleCarreauAvecEspace+(matTailleCarreauAvecEspace+matUnEspace)),     PaintColor.get(3));



        }


*/