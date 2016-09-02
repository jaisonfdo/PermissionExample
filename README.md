
This is a sample Android application to show how to get <B> Multiple Permissions in one Go in Android M</B>

Basically two files are needed to get permissions. One file is used to check and request the needed permission 
and the other one to send the callback.

<b>PermissionUtils.java</b> <br><br> Used to check and request the needed permission from the user.<br><br>

<b>PermissionResultCallback.java</b> <br><br> It's an interface,which gives the status of the request.

<pre>
interface PermissionResultCallback
{
    void PermissionGranted(int request_code);
    void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);
    void PermissionDenied(int request_code);
    void NeverAskAgain(int request_code);
}
</pre>

Getting permissions from the user is simple,First add all the needed permissions in a list

<pre>

 ArrayList<String> permissions=new ArrayList<>();

 permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
 permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
 
</pre>

Create an instance for the class "PermissionUtils", and call the function <b>check_permission</b> by passing the 
permission list,why we need those permission explanation and the request code.

<pre>

 permissionUtils=new PermissionUtils(getApplicationContext());
 permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
 
</pre>

Then redirect the onRequestPermissionsResult to permissionUtils.onRequestPermissionsResult

<pre>

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        // redirects to utils

        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

</pre>

For getting the callbacks you need to include <b>PermissionResultCallback</b> interface into your activity,
and also implement the needed methods.

<pre>

public class PermissionActivity extends AppCompatActivity implements
        OnRequestPermissionsResultCallback,PermissionResultCallback


 // Callback functions


    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }
    
</pre>




For more information, check out my detailed guide here : http://droidmentor.com/multiple-permissions-in-one-go

