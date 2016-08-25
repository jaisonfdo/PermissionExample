package droidmentor.permissionexample;

import java.util.ArrayList;

/**
 * Created by Jaison on 25/08/16.
 */


interface PermissionResultCallback
{
    void PermissionGranted(int request_code);
    void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);
    void PermissionDenied(int request_code);
    void NeverAskAgain(int request_code);
}
