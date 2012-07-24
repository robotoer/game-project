package com.goldblastgames.simonsays

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast

import reactive.BufferSignal
import reactive.EventSource
import reactive.EventStream
import reactive.Observing
import reactive.Timer
//import reactive.Var

/**
 * Main activity demonstrating the usage of Android, Scala, and Reactive in one project.
 *
 * Note: The Observing trait is required in all classes that use the [[EventSource#foreach]] method.
 */
class SimonSaysActivity extends Activity with TypedActivity with Observing {
  val handler = new Handler()
//  val score: Var[Int] = Var(1)
  val expected: BufferSignal[Int] = BufferSignal(1, 2, 3, 4)
  val actual: BufferSignal[Int] = BufferSignal[Int]()
  val gameEnd: EventStream[Boolean] = actual.change.collect {
    case current if expected.value equals current => true
    case current if !(expected.value startsWith current) => false
  }

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.simonsays)

    findViewById(R.id.button1).setBackgroundColor(android.graphics.Color.RED)
    findViewById(R.id.button2).setBackgroundColor(android.graphics.Color.BLUE)
    findViewById(R.id.button3).setBackgroundColor(android.graphics.Color.GREEN)
    findViewById(R.id.button4).setBackgroundColor(android.graphics.Color.YELLOW)

    gameEnd foreach {
      case true => toast("Victory!")
      case false => toast("You Lose :(")
    }
    gameEnd foreach { _ => setButtonStatus(false) }

//    val interval = 5000L
//    val timer = new Timer(0L, interval, _ >= expected.value.size * interval)
//    timer.foreach(_ => handle {
//    timer.foreach(time => handle { toast("Hello from a timer") })
  }

  def handle(fn: => Unit) {
    handler.post(new RunnableFn(fn))
  }

  /**
   * Opens a toast notification.
   *
   * @param message The message to display on the Toast notification.
   */
  def toast(message: String) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show()
  }

  /**
   * Sets whether or not the game buttons are enabled or not.
   *
   * @param enabled If this is true, this will enable the game buttons.
   */
  def setButtonStatus(enabled: Boolean) {
    findViewById(R.id.button1).setEnabled(enabled)
    findViewById(R.id.button2).setEnabled(enabled)
    findViewById(R.id.button3).setEnabled(enabled)
    findViewById(R.id.button4).setEnabled(enabled)
  }

  // Observers for interfacing with the Android SDK.
  def button1Observer(view: View): Unit = actual.value += 1
  def button2Observer(view: View): Unit = actual.value += 2
  def button3Observer(view: View): Unit = actual.value += 3
  def button4Observer(view: View): Unit = actual.value += 4
}
