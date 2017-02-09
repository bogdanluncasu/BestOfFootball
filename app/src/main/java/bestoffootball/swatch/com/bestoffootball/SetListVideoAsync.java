package bestoffootball.swatch.com.bestoffootball;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by swatch on 2/9/17.
 */
public class SetListVideoAsync extends AsyncTask<Void, Void, Void> {
    private final String KEY="AIzaSyDsiVxrCa36Psf7vo63bS5ExHnQ-IiKptY";
    private final String CHANNELID="UC8jIOIFOzpmw0ZJFf4xdXlg";
    private HttpHandler sh = new HttpHandler();
    private String url="https://www.googleapis.com/youtube/v3/search?key=" +
            KEY + "&channelId="+CHANNELID+"&part=snippet,id&order=date";
    private ListView listView;
    private List<Video> listVideos;
    private LayoutInflater inflater;
    private Context context;
    private TextView channelDescription;
    private String description;
    public SetListVideoAsync(Context context, LayoutInflater inflater, ListView listView,
                             List<Video> listVideos, TextView channelDescription){
        this.inflater=inflater;
        this.listView=listView;
        this.listVideos=listVideos;
        this.context=context;
        this.channelDescription=channelDescription;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String jsonStr=sh.makeServiceCall(url);

        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            JSONArray vids = jsonObject.getJSONArray("items");
            for (int i=0;i<vids.length();i++){
                JSONObject vid=vids.getJSONObject(i);

                Video video=new Video();
                JSONObject id=vid.getJSONObject("id");
                if(!id.has("videoId")) {
                    description = vid.getJSONObject("snippet").getString("description");
                    continue;
                }
                video.setVideoId(id.getString("videoId"));
                video.setTitle(vid.getJSONObject("snippet").getString("title"));
                video.setDescription(vid.getJSONObject("snippet").getString("description"));
                video.setThumbnail(vid.getJSONObject("snippet")
                        .getJSONObject("thumbnails").getJSONObject("default").getString("url"));

                listVideos.add(video);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        channelDescription.setText(description);
        listView.setAdapter(new ListVideoAdapter(inflater,listVideos));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,FloatingVideo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videoId",listVideos.get(position).getVideoId());
                context.startActivity(intent);
            }
        });

    }
}
