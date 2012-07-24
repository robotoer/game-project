package com.goldblastgames.simonsays

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import reactive.BufferSignal
import reactive.EventSource
import reactive.Observing

/**
 * Main activity demonstrating the usage of Android, Scala, and Reactive in one project.
 *
 * Note: The Observing trait is required in all classes that use the [[EventSource#foreach]] method.
 */
class SimonSaysActivity extends Activity with TypedActivity with Observing {
  // Setup an EventSource (in an actual project a real subtype of EventSource should be used instead).
  // TODO(robert): Replace Int with a real thing, probably an enum.
  val enable: EventSource[Boolean] = new EventSource[Boolean] { }
  val actual: BufferSignal[Int] = BufferSignal()

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.simonsays)

    val expected = Seq(1, 2, 3, 4)

    // Setup reactors.
    enable.foreach { setButtonStatus(_) }
    actual.foreach { currentSeq =>
      if (expected equals currentSeq) {
        toast("Victory!")
        enable fire false
      } else if (!(expected startsWith currentSeq)) {
        toast("You lose :(")
        enable fire false
      }
    }
  }

  def setButtonStatus(enabled: Boolean) {
    findViewById(R.id.button1).setEnabled(enabled)
    findViewById(R.id.button2).setEnabled(enabled)
    findViewById(R.id.button3).setEnabled(enabled)
    findViewById(R.id.button4).setEnabled(enabled)
  }

  def toast(message: String) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show()
  }

  def button1Observer(view: View) {
    actual.value += 1
  }

  def button2Observer(view: View) {
    actual.value += 2
  }

  def button3Observer(view: View) {
    actual.value += 3
  }

  def button4Observer(view: View) {
    actual.value += 4
  }
}
