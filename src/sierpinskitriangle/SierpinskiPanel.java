/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sierpinskitriangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author harlan.howe
 */
public class SierpinskiPanel extends JPanel {
        private int myDepth;

        private BufferedImage myCanvas; // an offscreen image where we'll do the drawing and occasionally copy to screen.
        private final Object myCanvasMutex; // a lock to make sure only one thing uses myCanvas at a time.

        private final TriangleThread drawingThread; // the portion of code that will do the drawing simultaneously with
                                              // occasional screen updates.

        public SierpinskiPanel()
        {
            super();
            myDepth = 1;
            myCanvasMutex = new Object();
            drawingThread = new TriangleThread();
            drawingThread.start();
        }

    /**
     * sets the total depth of the drawing, as a whole.
     * @param depth - how many steps of iteration you should go before drawing a line.
     */
    public void setDepth(int depth)
        {
            if (depth>0)
            {
                myDepth = depth;
                System.out.println("Setting depth to "+myDepth+".");
                drawingThread.interrupt();
                drawingThread.startDrawing();
            }

        }
        
        public void paintComponent(Graphics g)
        {
            // Note: There are two thread both potentially trying to use "myCanvas" at the same time. This can
            //      cause problems, so we use a "mutex" to ensure that only one accesses it at any given moment.

            synchronized (myCanvasMutex) // wait until myCanvas is available, then lock it for my use....
            {
                if (myCanvas != null)
                    g.drawImage(myCanvas,0,0,this);
            } // now I'm done with myCanvas, so I am unlocking the mutex.
        }

        class TriangleThread extends Thread
        {
            private boolean needsRestart;
            private boolean shouldInterrupt;

            public TriangleThread()
            {
                needsRestart = true;
                shouldInterrupt = true;
            }

            public void interrupt()
            {
                shouldInterrupt = true;
            }

            public void startDrawing()
            {
                needsRestart = true;
            }

            public void run() // this is what gets called when we say "start()." Never call this method directly.
            {
                while(true) // loop forever
                {
                    if (needsRestart & getHeight() > 5 & getWidth() > 5) // if we need to restart and the window has non-zero size
                    {
                        System.out.println("Making new canvas.");
                        // Note: There are two thread both potentially trying to use "myCanvas" at the same time. This
                        //       can cause problems, so we use a "mutex" to ensure that only one accesses it at any
                        //       given moment.
                        synchronized (myCanvasMutex) // wait for myCanvas to be free and lock it for my use.
                        {
                            myCanvas = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
                            Graphics g = myCanvas.getGraphics();
                            g.setColor(Color.BLUE);
                            g.fillRect(0,0,getWidth(),getHeight());
                        } // ok. I'm done with myCanvas for now, unlock the mutex.

                        // draw the triangle - the points given are the corners of the outside of the triangle.
                        drawSierpinskiTriangle(getWidth()/2,10,    10, getHeight()-10,       getWidth()-10, getHeight()-10,    myDepth);
                        if (!shouldInterrupt)
                            needsRestart = false; // we're done and didn't get interrupted.
                    }
                    try
                    {
                        Thread.sleep((250)); // wait for 1/4 second before checking whether to draw again.
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    shouldInterrupt = false;
                }
            }

            /**
             * Recursive method to draw the Sierpinski Triangle. Takes the coordinates of the corners of the triangle
             * to draw and a counter variable that indicates how many iterations _this_ call is from the base case.
             * Note that the only drawing takes place within the base case, itself... none of the nan-base case calls
             * should draw anything.
             * @param x1 - x for point 1
             * @param y1 - y for point 1
             * @param x2 - x for point 2
             * @param y2 - y for point 2
             * @param x3 - x for point 3
             * @param y3 - y for point 3
             * @param recursionsToGo - how far away is _this_ call of drawSierpinskiTriangle() from the base case?
             */
            public void drawSierpinskiTriangle(double x1, double y1,
                                               double x2, double y2,
                                               double x3, double y3,
                                               int recursionsToGo)
            {
                if (shouldInterrupt) // bail out quickly....
                    return;

                            // TODO: you should have a base case and a case in which you continue the recursion,
                            //  one step closer to the base case.
                if (1 == 1) //  Replace this condition with one that actually makes sense to detect the base case.
                {
                    // --> --> --> --> NOTE:You should ONLY draw a triangle in the base case! To do so, you will need
                    // something like this...

                    // Note: There are two thread both potentially trying to use "myCanvas" at the same time. This can
                    //      cause problems, so we use a "mutex" to ensure that only one accesses it at any given moment.
                    synchronized (myCanvasMutex) // wait until myCanvas is free, then lock it for my use...
                    {
                        Graphics myCanvas_g = myCanvas.getGraphics();
                        myCanvas_g.setColor(Color.white);

                        // TODO: ..... and draw the triangle via myCanvas_g.drawLine() or myCanvas_g.drawPolygon() here.
                        //  (The myCanvas_g is a graphics object, just like the "g" you're familiar with, but it draws
                        //  on an offscreen canvas that will be periodically copied to the screen.
                        //  Reminder: you'll need to typecast the doubles to ints to draw.
                        myCanvas_g.drawLine(100,100,200,200); // replace this line!


                    } // k, now I'm done with myCanvas for now. Release it.
                    repaint();
                } else // if it isn't the base case...
                {
                   // TODO - you write this part, the non-base case, which should involve recursive calls!
                   // Note: It does not contain any drawing commands.


                }

            }
        }
}
