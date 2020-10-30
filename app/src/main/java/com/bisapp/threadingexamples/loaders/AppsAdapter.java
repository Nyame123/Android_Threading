package com.bisapp.threadingexamples.loaders;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bisapp.threadingexamples.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppsAdapter extends ArrayAdapter<AppData> {

  private Context context;
  private RequestBuilder<Drawable> request;

  public AppsAdapter(
          Context context,
          int resourceId) {
    super(context, resourceId);
    this.context = context;
    request = Glide.with(context)
            .asDrawable().fitCenter();

  }

  public void setData(List<AppData> data) {
    clear();

    if (data != null) {
      addAll(data);
    }
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    final AppData rowItem = getItem(position);

    View view;
    if (convertView == null) {
      LayoutInflater mInflater =
              (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      view = mInflater.inflate(R.layout.rowlayout, null);
      final AppHolder vholder = new AppHolder(view);

      view.setTag(vholder);
    } else {
      view = convertView;
    }

    final AppHolder holder = (AppHolder) view.getTag();

            request.load(rowItem.drawable)
            .into(holder.apkIcon);

    holder.txtTitle.setText(rowItem.label);


    //	File f = new File(rowItem.getDesc());
    holder.size.setText(rowItem.fileSize);

    return view;
  }


  /**
   * @author Emmanuel Messulam <emmanuelbendavid@gmail.com> on 10/12/2017, at 14:45.
   */
  public class AppHolder extends RecyclerView.ViewHolder {

    public final CircleImageView apkIcon;
    public final TextView txtTitle;
    public final TextView size;

    public AppHolder(View view) {
      super(view);

      txtTitle = view.findViewById(R.id.title);
      apkIcon = view.findViewById(R.id.apk_icon);
      size = view.findViewById(R.id.size);
    }
  }


}
