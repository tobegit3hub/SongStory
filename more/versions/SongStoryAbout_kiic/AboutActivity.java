package cn;

import cn.Rubbler.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class AboutActivity extends Activity {
	/** Called when the activity is first created. */

	private int SCREEN_W;
	private int SCREEN_H;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));

	}

	class MyView extends View {

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Paint mPaint;
		private Path mPath;
		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		public MyView(Context context) {

			super(context);
			setFocusable(true);
			setScreenWH();

			setBackGround();

			// Bitmap bm = createBitmapFromARGB(0x99708090, SCREEN_W, SCREEN_H);
			Bitmap bm = createBitmapFromSRC();

			// 把bm设为cover
			// setCoverBitmap(bm);
			setCoverBitmap(scaleBitmapFillScreen(bm));

		}

		// 设图片大小
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

		// 设置背景图
		private void setBackGround() {
			setBackgroundResource(R.drawable.about);
		}

		// 设置图片颜色及透明度；
		private Bitmap createBitmapFromARGB(int colorARGB, int width, int height) {

			int[] argb = new int[width * height];

			for (int i = 0; i < argb.length; i++) {
				argb[i] = colorARGB;
			}

			// 该函数根据颜色数组来创建位图；
			// createBitmap(int[] colors, int width,int height,Bitmap.Config
			// config)
			// 注意:颜色数组的长度>=width*height
			// 根据width和height创建空位图，然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
			// config是一个枚举，可以用它来指定位图“质量”。
			return Bitmap.createBitmap(argb, width, height, Config.ARGB_8888);

		}

		// 若cover为图时，在此设置图片；【未用】
		private Bitmap createBitmapFromSRC() {

			// BitmapFactory.decodeResource（）可加载工程目录中的drawable文件夹下的图片资源
			// 第一个参数是包含要加载的位图资源文件的对象（一般写成 getResources（）就ok了）。
			// 第二个参数是需要加载的位图资源的Id
			return BitmapFactory.decodeResource(getResources(),
					R.drawable.cover);

		}

		// 可在在此设置透明度alpha（0至255）【未用】
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

		// 若bitmap小于屏幕，再次创建可缩放位图；【未用】
		private Bitmap scaleBitmapFillScreen(Bitmap bm) {

			// createScaledBitmap：创建可缩放的位图，
			// bm——原始位图 SCREEN_W——目标宽度 SCREEN_H——目标高度 true——是否过滤
			return Bitmap.createScaledBitmap(bm, SCREEN_W, SCREEN_H, true);

		}

		private void setCoverBitmap(Bitmap bm) {

			// 设置画笔

			mPaint = new Paint();

			mPaint.setAlpha(0); // 设置画笔透明度，取值范围为0~255。0为透明

			// setXfermode设置两张图片相交时的模式
			mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN)); // PorterDuff.Mode.DST_I取两层绘制交集。显示上层。

			mPaint.setAntiAlias(false); // 防止边缘的锯齿，但会消耗较大资源，绘制图形速度会变慢

			mPaint.setDither(true); // 防抖动

			mPaint.setStyle(Paint.Style.STROKE); // 画笔类型 STROKE空心 FILL 实心

			// mPaint.setStrokeJoin(Paint.Join.ROUND); //设置绘制时各图形的结合方式，如平滑效果等
			mPaint.setStrokeJoin(Paint.Join.MITER);

			mPaint.setStrokeCap(Paint.Cap.ROUND); // 置笔刷的图形样式，ROUND/SQUARE/BUTT

			mPaint.setStrokeWidth(41); // 设置笔刷的粗细度

			// set path
			mPath = new Path();

			/* 创建缓冲区 */
			mBitmap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Config.ARGB_8888);

			/* 创建Canvas */
			mCanvas = new Canvas();

			/* 设置为：将内容绘制在mBitmap上 */
			mCanvas.setBitmap(mBitmap);

			/* 将bm绘制到mBitmap上 */
			mCanvas.drawBitmap(bm, 0, 0, null);

		}

		@Override
		protected void onDraw(Canvas canvas) {

			/* 将mBitmap绘制到屏幕上 */
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

		// 定轨迹
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

				touch_start(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_MOVE:

				touch_move(x, y);
				invalidate();
				break;

			case MotionEvent.ACTION_UP:

				touch_up();
				invalidate();
				break;

			}

			return true;

		}

	}

}
