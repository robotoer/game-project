package com.goldblastgames.simonsays.tests

import com.goldblastgames.simonsays._
import junit.framework.Assert._
import android.test.AndroidTestCase
import android.test.ActivityInstrumentationTestCase2

class AndroidTests extends AndroidTestCase {
  def testPackageIsCorrect() {
    assertEquals("com.goldblastgames.simonsays", getContext.getPackageName)
  }
}

class ActivityTests extends ActivityInstrumentationTestCase2(classOf[SimonSaysActivity]) {
   def testHelloWorldIsShown() {
//      val activity = getActivity
//      val textview = activity.findView(TR.textview)
//      assertEquals(textview.getText, "hello, world!")
    }
}
