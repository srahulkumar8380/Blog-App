package Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srahulkumar.blog.Model.Blog;
import com.example.srahulkumar.blog.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {
  private Context context;
  private List<Blog> blogList;

    public BlogRecyclerAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;

    }

    @NonNull
    @Override
    public BlogRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerAdapter.ViewHolder holder, int position) {
          Blog blog=blogList.get(position);
          String imageUrl="URL";
          holder.title.setText(blog.getTitle());
          holder.desc.setText(blog.getDesc());
          holder.timestamp.setText(blog.getTimestamp());

          java.text.DateFormat dateFormat=java.text.DateFormat.getDateInstance();
          String FormattedDate=dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
          holder.timestamp.setText(FormattedDate);
          imageUrl=blog.getImage();

        //TODO: Use Picasso library to load image
        Picasso.get()
                .load(imageUrl)
                .into(holder.image);


    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,timestamp;
          public ImageView image;
          String userId;
        public ViewHolder(View view ,Context ctx) {
            super(view);
            context=ctx;

              title=view.findViewById(R.id.postTitleId);
              desc=view.findViewById(R.id.postDiscriptionId);
              timestamp=view.findViewById(R.id.postDateId);
              image=view.findViewById(R.id.postImageId);
              userId=null;
              view.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      //we can go the next Activity...
                  }
              });
        }
    }

}
