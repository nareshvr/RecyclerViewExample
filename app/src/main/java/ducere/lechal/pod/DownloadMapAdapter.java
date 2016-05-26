package ducere.lechal.pod;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DownloadMapAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final ArrayList<String> web;
	private final ArrayList<String> size;
	int images[];
	Typeface tf;
	private final ArrayList<Integer> downloadimage ;
	int continentImages[] = {R.mipmap.battery_20,R.mipmap.battery_20,R.mipmap.battery_20,R.mipmap.battery_20,
			R.mipmap.battery_20,R.mipmap.battery_20,R.mipmap.battery_20};
	int cont;


	public DownloadMapAdapter(Activity context, ArrayList<String> web, int[] images, ArrayList<String> size,
			ArrayList<Integer> downloadimage,int cont) {
		super(context, R.layout.autocomplete_row, web);
		this.context = context;
		this.web = web;
		this.images = images;
		this.size = size;
		this.downloadimage = downloadimage;
		this.cont = cont;

	}


	@Override
	public View getView(int position, View view, ViewGroup parent) {

		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.map_downlaod, null, true);
		TextView tvName = (TextView) rowView.findViewById(R.id.tv_name);
		TextView tvSize = (TextView) rowView.findViewById(R.id.tv_size);
		ImageView iv = (ImageView) rowView.findViewById(R.id.iv_download);
		ImageView ivContinent = (ImageView) rowView.findViewById(R.id.iv_map);
		LinearLayout llProgress = (LinearLayout)rowView.findViewById(R.id.llProgress);
        ProgressBar progressBar = (ProgressBar)rowView.findViewById(R.id.status_progress);
        ImageView ivCancel= (ImageView)rowView.findViewById(R.id.ivCancel);

		if(web != null && web.size() >0){

			tvName.setText(web.get(position).toString());
			tvSize.setText(size.get(position).toString());
			if(downloadimage.get(position) == 0)
				iv.setBackgroundResource(images[3]);
			else if (downloadimage.get(position) == 1)
				iv.setBackgroundResource(images[1]);
			else if (downloadimage.get(position) == 2)
				iv.setBackgroundResource(images[2]);
			if(cont==0){
				ivContinent.setVisibility(View.VISIBLE);
				iv.setBackground(null);
				ivContinent.setBackgroundResource(continentImages[position]);
			} else {
				ivContinent.setVisibility(View.GONE);

			}
		}
		return rowView;
	}
}

