/**
 
 <h2>Overview</h2>
 
 This package contains all the classes and interfaces you have to interact with.
 
 <h2>Initialization</h2>
 
 Before using it, the API must be initialized:
 <pre>
     import org.gamepad4j.Controllers;
     ...
     Controllers.initialize();
 </pre>
 Once the game exits, the API should be properly terminated in order
 to release all resources by invoking
 <pre>
     Controllers.shutdown();
 </pre>
 
 <h3>Log level override</h3>
 
 Note that by default, the API does not log at all. See {@ILog} for 
 information on how to implement and set your own custom log adapter.
 But for simply enabling logging to stdout/stderr, invoke this method
 BEFORE invoking <code>Controllers.initialize()</code>:
 <pre>
     import org.gamepad4j.util.Log;
     ... 
     Log.initialize(Log.LogLevel.DEBUG);
 </pre>
 Or whatever log level you want to use.
 
 <h2>Query controller status</h2>

 Pull the status of the currently attached controllers:
 <pre>
     Controllers.checkControllers();
     IController[] controllers = Controllers.getControllers();
 </pre>
 
 <h2>Handle digital buttons</h2>
 
 To query the status of a certain button, use the corresponding constant:
 <pre>
     IButton button = controllers[0].getButton(ButtonID.FACE_DOWN);
     if(button.isPressed()) {
         // perform action...
     }
 </pre>
 In a game GUI, it is often not desirable to have constant button press
 events. Pressing a button should only be detected once, until it is released
 again (in order to prevent the user from activating several menu options 
 subsequently). For this, use the following method:
 <pre>
     if(button.isPressedOnce()) {
         // Processed only once, until button is released and pressed again
     }
 </pre>
 It is also possible to just retrieve an array of all available buttons,
 which may include some for which no mapping to a constant exists:
 <pre>
     IButton[] buttons = controllers[0].getButtons();
 </pre>
  
 <h2>Handle triggers</h2>
 
 <h2>Handle analog sticks</h2>
 
 <h2>Handle the D-pad</h2>
 The direction on the D-pad currently pressed by the player can be queried directly:
 <pre>
     DPadDirection direction = controllers[0].getDpadDirection();
     if(direction == DpadDirection.UP_RIGHT) {
         // move spaceship upwards, to the right...
     }
 </pre>
 Similar to the buttons, there is an option to process a D-pad event only once, until
 the user releases the D-pad and presses it again:
 <pre>
     DPadDirection direction = controllers[0].getDpadDirectionOnce();
     if(direction == DpadDirection.DOWN) {
         // select menu option below...
     }
 </pre>
 
 */
package org.gamepad4j;