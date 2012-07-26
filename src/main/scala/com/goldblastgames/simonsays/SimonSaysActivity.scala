package com.goldblastgames.simonsays

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast

import reactive.BufferSignal
import reactive.EventSource
import reactive.EventStream
import reactive.Observing
import reactive.Signal
import reactive.Timer
import reactive.Val
import reactive.Var

import scala.util.Random

/**
 * Main activity demonstrating the usage of Android, Scala, and Reactive in one project.
 *
 * Note: The Observing trait is required in all classes that use the [[EventSource#foreach]] method.
 */
class SimonSaysActivity extends Activity with TypedActivity with Observing {
  val handler = new Handler()
  val score: Var[Int] = Var(1)
  val expected: BufferSignal[Int] = BufferSignal(1)
  val actual: BufferSignal[Int] = BufferSignal()
  val displayIndex: Var[Int] = Var(0)
  val btn1Light: Signal[Boolean] = displayIndex.map(expected.value.apply(_) == 1).distinct
  val btn2Light: Signal[Boolean] = displayIndex.map(expected.value.apply(_) == 2).distinct
  val btn3Light: Signal[Boolean] = displayIndex.map(expected.value.apply(_) == 3).distinct
  val btn4Light: Signal[Boolean] = displayIndex.map(expected.value.apply(_) == 4).distinct
  val interactive: Var[Boolean] = Var(false)
  var timer: Timer = new Timer(0L, 2000L)
  val timerOff: EventSource[Long] = new EventSource[Long]() { }
  val displayUpdate: EventStream[Long] = interactive.distinct.flatMap {
    case false => timer
    case true => timerOff
  }
  val gameEnd: EventStream[Boolean] = actual.change.collect {
    case current if expected.value equals current => true
    case current if !(expected.value startsWith current) => false
  }

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.simonsays)
    setButtonStatus(false)

    // Bad design - this should actually be done via functional mapping,
    // not with an imperative foreach.
    interactive.distinct.change.foreach {
      case false => {
        setButtonStatus(false)
        displayIndex.update(0)
      }
      case true => {
        setButtonStatus(true)
        lightButton(R.id.button1, Color.RED)
        lightButton(R.id.button2, Color.BLUE)
        lightButton(R.id.button3, Color.GREEN)
        lightButton(R.id.button4, Color.YELLOW)
      }
    }

    gameEnd.foreach {
      case true => {
        toast("Victory!")
        // Restart game
        score.value += 1
        expected.value += randomButton
        actual.update(Seq())
        interactive.update(false)
      }
      case false => {
        setButtonStatus(false)
        toast("You Lose :D")
      }
    }

    btn1Light.foreach { lit =>
      handle {
        lit match {
          case true => lightButton(R.id.button1, Color.WHITE)
          case false => lightButton(R.id.button1, Color.RED)
        }
      }
    }

    btn2Light.foreach { lit =>
      handle {
        lit match {
          case true => lightButton(R.id.button2, Color.WHITE)
          case false => lightButton(R.id.button2, Color.BLUE)
        }
      }
    }

    btn3Light.foreach { lit =>
      handle {
        lit match {
          case true => lightButton(R.id.button3, Color.WHITE)
          case false => lightButton(R.id.button3, Color.GREEN)
        }
      }
    }

    btn4Light.foreach { lit =>
      handle {
        lit match {
          case true => lightButton(R.id.button4, Color.WHITE)
          case false => lightButton(R.id.button4, Color.YELLOW)
        }
      }
    }

    displayUpdate.foreach(
      time => handle {
        if (score.now == displayIndex.now + 1) {
          interactive.update(true)
        } else {
          displayIndex.value += 1
        }
      }
    )
  }

  def randomButton = Random.nextInt(4)

  def handle(fn: => Unit) {
    handler.post(new RunnableFn(fn))
  }

  def lightButton(id: Int, color: Int) {
    findViewById(id).setBackgroundColor(color)
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
