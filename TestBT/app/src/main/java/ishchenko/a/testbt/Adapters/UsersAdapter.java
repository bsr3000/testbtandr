package ishchenko.a.testbt.Adapters;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import ishchenko.a.testbt.R;
import ishchenko.a.testbt.RestClient;
import ishchenko.a.testbt.SupportClasses.User;

/**
 * Created by andrey on 08.03.17.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> userList;
    private SharedPreferences sP;

    public UsersAdapter(List<User> userList, SharedPreferences sP) {
        this.userList = userList;
        this.sP = sP;
    }


    public int getUserId(int position) {
        return userList.get(position).getPk();
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.name.setText(user.getFirstName());
        holder.last_name.setText(user.getLastName());
        if(user.getPhoto().length() > 0) {
            DownloadImageTask dImage = new DownloadImageTask(holder.avatar);
            dImage.execute(RestClient.getInstance(sP).getBaseUrl() + user.getPhoto());
        }
    }

    @Override
    public int getItemCount() {
        if (userList == null) {
            return 0;
        }
        return userList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView last_name;
        ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.adapitem_name);
            last_name = (TextView) itemView.findViewById(R.id.adapitem_surname);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, result.getWidth(), result.getHeight()), new RectF(0, 0, 150, 150), Matrix.ScaleToFit.CENTER);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), m, true);
            bmImage.setImageBitmap(result);
        }
    }
}
