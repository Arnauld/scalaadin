package scalaadin

import com.vaadin.Application 
import com.vaadin.ui._
import java.util.Date 

class ScalaadinApplication extends Application { 
    override def init(): Unit = { 
        val mainWindow = new Window("Scalaadin Application")
        val label = new Label("Hello World")
        mainWindow.addComponent(label) 
        mainWindow.addComponent(
        new Button("What is the time?", 
            new Button.ClickListener() { 
                def buttonClick(event: Button#ClickEvent) { 
                    mainWindow.showNotification("The time is " + new Date()) 
            }}))
	    setMainWindow(mainWindow)		
   } 
}