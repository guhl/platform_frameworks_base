package android.widget;

import android.view.View;

/**
 * {@hide}
 */
public abstract class AppSecurityPermissionsBase {
    public abstract View getPermissionsView();

    public abstract int getPermissionCount();
    
    public abstract View getPermissionsViewWithRevokeButtons();
}
