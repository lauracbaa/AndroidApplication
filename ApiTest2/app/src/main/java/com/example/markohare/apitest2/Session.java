/**
 * Created by Aaron Nicholl on 06/04/2017.
 */
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;


public class Session
{
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx)
    {
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("WhereWazza",Context.MODE_PRIVATE);
        editor=prefs.edit();
    }
    public void setLoggedin(boolean loggedin)
    {
        editor.putBoolean("loggedInMode",loggedin);
        editor.commit();
    }
    public boolean loggedin()
    {
        return prefs.getBoolean("loggedInMode",false);
    }
}
