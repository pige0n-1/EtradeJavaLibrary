package blue.etradeJavaLibrary.model.etradeObjects.example;

import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;

import java.time.LocalDateTime;

public class OtherChild
        implements XMLBuildable {

    public LocalDateTime otherChildField1;
    public Integer otherChildField2;

    public OtherChild() {
        otherChildField1 = LocalDateTime.now();
        otherChildField2 = java.util.random.RandomGenerator.getDefault().nextInt();
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("otherChildField1", otherChildField1)
                .addStringField("otherChildField2", otherChildField2);
    }

    @Override
    public String getXMLClassName() {
        return "OtherChild";
    }
}
