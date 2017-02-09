package bestoffootball.swatch.com.bestoffootball;

import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by swatch on 2/9/17.
 */
public class ListVideoAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Video> vids;
    public ListVideoAdapter(LayoutInflater layoutInflater,List<Video> listOfVideos) {
        inflater=layoutInflater;
        vids=listOfVideos;
    }



    @Override
    public int getCount() {
        return vids.size();
    }

    @Override
    public Object getItem(int position) {
        return vids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItems = null;


        listItems = inflater.inflate(R.layout.video_list, parent, false);
        Video video = (Video) getItem(position);

        ImageView imageView = ((ImageView) listItems.findViewById(R.id.imageView));
        TextView textView = ((TextView) listItems.findViewById(R.id.editText));

        new DownloadImageTask(imageView)
                .execute(video.getThumbnail());

        textView.setText(video.getDescription());

        return listItems;
    }



}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}