package cn;

import cn.back_exit.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class kiic_backActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button button = (Button)findViewById(R.id.Button01);
        
        button.setOnClickListener(
        	new OnClickListener(){
        		public void onClick(View v){
        			gotoChange();
        		}
        	});
        
    }
    
    public void gotoChange()
    {
    	setContentView(R.layout.change);
    }
    
  
    
    exit Exit=new exit();
    
    @Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {    
      if (keyCode == KeyEvent.KEYCODE_BACK) {    
              pressAgainExit();    
              return true;    
              }    
    
        return super.onKeyDown(keyCode, event);    
    }    
      
  private void pressAgainExit() {    
          if (Exit.isExit()) {    
              finish();    
          } else {    
              Toast.makeText(getApplicationContext(), "再按一次退出程序",    
                      Toast.LENGTH_SHORT).show();    
              Exit.doExitInOneSecond();    
          }    
      }    

}