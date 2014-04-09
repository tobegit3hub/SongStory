package com.aliceinwc.data;

import com.aliceinwc.R;

import android.content.Context;

public class SongStoryCategory {

	public final static int CATEGORY_TOTAL_NUMBER = 5;
	
	public final static int CATEGORY_CANTONESE = 0;
	public final static int CATEGORY_MANDARIN = 1;
	public final static int CATEGORY_ENGLISH = 2;
	public final static int CATEGORY_ASIAN = 3;
	public final static int CATEGORY_OTHER = 4;
	
	public final static int CATEGORY_IMAGE_NUMBER = 5;
	
	
	public int number;
	public int displayNumber;
	public String title;
	public int[] imageIds;
	
	
	
	public SongStoryCategory(Context context, int number){
		
		this.number = number;
		displayNumber = number+1;
		imageIds = new int[CATEGORY_IMAGE_NUMBER];
		
		if(number==CATEGORY_CANTONESE){
			title = context.getResources().getString(R.string.cantonese_category_title);
			imageIds[0] = R.drawable.cantonese_category_0;
			imageIds[1] = R.drawable.cantonese_category_1;
			imageIds[2] = R.drawable.cantonese_category_2;
			imageIds[3] = R.drawable.cantonese_category_3;
			imageIds[4] = R.drawable.cantonese_category_4;
			
		}else if(number==CATEGORY_MANDARIN){
			title = context.getResources().getString(R.string.mandarin_category_title);
			imageIds[0] = R.drawable.mandarin_category_0;
			imageIds[1] = R.drawable.mandarin_category_1;
			imageIds[2] = R.drawable.mandarin_category_2;
			imageIds[3] = R.drawable.mandarin_category_3;
			imageIds[4] = R.drawable.mandarin_category_4;
						
		}else if(number==CATEGORY_ENGLISH){
			title = context.getResources().getString(R.string.english_category_title);
			imageIds[0] = R.drawable.english_category_0;
			imageIds[1] = R.drawable.english_category_1;
			imageIds[2] = R.drawable.english_category_2;
			imageIds[3] = R.drawable.english_category_3;
			imageIds[4] = R.drawable.english_category_4;
						
		}else if(number==CATEGORY_ASIAN){
			title = context.getResources().getString(R.string.asian_category_title);
			imageIds[0] = R.drawable.asian_category_0;
			imageIds[1] = R.drawable.asian_category_1;
			imageIds[2] = R.drawable.asian_category_2;
			imageIds[3] = R.drawable.asian_category_3;
			imageIds[4] = R.drawable.asian_category_4;
						
		}else if(number==CATEGORY_OTHER){
			title = context.getResources().getString(R.string.other_category_title);
			imageIds[0] = R.drawable.other_category_0;
			imageIds[1] = R.drawable.other_category_1;
			imageIds[2] = R.drawable.other_category_2;
			imageIds[3] = R.drawable.other_category_3;
			imageIds[4] = R.drawable.other_category_4;
						
		}
		
		
		
	}
	
}
