package life;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class StepView extends View {
    private Paint mPaintWalk; // 行走蓝色画笔
    private Paint mStrokePaintWalk;

    private Paint mPaintFall; //摔倒红色画笔
    private Paint mStrokePaintFall;

    private Path mArrowPath; // 箭头路径

    private int cR = 10; // 圆点半径
    private int arrowR = 20; // 箭头半径

    private float mCurX = 200;
    private float mCurY = 200;
    private int mOrient;
    private Bitmap mBitmap;
    private List<PointF> mPointList = new ArrayList<>();
    private List mFallList = new ArrayList<>();

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化行走蓝色画笔
        mPaintWalk = new Paint();
        mPaintWalk.setColor(Color.BLUE);
        mPaintWalk.setAntiAlias(true);
        mPaintWalk.setStyle(Paint.Style.FILL);
        mStrokePaintWalk = new Paint(mPaintWalk);
        mStrokePaintWalk.setStyle(Paint.Style.STROKE);
        mStrokePaintWalk.setStrokeWidth(5);

        // 初始化摔倒红色画笔
        mPaintFall = new Paint();
        mPaintFall.setColor(Color.RED);
        mPaintFall.setAntiAlias(true);
        mPaintFall.setStyle(Paint.Style.FILL);
        mStrokePaintFall = new Paint(mPaintFall);
        mStrokePaintFall.setStyle(Paint.Style.STROKE);
        mStrokePaintFall.setStrokeWidth(5);


        // 初始化箭头路径
        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR, -arrowR, arrowR, arrowR), 0, -180);
        mArrowPath.lineTo(0, -3 * arrowR);
        mArrowPath.close();
        // 页面背景，png最好
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
//        canvas.drawBitmap(mBitmap, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), new Rect(0, 0, getWidth(), getHeight()), null); // 将mBitmap绘到canLock
        for (PointF p : mPointList) {
            int fallIndex = mPointList.indexOf(p);
            if (Integer.parseInt(mFallList.get(fallIndex).toString()) == 0) { //这里判断这个点是否跌倒来选择不同的画笔
                canvas.drawCircle(p.x, p.y, cR, mPaintWalk);
            } else {
                canvas.drawCircle(p.x, p.y, cR, mPaintFall);
            }
        }
        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrient); // 转动画布
        canvas.drawPath(mArrowPath, mPaintWalk);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaintWalk);
        canvas.restore(); // 恢复画布
    }

    /**
     * 当屏幕被触摸时调用
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurX = event.getX();
        mCurY = event.getY();
        invalidate();
        return true;
    }

    /**
     * 自动增加点
     */
    public void autoAddPoint(float stepLen, int CURRENT_FALL) {
        mCurX += (float) (stepLen * Math.sin(Math.toRadians(mOrient)));
        mCurY += -(float) (stepLen * Math.cos(Math.toRadians(mOrient)));
        mPointList.add(new PointF(mCurX, mCurY));
        mFallList.add(CURRENT_FALL);
        invalidate();
    }

    public void autoDrawArrow(int orient) {
        mOrient = orient;
        invalidate();
    }
}