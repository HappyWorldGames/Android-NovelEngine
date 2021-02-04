package ru.HappyWorldGames.NovelEngine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> projectsList;
    private MainRecyclerViewAdapter adapter;
    private AlertDialog createProjectAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) start();
        else requestPermissionShow();
    }

    private void requestPermissionShow(){
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);

        TextView textView = new TextView(this);
        textView.setText(R.string.request_permission);

        Button requestButton = new Button(this);
        requestButton.setText(R.string.request_permission_button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });

        main.addView(textView);
        main.addView(requestButton);

        setContentView(main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) return;

        start();
    }

    private void start() {
        if(!Files.projectsDir.exists()) Files.projectsDir.mkdirs();

        projectsList = new ArrayList<String>();
        for(String name : Files.projectsDir.list())
            projectsList.add(name);

        setContentView(R.layout.activity_main);

        Button createProjectButton = findViewById(R.id.create_project);
        RecyclerView projectListRecyclerView = findViewById(R.id.project_list);

        createProjectAlertDialog = createProjectAlertDialog();

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProjectAlertDialog.show();
            }
        });

        projectListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainRecyclerViewAdapter(this, projectsList);
        adapter.setClickListener(new MainRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openProject(projectsList.get(position));
            }
        });
        projectListRecyclerView.setAdapter(adapter);
    }

    private AlertDialog createProjectAlertDialog(){
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);

        final EditText nameProject = new EditText(this);
        nameProject.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
        nameProject.setHint(R.string.name_project);

        final EditText packageName = new EditText(this);
        packageName.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
        packageName.setHint(R.string.package_name);

        main.addView(nameProject);
        main.addView(packageName);

        return new AlertDialog.Builder(this)
                .setTitle(R.string.create_project)
                .setView(main)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String projectName = nameProject.getText().toString();
                        if(Files.createProject(projectName, packageName.getText().toString()) != null) {
                            projectsList.add(projectName);
                            adapter.notifyItemInserted(projectsList.size() - 1);

                            nameProject.setText("");
                            packageName.setText("");
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nameProject.setText("");
                        packageName.setText("");
                    }
                }).create();
    }

    public void openProject(String projectName){
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        intent.putExtra("projectName", projectName);

        startActivity(intent);
    }


    public static class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        public MainRecyclerViewAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.main_recyclerview_row, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String animal = mData.get(position);
            holder.myTextView.setText(animal);
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.textView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // convenience method for getting data at click position
        String getItem(int id) {
            return mData.get(id);
        }

        // allows clicks events to be caught
        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

        // parent activity will implement this method to respond to click events
        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }

}