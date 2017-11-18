package com.rootonchair.phv;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;



public class DrawCanvas extends JPanel{
	public static final int CANVAS_WIDTH=420
			,CANVAS_HEIGHT=420;
	private static final int POINT_RADIUS=1;
	private static final int SPACING=20;
	private static final int NUMBER_LINE_WIDTH=7;
	private Point mPoint;
	private BufferedImage img;
	
	public DrawCanvas(){
		img=new BufferedImage(CANVAS_WIDTH,CANVAS_HEIGHT,BufferedImage.TYPE_INT_RGB);
		Graphics bfGraphic=img.getGraphics();
		drawBackground(bfGraphic);
		bfGraphic.dispose();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}
	
	public void clearCanvas(){
		Graphics g=img.getGraphics();
		drawBackground(g);
		g.dispose();
		repaint();
	}
	
	private void drawBackground(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		//g.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
		centerCanvas(g);
		g.setColor(Color.WHITE);
		g.drawLine(-CANVAS_WIDTH/2, 0, CANVAS_WIDTH/2, 0);
		g.drawLine(0, -CANVAS_HEIGHT/2, 0, CANVAS_HEIGHT/2);
		
		
		FontMetrics fontMetric=g.getFontMetrics();
		//draw horizontal lines
		int offsetY=CANVAS_HEIGHT%(SPACING*20);
		int number=10;
		int textHeight=fontMetric.getAscent();
		for (int y= offsetY/2-CANVAS_HEIGHT/2;y<=CANVAS_HEIGHT/2;y+=SPACING){
			if(y!=0){
				g.drawLine(-NUMBER_LINE_WIDTH/2,y,NUMBER_LINE_WIDTH/2,y);
				g.drawString(String.valueOf(number), 5,y+textHeight/2 );
			}
			number--;
		}

		//draw vertical line
		number=-10;
		int offsetX=CANVAS_WIDTH%(SPACING*20);
		for(int x=offsetX/2-CANVAS_WIDTH/2;x<=CANVAS_WIDTH/2;x+=SPACING){
			if(x!=0){
				g.drawLine(x, -NUMBER_LINE_WIDTH/2, x, NUMBER_LINE_WIDTH/2);
				int textWidth=fontMetric.stringWidth(String.valueOf(number));
				g.drawString(String.valueOf(number), x-textWidth/2, -5);
			}
			number++;
		}
		// draw zero
		g.drawString(String.valueOf(0), 5,fontMetric.getAscent());
		
	}
	
	public void drawPoint(int x,int y){
		if(mPoint==null)
			mPoint=new Point();
		mPoint.x=x;
		mPoint.y=y;
		if(mPoint!=null){
			Graphics g=img.getGraphics();
			//g.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
			centerCanvas(g);
			g.setColor(Color.RED);
			g.fillOval(mPoint.x-POINT_RADIUS, mPoint.y-POINT_RADIUS, 
					POINT_RADIUS*2, POINT_RADIUS*2);
			g.dispose();
			repaint();
		}
	}
	
	private void centerCanvas(Graphics g){
		g.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
	}
	public int getScale(){
		return SPACING;
	}
}