package ishchenko.a.testbt.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ishchenko.a.testbt.R;
import ishchenko.a.testbt.SupportClasses.Docs;

/**
 * Created by andrey on 09.03.17.
 */

public class DocsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Docs> docsList;
    private LayoutInflater inflater;

    public DocsAdapter(Context context, ArrayList<Docs> docsList) {
        this.context = context;
        this.docsList = docsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return docsList.size();
    }

    @Override
    public Object getItem(int i) {
        return docsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Docs doc = docsList.get(i);
        ViewHolder viewHolder = null;
        if(view==null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.grid_adapter_item, viewGroup,false);
            viewHolder.name=(TextView) view.findViewById(R.id.filename);
            viewHolder.ext=(TextView) view.findViewById(R.id.extview);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String[] splitName = doc.getDoc().split("/");
        String[] nameExt = splitName[splitName.length-1].split("\\.");
        viewHolder.ext.setText("." + nameExt[nameExt.length - 1]);
        viewHolder.name.setText(nameExt[0]);

        return view;
    }

    private class ViewHolder {
        TextView name, ext;
    }
}
