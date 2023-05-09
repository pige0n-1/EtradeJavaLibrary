package blue.etradeJavaLibrary.model.etradeObjects.example;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;

public class Child
        implements XMLBuildable {

    public Boolean childField1;

    public Child(Boolean e1) {
        this.childField1 = e1;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields().addStringField("childField1", childField1);
    }

    @Override
    public String getXMLClassName() {
        return "Child";
    }
}
