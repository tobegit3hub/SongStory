package com.aliceinwc.ui;

import java.util.ArrayList;

import com.aliceinwc.R;
import com.aliceinwc.data.SongStory;
import com.aliceinwc.data.SongStoryCategory;
import com.aliceinwc.database.StoryDatabaseAccessor;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class StoryListActivity extends Activity implements OnItemClickListener{

	private Context context;  
	private LayoutInflater inflater;  
	
	
	private ViewPager storyListPager;
	
	private StoryListPagerAdapter storyListPagerAdapter;  
	
	private ArrayList<View> storyListHeaderViews;
	
//	private ArrayList<StoryListHeaderAdapter> storyListHeaderAdapters;
	
	
	private ArrayList<View> storyLisViews;  
	
	private ArrayList<StoryListAdapter> storyListAdapters;
	

	
	
	
	private SongStoryCategory[] songStoryCategories;
	
	private ArrayList<SongStory>[] storiesArray;

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
        setContentView(R.layout.story_list_activity);

        
        StoryDatabaseAccessor.init(context);
        
        
        context = this;  
        inflater = getLayoutInflater();
        
        
        songStoryCategories = new SongStoryCategory[SongStoryCategory.CATEGORY_TOTAL_NUMBER];

        
        storiesArray = new ArrayList[SongStoryCategory.CATEGORY_TOTAL_NUMBER];
        for(int i=0; i<SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i){
        	storiesArray[i] = new ArrayList<SongStory>();
        	
        	songStoryCategories[i] = new SongStoryCategory(context, i);
        	
        }
        //temp
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[0].add(new SongStory());
        storiesArray[1].add(new SongStory());
        storiesArray[1].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[2].add(new SongStory());
        storiesArray[3].add(new SongStory());
        storiesArray[4].add(new SongStory());
        storiesArray[4].add(new SongStory());
        
        
        storyListHeaderViews = new ArrayList<View>();
        //storyListHeaderAdapters = new ArrayList<StoryListHeaderAdapter>();
        
        
        storyLisViews = new ArrayList<View>();
        storyListAdapters = new ArrayList<StoryListAdapter>();
        
        
        
        for(int i=0; i<SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i){
        	storyListAdapters.add(new StoryListAdapter(i));
        	
        	storyListHeaderViews.add(inflater.inflate(R.layout.story_list_header, null));  
        	((ImageView)storyListHeaderViews.get(i).findViewById(R.id.story_list_header_image)).setImageResource(songStoryCategories[i].imageIds[0]);
        	//storyListHeaderAdapters.add(new StoryListHeaderAdapter(i));
        	
        	ViewPager storyListHeaderViewPager = (ViewPager) storyListHeaderViews.get(i).findViewById(R.id.story_list_header_pager);
        	
        	//storyListHeaderViewPager.setAdapter(new StoryListHeaderAdapter(i));
        	
        }
        
        
        for(int i=0; i<SongStoryCategory.CATEGORY_TOTAL_NUMBER; ++i){
        	
        	View completeListView = inflater.inflate(R.layout.story_list_view, null);
        	
        	((TextView)completeListView.findViewById(R.id.story_list_category_title)).setText(songStoryCategories[i].title);
        	((TextView)completeListView.findViewById(R.id.story_list_category_number)).setText(""+songStoryCategories[i].displayNumber);
        	
        	ListView listView = (ListView)completeListView.findViewById(R.id.story_list_view);
        	
 
        	listView.addHeaderView(storyListHeaderViews.get(i));
        	
        	
        	
        	listView.addFooterView(inflater.inflate(R.layout.story_list_footer, null));
        	
        	listView.setAdapter(storyListAdapters.get(i));
        	
        	storyLisViews.add(completeListView);
        }
        
        
        
        storyListPager = (ViewPager) findViewById(R.id.story_list_pager); 
        storyListPagerAdapter = new StoryListPagerAdapter();
        storyListPager.setAdapter(storyListPagerAdapter);
        
        
    }
	
	
    
    private class StoryListPagerAdapter extends PagerAdapter{
    	
    	public StoryListPagerAdapter(){
    		
    	}

    	
		@Override
		public int getCount() {
			
			//return 0;
			
			return storyLisViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			//return false;
			
			return arg0==(arg1);
		}
    	
    	
    	
		@Override  
        public Object instantiateItem(View collection, int position) {  
  
              
            ((ViewPager) collection).addView(storyLisViews.get(position),0);  
              
            return storyLisViews.get(position);  
        }  
		
		
		 @Override  
        public void destroyItem(View collection, int position, Object view) {  
			 
            ((ViewPager) collection).removeView(storyLisViews.get(position));  
        }  
		 
		 @Override  
        public void finishUpdate(View arg0) {
			 
		 }  
          
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        	
        }  
  
        @Override  
        public Parcelable saveState() {  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {
        	
        }  
        
    }
    
    
    
    private class StoryListAdapter extends BaseAdapter {

    	ArrayList<SongStory> stories;
    	
    	
    	public StoryListAdapter(int categoryNumber){
    		
    		stories = storiesArray[categoryNumber];
    		
    	}
    	
    	
		@Override
		public int getCount() {
			
			//return 0;
			
			return stories.size();
		}

		@Override
		public Object getItem(int position) {
			
			//return null;
			
			return stories.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			//return 0;
			
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//return null;
			
            final SongStory data = (SongStory) getItem(position);
            
            
            if (convertView == null) {
            	convertView = getLayoutInflater().inflate(R.layout.story_list_item, null);
            	
                
            }
            
            return convertView;
		}
    	

    }
    
    
 /*   
    private class StoryListHeaderAdapter extends PagerAdapter{
    	
    	private ArrayList<View> imageViews;
    	private int imageIds[];

    	public StoryListHeaderAdapter(int category){
    		
    		imageIds = new int[SongStoryCategory.CATEGORY_IMAGE_NUMBER];
    		
    		imageViews = new ArrayList<View>();
    		
    		for(int i=0; i<SongStoryCategory.CATEGORY_IMAGE_NUMBER; ++i){
    			imageIds[i] = songStoryCategories[category].imageIds[i];
    			
    			ImageView imageView = new ImageView(context);  
    			imageView.setImageResource(imageIds[i]);
    			imageViews.add(imageView);
    		}
    	}
		@Override
		public int getCount() {
			
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			
			return arg0 == (View)arg1;
		}

		
		@Override
		public Object instantiateItem(View container, int position) {
		    
//			ImageView imageView = new ImageView(context);  
//
//			imageView.setImageResource(imageIds[position]);
//	        
//	        ((ViewPager)container).addView(imageViews.get(position), 0);  
//	        
//	        return imageViews.get(position);  
	        
            ((ViewPager) container).addView(imageViews.get(position),0);  
            
            return imageViews.get(position);  
			
//			
//			ImageView imageView = new ImageView(context);
//			imageView.setImageResource(R.drawable.cantonese_category_1);
//			return imageView;
			
		}

		
		 @Override  
	        public void destroyItem(View collection, int position, Object view) {  
				 
	            ((ViewPager) collection).removeView(imageViews.get(position));  
	        }  
			 
			 @Override  
	        public void finishUpdate(View arg0) {
				 
			 }  
	          
	  
	        @Override  
	        public void restoreState(Parcelable arg0, ClassLoader arg1) {
	        	
	        }  
	  
	        @Override  
	        public Parcelable saveState() {  
	            return null;  
	        }  
	  
	        @Override  
	        public void startUpdate(View arg0) {
	        	
	        }  
	        
    }
*/


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		
	}
    
    
	
}
