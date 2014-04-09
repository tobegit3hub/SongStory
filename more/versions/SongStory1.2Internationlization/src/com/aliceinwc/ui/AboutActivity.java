package com.aliceinwc.ui;

import com.aliceinwc.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class AboutActivity extends Activity {
	/** Called when the activity is first created. */
	
	private Activity activity;

	private int SCREEN_W;
	private int SCREEN_H;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		
		setContentView(new MyView(this));
		
		activity = this;

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		
		StoryDetailActivity.setFullScreenOrNot(activity);
		
	}

	class MyView extends View {

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Paint mPaint;
		private Path mPath;
		private float mX, mY;
		private float mDownX, mDownY;
		private static final float TOUCH_TOLERANCE = 6;

		public MyView(Context context) {

			super(context);
			setFocusable(true);
			setScreenWH();

			setBackGround();

			// Bitmap bm = createBitmapFromARGB(0x99708090, SCREEN_W, SCREEN_H);
			Bitmap bm = createBitmapFromSRC();

			// ��bm��Ϊcover
			// setCoverBitmap(bm);
			setCoverBitmap(scaleBitmapFillScreen(bm));

		}

		// ��ͼƬ��С
		private void setScreenWH() {

			// get screen info

			DisplayMetrics dm = new DisplayMetrics();

			dm = this.getResources().getDisplayMetrics();

			// get screen width

			int screenWidth = dm.widthPixels;

			// get screen height

			int screenHeight = dm.heightPixels;

			SCREEN_W = screenWidth;

			SCREEN_H = screenHeight;

		}

		// ���ñ���ͼ
		private void setBackGround() {
			setBackgroundResource(R.drawable.about_bg);
		}

		// ����ͼƬ��ɫ��͸���ȣ�
		private Bitmap createBitmapFromARGB(int colorARGB, int width, int height) {

			int[] argb = new int[width * height];

			for (int i = 0; i < argb.length; i++) {
				argb[i] = colorARGB;
			}

			// �ú�������ɫ����������λͼ��
			// createBitmap(int[] colors, int width,int height,Bitmap.Config
			// config)
			// ע��:��ɫ����ĳ���>=width*height
			// ���width��height������λͼ��Ȼ����ָ������ɫ����colors�������Ҵ�������һ�������ɫ��
			// config��һ��ö�٣�����������ָ��λͼ����������
			return Bitmap.createBitmap(argb, width, height, Config.ARGB_8888);

		}

		// ��coverΪͼʱ���ڴ�����ͼƬ����δ�á�
		private Bitmap createBitmapFromSRC() {

			// BitmapFactory.decodeResource�����ɼ��ع���Ŀ¼�е�drawable�ļ����µ�ͼƬ��Դ
			// ��һ�������ǰ�Ҫ���ص�λͼ��Դ�ļ��Ķ���һ��д�� getResources������ok�ˣ���
			// �ڶ�����������Ҫ���ص�λͼ��Դ��Id
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.about_bg_cover);

		}

		// �����ڴ�����͸����alpha��0��255����δ�á�
		private Bitmap setBitmapAlpha(Bitmap bm, int alpha) {

			int[] argb = new int[bm.getWidth() * bm.getHeight()];

			bm.getPixels(argb, 0, bm.getWidth(), 0, 0, bm.getWidth(),
					bm.getHeight());

			for (int i = 0; i < argb.length; i++) {
				argb[i] = ((alpha << 24) | (argb[i] & 0x00FFFFFF));
			}

			return Bitmap.createBitmap(argb, bm.getWidth(), bm.getHeight(),
					Config.ARGB_8888);

		}

		// ��bitmapС����Ļ���ٴδ���������λͼ����δ�á�
		private Bitmap scaleBitmapFillScreen(Bitmap bm) {

			// createScaledBitmap�����������ŵ�λͼ��
			// bm����ԭʼλͼ SCREEN_W����Ŀ���� SCREEN_H����Ŀ��߶� true�����Ƿ����
			return Bitmap.createScaledBitmap(bm, SCREEN_W, SCREEN_H, true);

		}

		private void setCoverBitmap(Bitmap bm) {

			// ���û���

			mPaint = new Paint();

			mPaint.setAlpha(0); // ���û���͸���ȣ�ȡֵ��ΧΪ0~255��0Ϊ͸��

			// setXfermode��������ͼƬ�ཻʱ��ģʽ
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); // PorterDuff.Mode.DST_Iȡ������ƽ�������ʾ�ϲ㡣

			mPaint.setAntiAlias(false); // ��ֹ��Ե�ľ�ݣ�������Ľϴ���Դ������ͼ���ٶȻ����

			mPaint.setDither(true); // ������

			mPaint.setStyle(Paint.Style.STROKE); // �������� STROKE���� FILL ʵ��

			// mPaint.setStrokeJoin(Paint.Join.ROUND); //���û���ʱ��ͼ�εĽ�Ϸ�ʽ����ƽ��Ч���
			mPaint.setStrokeJoin(Paint.Join.MITER);

			mPaint.setStrokeCap(Paint.Cap.ROUND); // �ñ�ˢ��ͼ����ʽ��ROUND/SQUARE/BUTT

			mPaint.setStrokeWidth(41); // ���ñ�ˢ�Ĵ�ϸ��

			// set path
			mPath = new Path();

			/* ���������� */
			mBitmap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Config.ARGB_8888);

			/* ����Canvas */
			mCanvas = new Canvas();

			/* ����Ϊ�������ݻ�����mBitmap�� */
			mCanvas.setBitmap(mBitmap);

			/* ��bm���Ƶ�mBitmap�� */
			mCanvas.drawBitmap(bm, 0, 0, null);

		}

		@Override
		protected void onDraw(Canvas canvas) {

			/* ��mBitmap���Ƶ���Ļ�� */
			canvas.drawBitmap(mBitmap, 0, 0, null);

			mCanvas.drawPath(mPath, mPaint);

			super.onDraw(canvas);

		}

		private void touch_start(float x, float y) {

			mPath.reset();

			mPath.moveTo(x, y);

			mX = x;

			mY = y;

		}

		// ���켣
		private void touch_move(float x, float y) {

			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {

			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			mPath.reset();

		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				
				mDownX = x;
				mDownY = y;

				touch_start(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_MOVE:

				touch_move(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_UP:
				
				if(mDownX<SCREEN_W/6 && mDownY<SCREEN_H/8 && x<SCREEN_W/6 && y<SCREEN_H/8){
					activity.finish();
				}

				touch_up();
				invalidate();
				break;

			}

			return true;

		}

	}

}
