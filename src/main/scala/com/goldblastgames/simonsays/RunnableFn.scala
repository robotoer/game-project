package com.goldblastgames.simonsays

class RunnableFn(fn: => Unit) extends Runnable {
  def run() { fn }
}
