package com.example.asm2_applicationdevelopment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    Toolbar toolbar;
    FragmentManager fragmentManager;

    private String username, password, email, phone;  // Declare variables to store user data

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve user data from Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");

        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_nevigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.home) {
                    openFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.report) {
                    openFragment(new ReportFragment());
                    return true;

                } else if (itemId == R.id.budget) {
                    openFragment(new BudgetFragment());
                    return true;
                } else if (itemId == R.id.profile) {
                    openProfileFragment();  // Use a separate method to pass data
                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });
    }

    private void showBottomSheetDialog() {
        Dialog bottomSheetDialog = new Dialog(MainActivity.this);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.bottomsheetlayout);

        // Set dialog to full width and wrap content height
        bottomSheetDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Find the expense item and set click listener
        View expenseItem = bottomSheetDialog.findViewById(R.id.outcome);
        if (expenseItem != null) {
            expenseItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start AddExpenseActivity
                    Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                    startActivity(intent);
                    bottomSheetDialog.dismiss(); // Dismiss the dialog after the click
                }
            });
        }
        View incomeItem = bottomSheetDialog.findViewById(R.id.income);
        if (incomeItem != null) {
            incomeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddIncomeActivity.class);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });
        }
        View budgetItem = bottomSheetDialog.findViewById(R.id.budget);
        if (budgetItem != null) {
            budgetItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddBudgetActivity.class);
                    startActivity(intent);
                    bottomSheetDialog.dismiss();
                }
            });
        }

        bottomSheetDialog.show();
    }

    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();

        // Pass user data to the fragment using a Bundle
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        bundle.putString("email", email);
        bundle.putString("phone", phone);
        profileFragment.setArguments(bundle);

        // Open the ProfileFragment with the passed data
        openFragment(profileFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_expense) {
            openFragment(new ExpenseFragment());
        } else if (itemId == R.id.nav_income) {
            openFragment(new IncomeFragment());
        } else if (itemId == R.id.nav_budget) {
            openFragment(new BudgetFragment());
        } else if (itemId == R.id.nav_setting) {
            openFragment(new SettingFragment());
        } else if (itemId == R.id.nav_logout) {
            startActivity(new Intent(MainActivity.this, LoginAccountActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}
