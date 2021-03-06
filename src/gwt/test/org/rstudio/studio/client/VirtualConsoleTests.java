/*
 * VirtualConsoleTests.java
 *
 * Copyright (C) 2009-17 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client;

import org.rstudio.core.client.VirtualConsole;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.junit.client.GWTTestCase;

import junit.framework.Assert;

public class VirtualConsoleTests extends GWTTestCase
{
   @Override
   public String getModuleName()
   {
      return "org.rstudio.studio.RStudioTests";
   }

   public void testSimpleText()
   {
      String simple = VirtualConsole.consolify("foo");
      Assert.assertEquals("foo", simple);
   }
   
   public void testBackspace()
   {
      String backspace = VirtualConsole.consolify("bool\bk");
      Assert.assertEquals("book", backspace);
   }
   
   public void testCarriageReturn()
   {
      String cr = VirtualConsole.consolify("hello\rj");
      Assert.assertEquals("jello", cr);
   }
   
   public void testNewlineCarrigeReturn()
   {
      String cr = VirtualConsole.consolify("L1\nL2\rL3");
      Assert.assertEquals("L1\nL3", cr);
   }
   
   public void testSimpleColor()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("Error", "error");
      Assert.assertEquals(
            "<span class=\"error\">Error</span>", 
            ele.getInnerHTML());
   }
   
   public void testTwoColors()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("Output 1", "one");
      vc.submit("Output 2", "two");
      Assert.assertEquals(
            "<span class=\"one\">Output 1</span>" + 
            "<span class=\"two\">Output 2</span>",
            ele.getInnerHTML());
   }
   
   public void testColorOverwrite()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("XXXX\r", "X");
      vc.submit("YY", "Y");
      Assert.assertEquals(
            "<span class=\"Y\">YY</span>" + 
            "<span class=\"X\">XX</span>",
            ele.getInnerHTML());
   }
   
   public void testColorSplit()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("123456");
      vc.submit("\b\b\b\bXX", "X");
      Assert.assertEquals(
            "<span>12</span>" + 
            "<span class=\"X\">XX</span>" + 
            "<span>56</span>",
            ele.getInnerHTML());
   }
   
   public void testColorOverlap()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("123", "A");
      vc.submit("456", "B");
      vc.submit("\b\b\b\bXX", "X");
      Assert.assertEquals(
            "<span class=\"A\">12</span>" + 
            "<span class=\"X\">XX</span>" + 
            "<span class=\"B\">56</span>",
            ele.getInnerHTML());
   }
   
   public void testFormFeed()
   {
      PreElement ele = Document.get().createPreElement();
      VirtualConsole vc = new VirtualConsole(ele);
      vc.submit("Sample1\n");
      vc.submit("Sample2\n");
      vc.submit("Sample3\f");
      vc.submit("Sample4");
      Assert.assertEquals("<span>Sample4</span>", ele.getInnerHTML());
   }
}
