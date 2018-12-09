package dk.au.itsmap.group4.crispy.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

public interface INavigationController {
    NavController getNavController();

    void setToolbar(Toolbar t);

    void setMainToolbarWithNavigation(String title);

    void setMainToolbar(String title);
}
