package com.example.viewgrouptest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

public class MyPager extends ViewGroup{

	float lastX;
	int width;
	Scroller a;
	public MyPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
//		a = new Scroller(context);
//		AccelerateInterpolator a1 = new AccelerateInterpolator();
//		DecelerateInterpolator a2 = new DecelerateInterpolator(0.5f);
//		OvershootInterpolator a3 = new OvershootInterpolator(10);
		BounceInterpolator a4 = new BounceInterpolator();
		a = new Scroller(context, a4);
//		a.startScroll(0, 0, 100, 0,2000);
//获取当前scroller x的实时位置
//		a.getCurrX();
//根据当前的时间流逝，计算出相应的x，y的位置，只有调用了这个方法a.getCurrX();才会返回有效值
//返回true，表示还没有scroller还没有结束，如果false计算结束了，时间已经到期了。
//		a.computeScrollOffset();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			//触发子view的onMeasure方法被调用。
			v.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			float currentX = event.getX();
			float changeX = currentX - lastX;
			lastX = currentX;
			Log.d("wang","currentX " + currentX + " changeX " + changeX);
			//移动到那个位置。
			//scrollTo(x, y);
//			int dis = getScrollX();
//			int count = 2 * width;
			scrollBy(-((int) changeX), 0);
			//不让移动出边界
//			if(dis <= count && dis >= 0) {
//				//每次移动了多少
//				if(dis==0 && changeX > 0) {
//					break;
//				}
//				if(dis == count && changeX < 0) {
//					break;
//				}
//				scrollBy(-((int) changeX), 0);
//			}
			break;
		case MotionEvent.ACTION_UP:
			int disX = getScrollX();
			int countW = disX / width;
			//
			int value = countW * width+ width / 2;
			if(disX > value) {
				a.startScroll(disX, 0, (countW + 1)* width - disX, 0,1000);
				invalidate();
//				scrollTo((countW + 1)* width,0);
			} else {
				//让滚动值开始变化，并不会让界面滚动
				a.startScroll(disX, 0, countW * width - disX, 0,1000);
				invalidate();
//				scrollTo(countW * width,0);
//				scrollTo(a.getCurrX(),0);
			}
			break;
		default:
			break;
		}
		return true;
	}
	//computeScroll会被invalidate触发。
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if(a.computeScrollOffset()) {
			scrollTo(a.getCurrX(),0);
			invalidate();
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		width = r;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
			//给各个子view指定显示区域。
			v.layout(l+i*r, t,l+r+i*r, b);
		}
	}
}
