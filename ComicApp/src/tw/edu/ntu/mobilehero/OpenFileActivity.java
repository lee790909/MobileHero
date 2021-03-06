package tw.edu.ntu.mobilehero;

import greendroid.sql.FileManager;
import greendroid.sql.FileManager.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OpenFileActivity extends Fragment {
	private List<File> mPanelFiles = new ArrayList<File>();
	private List<Bitmap> mPanelBitmaps = new ArrayList<Bitmap>();
		
	private LinearLayout layout;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	Log.d("OpenFileActivity", "onCreateView");
    	View v = inflater.inflate(R.layout.open_existing_panel, container, false);
        
        getPanels();
        
        layout = (LinearLayout) v.findViewById(R.id.browser_edit);
        for(int i = 0 ; i < mPanelFiles.size() ; i++) {
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setImageBitmap(mPanelBitmaps.get(i));
            final int k = i;
            image.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                  String fileName = mPanelFiles.get(k).getName();
                  
                  FileManager fileManager = new FileManager(getActivity());
                  fileManager.open();
                  FileInfo fileInfo = fileManager.getFileInfo(fileName);
                  fileManager.close();
                  
                  if(getActivity() instanceof ActivityGroupEdit) {
                      FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                      Bundle bundle = new Bundle();
                      bundle.putString("filePath", mPanelFiles.get(k).getAbsolutePath());
                      ft.replace(R.id.simple_fragment, new PaintingActivity(bundle));
                      ft.addToBackStack(null); 
                      ft.commitAllowingStateLoss(); 
                  }
                }
            });
            layout.addView(image);
        }
        
//        GridView gridView = (GridView) v.findViewById(R.id.gridview);
//        gridView.setAdapter(new PanelAdapter());
//        gridView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//				String fileName = mPanelFiles.get(position).getName();
//				
//				FileManager fileManager = new FileManager(getActivity());
//				fileManager.open();
//				FileInfo fileInfo = fileManager.getFileInfo(fileName);
//				fileManager.close();
//				
//				if(getActivity() instanceof ActivityGroupEdit) {
//					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//					Bundle bundle = new Bundle();
//					bundle.putString("filePath", mPanelFiles.get(position).getAbsolutePath());
//					ft.replace(R.id.simple_fragment, new PaintingActivity(bundle));
//					ft.addToBackStack(null); 
//					ft.commitAllowingStateLoss(); 
//				}
//			}
//        	
//        });
        return v;
    }

    private void getPanels() {        
    	File panelStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    	File ComicRelaysDir = new File(panelStorageDir, "ComicRelays");
    	
    	if (ComicRelaysDir.exists()) {
    		File[] files = ComicRelaysDir.listFiles();
	        for (File file : files) {
	        	if (file.isFile()) {
	        		Log.d("fileName", file.getName());
	        		Bitmap panel = BitmapFactory.decodeFile(file.getAbsolutePath());
	        		mPanelFiles.add(file);
	        		mPanelBitmaps.add(panel);
	        	}
	        }
    	}
    }

    public class PanelAdapter extends BaseAdapter {
        public PanelAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;

            if (convertView == null) {
                i = new ImageView(getActivity());
                i.setScaleType(ImageView.ScaleType.CENTER);
                i.setLayoutParams(new GridView.LayoutParams(280, 210));
            } else {
                i = (ImageView) convertView;
            }

            Bitmap panel = mPanelBitmaps.get(position);
            i.setImageBitmap(panel);

            return i;
        }

        public final int getCount() {
        	return mPanelBitmaps.size();
        }

        public final Object getItem(int position) {
            return mPanelBitmaps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }
}