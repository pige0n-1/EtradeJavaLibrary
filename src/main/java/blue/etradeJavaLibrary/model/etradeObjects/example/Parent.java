package blue.etradeJavaLibrary.model.etradeObjects.example;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDefinedObject;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class Parent implements XMLBuildable {

    public String exampleField1;
    public Child exampleField2;
    public ArrayList<OtherChild> exampleField3;

    public Parent(String e1, Child e2, ArrayList<OtherChild> e3) {
        this.exampleField1 = e1;
        this.exampleField2 = e2;
        this.exampleField3 = e3;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("exampleField1", exampleField1)
                .addXMLObjectField("exampleField2", exampleField2)
                .addArrayListField("exampleField3", exampleField3);
    }

    @Override
    public String getXMLClassName() {
        return "Parent";
    }

    public static void main(String[] args) throws ParserConfigurationException {
        var child = new Child(false);

        var otherChildren = new ArrayList<OtherChild>();
        otherChildren.add(new OtherChild());
        otherChildren.add(new OtherChild());
        otherChildren.add(new OtherChild());

        var parent = new Parent("hahaha", child, otherChildren);
        System.out.println(XMLDefinedObject.toIndentedString(parent.buildXMLFromDataFields()));
    }
}
