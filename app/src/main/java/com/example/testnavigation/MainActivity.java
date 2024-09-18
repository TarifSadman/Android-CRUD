package com.example.testnavigation;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.testnavigation.ui.home.HomeFragment;
import com.example.testnavigation.ui.notifications.NotificationsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.badge.BadgeDrawable;

public class MainActivity extends AppCompatActivity {

    private NotificationsViewModel notificationsViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            bottomNavigationView = findViewById(R.id.nav_view);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Pass BottomNavigationView to HomeFragment
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.navigation_home) {
                    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_home);
                    if (homeFragment != null) {
                        homeFragment.setBottomNavigationView(bottomNavigationView);
                    }
                } else if (destination.getId() == R.id.navigation_notifications) {
                    // Reset badge count when navigating to NotificationsFragment
                    resetNotificationBadge();
                }
            });
        } else {
            throw new IllegalStateException("NavHostFragment is not found in the layout");
        }

        // Initialize NotificationsViewModel
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        // Observe notifications count
        notificationsViewModel.getNotifications().observe(this, notifications -> {
            if (bottomNavigationView != null && bottomNavigationView.getMenu() != null) {
                updateNotificationBadge(notifications.size());
            }
        });

        // Handle the back press using OnBackPressedDispatcher
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    if (navController.getCurrentDestination() != null && navController.getCurrentDestination().getId() == R.id.navigation_dashboard) {
                        navController.navigate(R.id.navigation_home);
                    } else {
                        setEnabled(false);
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void updateNotificationBadge(int count) {
        if (bottomNavigationView != null) {
            BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
            badge.setVisible(count > 0);
            badge.setNumber(count);
        }
    }

    private void resetNotificationBadge() {
        if (bottomNavigationView != null) {
            BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
            if (badge != null) {
                badge.setVisible(false);
            }
        }
    }

}
