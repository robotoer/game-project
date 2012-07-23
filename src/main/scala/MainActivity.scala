package com.gameproject

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import reactive.EventSource
import reactive.Observing

/**
 * Main activity demonstrating the usage of Android, Scala, and Reactive in one project.
 *
 * Note: The Observing trait is required in all classes that use the [[EventSource#foreach]] method.
 */
class MainActivity extends Activity with TypedActivity with Observing {
  // Setup an EventSource (in an actual project a real subtype of EventSource should be used instead).
  val es: EventSource[String] = new EventSource[String] { }

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)

    findView(TR.textview).setText("hello, world!")
    es.foreach(msg => Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show())
  }

  /**
   * Observer for button. This fires an event for the test event source.
   *
   * @param view The view that caused this event.
   */
  def buttonObserver(view: View) {
    es fire "foo msg!"
  }
}
