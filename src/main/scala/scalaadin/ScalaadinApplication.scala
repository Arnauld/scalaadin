package scalaadin

import com.vaadin.Application 
import com.vaadin.ui._
import java.util.Date 

class ScalaadinApplication extends Application { 
    override def init() {
        val mainWindow = new Window("Scalaadin Application")
        val label = new Label("Hello World")

        val s = new org.vaadin.sparklines.Sparklines("Stuff", 0, 0, 0, 100);
        s.setDescription("Everything turned on");
        s.setValue("15,26,23,56,35,37,21"); 
        s.setAverageVisible(true);
        s.setNormalRangeColor("#444"); 
        s.setNormalRangeMax(20);
        s.setNormalRangeMin(25);
        s.setNormalRangeVisible(true);
        s.setMaxColor("#f69");
        s.setMinColor("#6f9");

        mainWindow.addComponent(s);
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