package domain.mstest;

import xmlcomponents.Jocument;
import xmlcomponents.Jode;

public class TestSettings {

   private String name;
   private String id;
   
   private Deployment deployment;
   public TestSettings(){}
   public TestSettings(Jode j) {
      name = j.attribute("name").value();
      id = j.attribute("name").value();
      deployment = new Deployment(j.single("Deployment"));
   }

}
