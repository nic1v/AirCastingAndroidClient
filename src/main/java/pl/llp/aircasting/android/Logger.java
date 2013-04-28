package pl.llp.aircasting.android;

import android.util.Log;

import static pl.llp.aircasting.util.Constants.TAG;

/**
 * Created by ags on 14/04/2013 at 00:00
 */
public class Logger
{
  public static void w(String message, Exception e)
  {
    Log.w(TAG, message, e);
  }

  public static void e(String message, Exception e)
  {
    Log.e(TAG, message, e);
  }

  public static void i(String message)
  {
    Log.i(TAG, message);
  }

  public static void e(String msg)
  {
    Log.e(TAG, msg);
  }

  public static void w(String message)
  {
    Log.w(TAG, message);
  }

  public static void d(String message)
  {
    Log.d(TAG, message);
  }

  public static void d(String message, Throwable throwable)
  {
    Log.d(TAG, message, throwable);
  }
}
