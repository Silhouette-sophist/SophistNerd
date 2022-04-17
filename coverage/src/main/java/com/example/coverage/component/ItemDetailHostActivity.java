package com.example.coverage.component;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.coverage.R;
import com.example.coverage.databinding.ActivityItemDetailBinding;
import com.example.coverage.util.FileHelper;
import com.example.coverage.util.JacocoHelper;
import com.google.android.material.snackbar.Snackbar;

public class ItemDetailHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityItemDetailBinding binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_item_detail);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.
                Builder(navController.getGraph())
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "导出jacoco数据", Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] bytes = JacocoHelper.getInstance().outputJacocoCoverage();
                        System.out.println("coverage bytes === " + bytes.length);
                        String filePath = FileHelper.writeCoverage(bytes);
                        System.out.println("coverage filePath === " + filePath);
                    }
                }).start();

                Snackbar.make(binding.fab, "启动服务", Snackbar.LENGTH_LONG)
                        .setAction("启动", new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setClass(view.getContext(), MyService.class);
                                startService(intent);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_item_detail);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}