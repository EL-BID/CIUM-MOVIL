package code.yoursoft.ciummovil;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public  class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener
{
  public static interface OnItemClickListener
  {
    public void onItemClick(View view, int position);
    public void onItemLongClick(View view, int position);
  }

  private OnItemClickListener mListener;
  private GestureDetector mGestureDetector;

  public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener)
  {
    mListener = listener;

    mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
    {
      @Override
      public boolean onSingleTapUp(MotionEvent e)
      {
        return true;
      }

      @Override
      public void onLongPress(MotionEvent e)
      {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

        if(childView != null && mListener != null)
        {
          mListener.onItemLongClick(childView, recyclerView.getChildPosition(childView));
        }
      }
    });
  }

  @Override
  public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
  {
    View childView = view.findChildViewUnder(e.getX(), e.getY());

    if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
    {
      mListener.onItemClick(childView, view.getChildPosition(childView));
    }

    return false;
  }

  @Override
  public void onTouchEvent(RecyclerView view, MotionEvent motionEvent){}


  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

  }
}







/*

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;




//  Used for listening to RecyclerView item clicks. You can either implement an OnItemClickListener
 // or extend SimpleOnItemClickListener and override its methods.

//  Licence: MIT

  //@author Leo Nikkilä <hello@lnikki.la>



public abstract class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

  private OnItemClickListener listener;

  private GestureDetector gestureDetector;

  @Nullable
  private View childView;

  private int childViewPosition;

  public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
    this.gestureDetector = new GestureDetector(context, new GestureListener());
    this.listener = listener;
  }

  @Override
  public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent event) {
    childView = view.findChildViewUnder(event.getX(), event.getY());
    childViewPosition = view.getChildPosition(childView);

    return childView != null && gestureDetector.onTouchEvent(event);


  }

  @Override
  public void onTouchEvent(RecyclerView view, MotionEvent event) {
    // Not needed.
  }


  public interface OnItemClickListener {


    public void onItemClick(View childView, int position);


    public void onItemLongPress(View childView, int position);

  }

  public static abstract class SimpleOnItemClickListener implements OnItemClickListener {


    public void onItemClick(View childView, int position) {
      // Do nothing.
    }

    public void onItemLongPress(View childView, int position) {
      // Do nothing.
    }

  }

  protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
      if (childView != null) {
        listener.onItemClick(childView, childViewPosition);
      }

      return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
      if (childView != null) {
        listener.onItemLongPress(childView, childViewPosition);
      }
    }

    @Override
    public boolean onDown(MotionEvent event) {
      return true;
    }

  }

}


*/






/*
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public abstract class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener
{
  private OnItemClickListener mListener;

  public interface OnItemClickListener
  {
    public void onItemClick(View view, int position);
  }

  GestureDetector mGestureDetector;

  public RecyclerItemClickListener(Context context, OnItemClickListener listener)
  {
    mListener = listener;
    mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
    {
      @Override public boolean onSingleTapUp(MotionEvent e)
      {
        return true;
      }
    });
  }

  @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e)
  {
    View childView = view.findChildViewUnder(e.getX(), e.getY());
    if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e))
    {
      mListener.onItemClick(childView, view.getChildPosition(childView));
    }
    return false;
  }

  @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }


}


*/