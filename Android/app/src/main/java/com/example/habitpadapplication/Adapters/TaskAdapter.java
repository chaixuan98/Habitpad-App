package com.example.habitpadapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitpadapplication.Model.Task;
import com.example.habitpadapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context context;
    private List<Task> tasks = new ArrayList<>();

    public TaskAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTitle, taskDescription, taskPointTv;
        private ImageView taskImage;
        private LinearLayout taskLayout;

        public MyViewHolder (View view){
            super(view);

            taskImage = view.findViewById(R.id.task_image);
            taskTitle = view.findViewById(R.id.task_title);
            taskDescription = view.findViewById(R.id.task_description);
            taskPointTv = view.findViewById(R.id.task_point);
            taskLayout = view.findViewById(R.id.task_layout);

        }
    }


    @NonNull
    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.task_raw,parent,false);
        return new TaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final Task task = tasks.get(position);
        String taskPoint = task.getTaskPoint();

        Glide.with(context).asBitmap().load(task.getTaskImg())
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_image_not_supported_24)
                .dontAnimate().into(holder.taskImage);

        holder.taskTitle.setText(task.getTaskTitle());
        holder.taskDescription.setText(task.getTaskDescription());
        holder.taskPointTv.setText("+"+taskPoint);


        }



    @Override
    public int getItemCount() {
        return tasks.size();
    }


}
