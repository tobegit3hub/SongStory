package com.aliceinwc.data;

import java.util.ArrayList;

import android.content.Context;

import com.aliceinwc.R;

public class SongStoryCategory {

	public final static int CATEGORY_TOTAL_NUMBER = 5;

	public final static int CATEGORY_CANTONESE = 0;
	public final static int CATEGORY_MANDARIN = 1;
	public final static int CATEGORY_ENGLISH = 2;
	public final static int CATEGORY_ASIAN = 3;
	public final static int CATEGORY_OTHER = 4;

	public final static int CATEGORY_IMAGE_NUMBER = 5;

	public int number;
	public String title;
	public String titleSpecification;
	public int[] imageIds;

	public ArrayList<SongStory> stories;
	public int storyNumber = 0;

	public SongStoryCategory(Context context, int number) {

		this.number = number;
		imageIds = new int[CATEGORY_IMAGE_NUMBER];

		title = context.getResources().getStringArray(
				R.array.story_list_category_title)[number];
		titleSpecification = context.getResources().getStringArray(
				R.array.story_list_category_title_specification)[number];

		if (number == CATEGORY_CANTONESE) {
			imageIds[0] = R.drawable.cantonese_category_0;
			imageIds[1] = R.drawable.cantonese_category_1;
			imageIds[2] = R.drawable.cantonese_category_2;
			imageIds[3] = R.drawable.cantonese_category_3;
			imageIds[4] = R.drawable.cantonese_category_4;

		} else if (number == CATEGORY_MANDARIN) {
			imageIds[0] = R.drawable.mandarin_category_0;
			imageIds[1] = R.drawable.mandarin_category_1;
			imageIds[2] = R.drawable.mandarin_category_2;
			imageIds[3] = R.drawable.mandarin_category_3;
			imageIds[4] = R.drawable.mandarin_category_4;

		} else if (number == CATEGORY_ENGLISH) {
			imageIds[0] = R.drawable.english_category_0;
			imageIds[1] = R.drawable.english_category_1;
			imageIds[2] = R.drawable.english_category_2;
			imageIds[3] = R.drawable.english_category_3;
			imageIds[4] = R.drawable.english_category_4;

		} else if (number == CATEGORY_ASIAN) {
			imageIds[0] = R.drawable.asian_category_0;
			imageIds[1] = R.drawable.asian_category_1;
			imageIds[2] = R.drawable.asian_category_2;
			imageIds[3] = R.drawable.asian_category_3;
			imageIds[4] = R.drawable.asian_category_4;

		} else if (number == CATEGORY_OTHER) {
			imageIds[0] = R.drawable.other_category_0;
			imageIds[1] = R.drawable.other_category_1;
			imageIds[2] = R.drawable.other_category_2;
			imageIds[3] = R.drawable.other_category_3;
			imageIds[4] = R.drawable.other_category_4;

		}

	}

}
