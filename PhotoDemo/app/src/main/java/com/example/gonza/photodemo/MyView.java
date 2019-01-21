package com.example.gonza.photodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class MyView extends View {

    private SparseArray<Face> mFaces;
    private Bitmap mBitmap;

    public MyView (Context context, AttributeSet attrs){
        super(context,attrs);

    }

    public void setContent (Bitmap bitmap, SparseArray<Face> faces){
        this.mFaces = faces;
        this.mBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap != null && mFaces != null){

            double viewWidth = canvas.getWidth();
            double viewHeight = canvas.getHeight();

            double imageWidth = mBitmap.getWidth();
            double imageHeight = mBitmap.getHeight();

            double scale = Math.min(viewWidth/imageWidth,viewHeight/imageHeight);
            Rect destBounds = new Rect(0,0,(int)(imageWidth * scale),(int)(imageHeight * scale));
            canvas.drawBitmap(mBitmap,null,destBounds,null);

            Paint paint  = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);

            for (int i = 0 ; i < mFaces.size(); i++) {
                Face face = mFaces.valueAt(i);
                int x  = (int) (face.getPosition().x * scale);
                int y  = (int) (face.getPosition().y * scale);
                int width  = (int) (face.getWidth()*scale + x);
                int height  = (int) (face.getHeight()*scale + y);
                canvas.drawRect(x,y,width,height,paint);


                for (Landmark landMark: face.getLandmarks()){
                    int cx = (int)(landMark.getPosition().x * scale);
                    int xy = (int)(landMark.getPosition().y * scale);
                    canvas.drawCircle(cx,xy,10,paint);
                }
            }
        }
    }
}
