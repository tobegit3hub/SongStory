package com.aliceinwc.lyrics;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LyricView extends TextView {
	private Paint notCurrentPaint;
	private Paint currentPaint;

	private int notCurrentPaintColor = Color.GRAY;
	private int currentPaintColor = Color.WHITE;

	private Typeface textTypeFace = Typeface.SERIF;
	private Typeface currentTextTypeFace = Typeface.DEFAULT_BOLD;
	private float width;
	private static Lyric mLyric;
	private int brackGroundColor = 0xf000000;
	private float lrcTextSize = 22;
	private float currentTextSize = 24;

	public float mTouchHistoryY;

	private int height;
	private long currentDuringTime;
	private int TextHeight = 50;
	private boolean lrcInitDone = false;
	public int index = 0;
	private int lastIndex = 0;
	private List<Sentence> Sentencelist;

	private long currentTime;
	private long sentenctTime;

	public Paint getNotCurrentPaint() {
		return notCurrentPaint;
	}

	public void setNotCurrentPaint(Paint notCurrentPaint) {
		this.notCurrentPaint = notCurrentPaint;
	}

	public boolean isLrcInitDone() {
		return lrcInitDone;
	}

	public Typeface getCurrentTextTypeFace() {
		return currentTextTypeFace;
	}

	public void setCurrentTextTypeFace(Typeface currentTextTypeFace) {
		this.currentTextTypeFace = currentTextTypeFace;
	}

	public float getLrcTextSize() {
		return lrcTextSize;
	}

	public void setLrcTextSize(float lrcTextSize) {
		this.lrcTextSize = lrcTextSize;
	}

	public void setLrcInitDone(boolean lrcInitDone) {
		this.lrcInitDone = lrcInitDone;
	}

	public Paint getCurrentPaint() {
		return currentPaint;
	}

	public void setCurrentPaint(Paint currentPaint) {
		this.currentPaint = currentPaint;
	}

	public static Lyric getmLyric() {
		return mLyric;
	}

	public void setmLyric(Lyric mLyric) {
		LyricView.mLyric = mLyric;
	}

	public float getCurrentTextSize() {
		return currentTextSize;
	}

	public void setCurrentTextSize(float currentTextSize) {
		this.currentTextSize = currentTextSize;
	}

	public List<Sentence> getSentencelist() {
		return Sentencelist;
	}

	public void setSentencelist(List<Sentence> sentencelist) {
		Sentencelist = sentencelist;
	}

	public int getNotCurrentPaintColor() {
		return notCurrentPaintColor;
	}

	public void setNotCurrentPaintColor(int notCurrentPaintColor) {
		this.notCurrentPaintColor = notCurrentPaintColor;
	}

	public int getCurrentPaintColor() {
		return currentPaintColor;
	}

	public void setCurrentPaintColor(int currentPaintColor) {
		this.currentPaintColor = currentPaintColor;
	}

	public Typeface getTextTypeFace() {
		return textTypeFace;
	}

	public void setTextTypeFace(Typeface textTypeFace) {
		this.textTypeFace = textTypeFace;
	}

	public int getBrackGroundColor() {
		return brackGroundColor;
	}

	public void setBrackGroundColor(int brackGroundColor) {
		this.brackGroundColor = brackGroundColor;
	}

	public long getCurrentDuringTime() {
		return currentDuringTime;
	}

	public void setCurrentDuringTime(long currentDuringTime) {
		this.currentDuringTime = currentDuringTime;
	}

	public int getTextHeight() {
		return TextHeight;
	}

	public void setTextHeight(int textHeight) {
		TextHeight = textHeight;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public long getSentenctTime() {
		return sentenctTime;
	}

	public void setSentenctTime(long sentenctTime) {
		this.sentenctTime = sentenctTime;
	}

	public LyricView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	public LyricView(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	public LyricView(Context context, AttributeSet attr, int i) {
		super(context, attr, i);
		init();

	}

	private void init() {
		setFocusable(true);

		notCurrentPaint = new Paint();
		notCurrentPaint.setAntiAlias(true);

		notCurrentPaint.setTextAlign(Paint.Align.CENTER);

		currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(brackGroundColor);
		notCurrentPaint.setColor(notCurrentPaintColor);
		currentPaint.setColor(currentPaintColor);

		notCurrentPaint.setTextSize(lrcTextSize);
		notCurrentPaint.setTypeface(textTypeFace);

		currentPaint.setTextSize(lrcTextSize);
		currentPaint.setTypeface(currentTextTypeFace);

		if (index == -1)
			return;

		// float plus = currentDuringTime == 0?30:30+ (((float) currentTime -
		// (float) sentenctTime) / (float) currentDuringTime)
		// * (float) 30;
		//
		// canvas.translate(0, -plus);

		try {
			canvas.drawText(Sentencelist.get(index).getContent(), width / 2,
					height / 2, currentPaint);

			canvas.drawText(Sentencelist.get(index - 1).getContent(),
					width / 2, height / 4, notCurrentPaint);

			canvas.drawText(Sentencelist.get(index + 1).getContent(),
					width / 2, height * 3 / 4, notCurrentPaint);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		width = w; // remember the center of the screen
		height = h;
		// middleY = h * 0.5f;
	}

	public void updateIndex(long time) {
		this.currentTime = time;

		index = mLyric.getNowSentenceIndex(time);
		if (index != -1) {
			Sentence sen = Sentencelist.get(index);
			sentenctTime = sen.getFromTime();
			currentDuringTime = sen.getDuring();
			// Log.e("===","sa"+sen.toString());
		}

	}

}
