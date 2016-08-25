package droidmentor.permissionexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaison on 25/08/16.
 */


public class PermissionUtils
{

    Context context;
    Activity current_activity;

    PermissionResultCallback permissionResultCallback;


    ArrayList<String> permission_list=new ArrayList<>();
    ArrayList<String> listPermissionsNeeded=new ArrayList<>();
    String dialog_content="";
    int req_code;

    public PermissionUtils(Context context)
    {
        this.context=context;
        this.current_activity= (Activity) context;

        permissionResultCallback= (PermissionResultCallback) context;
    }


    public void check_permission(ArrayList<String> permissions, String dialog_content, int request_code)
    {
        this.permission_list=permissions;
        this.dialog_content=dialog_content;
        this.req_code=request_code;

       if(Build.VERSION.SDK_INT >= 23)
       {
           if (checkAndRequestPermissions(permissions, request_code))
           {
               permissionResultCallback.PermissionGranted(request_code);
               Log.i("all permissions", "granted");
               Log.i("proceed", "to callback");
           }
       }
        else
       {
           permissionResultCallback.PermissionGranted(request_code);

           Log.i("all permissions", "granted");
           Log.i("proceed", "to callback");
       }

    }


    private  boolean checkAndRequestPermissions(ArrayList<String> permissions,int request_code) {

        if(permissions.size()>0)
        {
            listPermissionsNeeded = new ArrayList<>();

            for(int i=0;i<permissions.size();i++)
            {
                int hasPermission = ContextCompat.checkSelfPermission(current_activity,permissions.get(i));

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }

            }

            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions(current_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),request_code);
                return false;
            }
        }

        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                 if(grantResults.length>0)
                 {
                     Map<String, Integer> perms = new HashMap<>();

                     for (int i = 0; i < permissions.length; i++)
                     {
                         perms.put(permissions[i], grantResults[i]);
                     }

                     final ArrayList<String> pending_permissions=new ArrayList<>();

                     for (int i = 0; i < listPermissionsNeeded.size(); i++)
                     {
                         if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED)
                         {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(current_activity,listPermissionsNeeded.get(i)))
                                    pending_permissions.add(listPermissionsNeeded.get(i));
                            else
                            {
                                Log.i("Go to settings","and enable permissions");
                                permissionResultCallback.NeverAskAgain(req_code);
                                Toast.makeText(current_activity, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                return;
                            }
                         }

                     }

                     if(pending_permissions.size()>0)
                     {
                         showMessageOKCancel(dialog_content,
                                 new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {

                                         switch (which) {
                                             case DialogInterface.BUTTON_POSITIVE:
                                                 check_permission(permission_list,dialog_content,req_code);
                                                 break;
                                             case DialogInterface.BUTTON_NEGATIVE:
                                                 Log.i("permisson","not fully given");
                                                 if(permission_list.size()==pending_permissions.size())
                                                     permissionResultCallback.PermissionDenied(req_code);
                                                 else
                                                     permissionResultCallback.PartialPermissionGranted(req_code,pending_permissions);
                                                 break;
                                         }


                                     }
                                 });

                     }
                     else
                     {
                        Log.i("all","permissions granted");
                        Log.i("proceed","to next step");
                         permissionResultCallback.PermissionGranted(req_code);

                     }



                 }
                 break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(current_activity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

}
