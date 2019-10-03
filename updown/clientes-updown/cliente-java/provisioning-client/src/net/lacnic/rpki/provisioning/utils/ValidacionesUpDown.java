package net.lacnic.rpki.provisioning.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class ValidacionesUpDown {
	private static final String SCHEMA_LOCATION = Utils.getResources() + "provisioning.rnc";
	private static final String SCHEMA_LOCATION2 = Utils.getResources() + "provisioning2.rnc";

	public static boolean validateAgainstRelaxNg(String xml) throws IOException, SAXException {
		try {
			System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");

			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);

			File schemaLocation = new File(SCHEMA_LOCATION);
			Schema schema = factory.newSchema(schemaLocation);

			Validator validator = schema.newValidator();

			StreamSource source = new StreamSource(new StringReader(xml));

			validator.validate(source);

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean validateAgainstRelaxNg2(String xml) throws IOException, SAXException {
		try {
			System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory");

			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);

			File schemaLocation = new File(SCHEMA_LOCATION2);
			Schema schema = factory.newSchema(schemaLocation);

			Validator validator = schema.newValidator();

			StreamSource source = new StreamSource(new StringReader(xml));

			validator.validate(source);

			return true;

		} catch (Exception e) {
			return false;
		}
	}
}
