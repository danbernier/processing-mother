import controlP5.*;
import oscP5.*;
import netP5.*;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

int fontSize = 18;
ControlP5 cp5;
int myColor = color(0,0,0);

ColorPicker cpTop, cpBottom;
Textlabel myTextlabelCP;
controlP5.Button b;

void setup() {
  size(700,400);
  noStroke();
  
  /* start oscP5, listening for incoming messages at port 12000 */
  oscP5 = new OscP5(this,5432);
  
  /* myRemoteLocation is a NetAddress. a NetAddress takes 2 parameters,
   * an ip address and a port number. myRemoteLocation is used as parameter in
   * oscP5.send() when sending osc packets to another computer, device, 
   * application. usage see below. for testing purposes the listening port
   * and the port of the remote location address are the same, hence you will
   * send messages back to this sketch.
   */
  myRemoteLocation = new NetAddress("127.0.0.1",7000);
  
  cp5 = new ControlP5(this);

  PFont pfont = createFont("Arial",20,true); // use true/false for smooth/no-smooth
  ControlFont font = new ControlFont(pfont,241);
  
  // Add Gradient Button
  b = cp5.addButton("AddGradient")
     .setValue(0)
     .setPosition(10,10)
     .setSize(255,30)
     ;

  b.setCaptionLabel("Add Gradient")
     .getCaptionLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;
     
  // Add Rotating Arcs Button
  b = cp5.addButton("AddRotatingArcs")
     .setValue(0)
     .setPosition(10,41)
     .setSize(255,30)
     ;

  b.setCaptionLabel("Add Rotating Arcs")
     .getCaptionLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;

 // Gradient Top Color controls
 myTextlabelCP = cp5.addTextlabel("topColorLabel")
                    .setText("Gradient top color")
                    .setPosition(10,72)
                    .setFont(pfont)
                    ;

 cpTop = cp5.addColorPicker("topPicker")
     .setPosition(10, 100)
     .setColorValue(color(0, 0, 255, 255))
     ;
     
// Gradient Bottom Color controls
 myTextlabelCP = cp5.addTextlabel("botColorLabel")
                    .setText("Gradient bottom color")
                    .setPosition(10,163)
                    .setFont(pfont)
                    ;

 cpBottom = cp5.addColorPicker("botPicker")
     .setPosition(10, 194)
     .setColorValue(color(0, 0, 0, 255))
     ;

  // add a vertical slider
  controlP5.Slider sl = cp5.addSlider("Slider")
     .setPosition(300,105)
     .setSize(20,200)
     .setRange(0,200)
     .setValue(128)
     ;
     
     sl.getCaptionLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;
     
     sl.getValueLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;

  // reposition the Label for controller 'slider'
  //cp5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
  //cp5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0); 
}

void draw() {
  background(0);
  
  fill(myColor);
  rect(0,280,width,70);
}

public void controlEvent(ControlEvent theEvent) {
  // when a value change from a ColorPicker is received, extract the ARGB values
  // from the controller's array value
  if(theEvent.isFrom(cpTop)) {
    float r = int(theEvent.getArrayValue(0));
    float g = int(theEvent.getArrayValue(1));
    float b = int(theEvent.getArrayValue(2));
        
    OscMessage myMessage;
  
    myMessage = new OscMessage("/Mother/Child/Grad_01/TopRed"); 
    myMessage.add(r/255f);
    oscP5.send(myMessage, myRemoteLocation);
  
    myMessage = new OscMessage("/Mother/Child/Grad_01/TopGreen");
    myMessage.add(g/255f);
    oscP5.send(myMessage, myRemoteLocation);

    myMessage = new OscMessage("/Mother/Child/Grad_01/TopBlue");
    myMessage.add(b/255f);
    oscP5.send(myMessage, myRemoteLocation);
  }
  else if(theEvent.isFrom(cpBottom)) {
    float r = int(theEvent.getArrayValue(0));
    float g = int(theEvent.getArrayValue(1));
    float b = int(theEvent.getArrayValue(2));
        
    OscMessage myMessage;
  
    myMessage = new OscMessage("/Mother/Child/Grad_01/BotRed"); 
    myMessage.add(r/255f);
    oscP5.send(myMessage, myRemoteLocation);
  
    myMessage = new OscMessage("/Mother/Child/Grad_01/BotGreen");
    myMessage.add(g/255f);
    oscP5.send(myMessage, myRemoteLocation);

    myMessage = new OscMessage("/Mother/Child/Grad_01/BotBlue");
    myMessage.add(b/255f);
    oscP5.send(myMessage, myRemoteLocation);
  }
}

/*
// color information from ColorPicker 'picker' are forwarded to the picker(int) function
void topPicker(int col) {
  OscMessage myMessage;
  
  myMessage = new OscMessage("/Mother/Child/Grad_01/TopRed");
  float topRed = red(col); 
  myMessage.add(topRed/255f);
  oscP5.send(myMessage, myRemoteLocation);
  
  myMessage = new OscMessage("/Mother/Child/Grad_01/TopGreen");
  float topGreen = green(col);
  myMessage.add(topGreen/255f);
  oscP5.send(myMessage, myRemoteLocation);

  myMessage = new OscMessage("/Mother/Child/Grad_01/TopBlue");
  float topBlue = blue(col);
  myMessage.add(topBlue/255f);
  oscP5.send(myMessage, myRemoteLocation);
}
*/

void slider(float theColor) {
  myColor = color(theColor);
  println("a slider event. setting background to "+theColor);
}

// function AddGradient will receive changes from 
// controller with name AddGradient
public void AddGradient(int theValue) {
  OscMessage myMessage = new OscMessage("/Mother/Add_synth");
  myMessage.add("Gradient");
  myMessage.add("Grad_01");
  oscP5.send(myMessage, myRemoteLocation);
}

// function AddRotatingArcs will receive changes from 
// controller with name AddRotatingArcs
public void AddRotatingArcs(int theValue) {
  OscMessage myMessage = new OscMessage("/Mother/Add_synth");
  myMessage.add("RotatingArcs");
  myMessage.add("Arcs_01");
  oscP5.send(myMessage, myRemoteLocation);
}

/* incoming osc message are forwarded to the oscEvent method. */
void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  print("### received an osc message.");
  print(" addrpattern: "+theOscMessage.addrPattern());
  println(" typetag: "+theOscMessage.typetag());
}

