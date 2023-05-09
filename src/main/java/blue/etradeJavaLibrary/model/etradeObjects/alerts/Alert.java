package blue.etradeJavaLibrary.model.etradeObjects.alerts;

import blue.etradeJavaLibrary.core.network.oauth.responses.ObjectMismatchException;
import blue.etradeJavaLibrary.model.etradeObjects.EtradeObject;
import blue.etradeJavaLibrary.model.etradeObjects.XMLBuildable;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDataFields;
import blue.etradeJavaLibrary.model.etradeObjects.XMLDefinedObject;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.time.LocalDateTime;

public class Alert extends EtradeObject<Alert>
        implements XMLBuildable {

    // Instance data fields
    public Long id;
    public LocalDateTime createTime;
    public String subject;
    public String status;

    @Override
    public Alert configureFromXMLDocument(Document xmlDocument) throws ObjectMismatchException {
        this.xmlDocument = xmlDocument;

        id = extractLong("id");
        createTime = extractTime("createTime");
        subject = extract("subject");
        status = extract("status");

        return this;
    }

    @Override
    public XMLDataFields getDataFields() {
        return new XMLDataFields()
                .addStringField("id", id)
                .addStringField("createTime", createTime)
                .addStringField("subject", subject)
                .addStringField("status", status);
    }

    @Override
    public String getXMLClassName() {
        return "Alert";
    }

    public static void main(String[] args) throws ParserConfigurationException {
        Alert example = new Alert();
        example.id = 54321l;
        example.createTime = LocalDateTime.now();
        example.subject = "example";
        example.status = "example";

        System.out.println(XMLDefinedObject.toString(example.buildXMLFromDataFields()));
    }
}
