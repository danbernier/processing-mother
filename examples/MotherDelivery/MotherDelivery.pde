import oscP5.*;
import netP5.*;
import processing.core.*;
import mother.library.*;

/**
* Mother Main Sketch
* by Ilias Bergstrom
* 
* Run this sketch to run Mother. You will not need to customize this sketch,
* it is only provided as an easy way to start Mother.
* all settings are made in the Mother.ini file, which resides in the same folder as this
* MotherDelivery.pde.
* 
* If you get a crash, chances are that it is one of your Visual synths / sketches
* that have malfunctioned, or that you have run out of memory. Check the console for 
* hopefully informative error messages.
*
* Note that Mother depends on you having installed the oscP5 library in your library folder!
*/

Mother m_Mother;

/**
* Overriding this is not really supposed to be done in normal Processing sketches,
* but it is in this case necessary for Mother to work correctly.
*/
void init() {
  m_Mother = new Mother(this);
	
  m_Mother.init();
		
  super.init();
}
	
void setup() {	
  m_Mother.setup();
}
	
void draw() {
  m_Mother.draw();
}
	
void keyPressed() {
  m_Mother.keyPressed();
}