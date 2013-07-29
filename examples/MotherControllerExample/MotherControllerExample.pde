import controlP5.*;
import oscP5.*;
import netP5.*;
  
OscP5 oscP5;
NetAddress myRemoteLocation;

ControlP5 cp5;
int myColor = color(0,0,0);
int fontSize = 20;

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
  
  controlP5.Button b = cp5.addButton("AddGradient")
     .setValue(0)
     .setPosition(10,10)
     .setSize(200,30)
     ;

    b.setCaptionLabel("Add Gradient")
     .getCaptionLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;
     
  b = cp5.addButton("AddRotatingArcs")
     .setValue(0)
     .setPosition(10,41)
     .setSize(200,30)
     ;

    b.setCaptionLabel("Add Rotating Arcs")
     .getCaptionLabel()
     .setFont(font)
     .toUpperCase(false)
     .setSize(fontSize)
     ;

  // add a vertical slider
  controlP5.Slider sl = cp5.addSlider("Slider")
     .setPosition(100,105)
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
//  println(theEvent.getController().getName());
}

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

