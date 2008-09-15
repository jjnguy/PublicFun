package edu.iastate.cs309.plugins;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.iastate.cs309.plugins.exceptions.PluginVersionCompatException;
import edu.iastate.cs309.util.Util;

/**
 * Class parses the plugin XML representation and creates a PluginRepresentation
 * which is them used by PluginLoader
 * 
 * @author kc0dhb
 */
public class PluginParser
{
	/**
	 * Parses the XML plugin file
	 * 
	 * @param xml
	 * @return null if the XML file is invalid or a PluginRepresentation of the
	 *         XML if the file is valid
	 */
	public static PluginRepresentation parse(File xml)
	{
		DocumentBuilder builder;
		Document document;
		PluginRepresentation plugin = null;

		try
		{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			builder.setEntityResolver(new EntityResolver()
			{
				public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException, java.io.IOException
				{
					// this deactivates the  DTD
					return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
				}
			});

			document = builder.parse(xml);

			Element root = document.getDocumentElement();
			if (root.getTagName().equals("uebertorrent-plugin"))
				plugin = parseUEbertorrentPlugin(root, xml);
			else
				plugin = null;
		}
		catch (Exception e)
		{
			//TODO [kyle] pass up an exception that the GUI can read and take appropriate action with
			if (Util.DEBUG)
				e.printStackTrace();
			plugin = null;
		}

		return plugin;
	}

	/**
	 * Parse the Übertorrent structure.
	 * 
	 * @param pl
	 *            the root element
	 * @param xml
	 *            the original xml file to add to the plugin representation
	 * @return a PLuginRepresentation or fails to return
	 * @throws PluginVersionCompatException
	 */
	public static PluginRepresentation parseUEbertorrentPlugin(Element pl, File xml) throws PluginVersionCompatException
	{

		//Base elements that can have only 1 and that have no children
		String name = getChildElementValue(pl, "name");
		Double version = null;
		try
		{
			version = Double.parseDouble(getChildElementValue(pl, "version"));
		}
		catch (NumberFormatException e)
		{
			if (Util.DEBUG)
				e.printStackTrace();
			throw new PluginVersionCompatException("Unable to parse version information from config file.");
		}
		PluginRepresentation plugin = new PluginRepresentation(name, version);
		plugin.setOrginalXML(xml);

		//files
		List<Element> files = getChildElements(pl, "file");
		for (Element elm : files)
			parseFile(elm, plugin);

		// prereq
		List<Element> prereqs = getChildElements(pl, "prereq");
		for (Element elm : prereqs)
			plugin.addPrereq(parsePrereq(elm));

		// prereq-other
		List<Element> prereqOther = getChildElements(pl, "prereq-other");
		for (Element elm : prereqOther)
		{
			String prereqName = getChildElementValue(elm, "name");
			File path = new File(getChildElementValue(elm, "path"));

			plugin.addPrereqOther(new PrerequisiteOther(prereqName, path));

		}

		// Elements with subs

		// server-compat
		List<Element> server = getChildElements(pl, "server-compat");
		for (Element elm : server)
		{
			Version sCompat = parseVersion(getChildElement(elm, "version"));
			plugin.setServerCompat(sCompat);
		}

		// client-compat
		List<Element> client = getChildElements(pl, "client-compat");
		for (Element elm : client)
		{
			Version cCompat = parseVersion(getChildElement(elm, "version"));
			plugin.setClientCompat(cCompat);
		}

		return plugin;
	}

	/**
	 * Parses the file elements
	 * 
	 * @param fileElm
	 * @param p
	 */
	private static void parseFile(Element fileElm, PluginRepresentation p)
	{
		p.addFileLocal(new File(p.getOrginalXML().getParent() + File.separator + getChildElementValue(fileElm, "local")));
		p.addFileInstall(getChildElementValue(fileElm, "install"));
	}

	/**
	 * Parse the elements called version
	 * 
	 * @param versionElm
	 * @return a version object
	 */
	private static Version parseVersion(Element versionElm)
	{
		String temp;
		Version retVal = null;

		Double start = null;
		Double end = null;

		temp = getChildElementValue(versionElm, "start");
		if (temp != null)
			start = Double.parseDouble(temp);

		temp = getChildElementValue(versionElm, "end");
		if (temp != null)
			end = Double.parseDouble(temp);

		if ((start != null) && (end != null))
			retVal = new Version(start, end);
		else
			//Will be using exact versions
			retVal = new Version();

		List<Element> elms = getChildElements(versionElm, "exact");
		for (Element elm : elms)
			retVal.addVersion(Double.parseDouble(getElementValue(elm)));
		return retVal;
	}

	/**
	 * Parses the prerequisites
	 * 
	 * @param prereqElm
	 * @return the Prerequisite
	 */
	private static Prerequisite parsePrereq(Element prereqElm)
	{
		String plugin = getChildElementValue(prereqElm, "plugin");
		Version version = parseVersion(getChildElement(prereqElm, "version"));
		return new Prerequisite(plugin, version);
	}

	/**
	 * Gets the string value of the child element.
	 * 
	 * @author Tony Ross
	 * @param parent
	 * @param name
	 * @return
	 */
	private static String getChildElementValue(Element parent, String name)
	{
		Element elm = getChildElement(parent, name);
		return getElementValue(elm);
	}

	/**
	 * Gets the String value of the element
	 * 
	 * @author Tony Ross
	 * @param elm
	 * @return
	 */
	private static String getElementValue(Element elm)
	{
		if (elm != null)
		{
			String value = "";
			NodeList children = elm.getChildNodes();
			for (int index = 0; index < children.getLength(); index++)
			{
				Node child = children.item(index);
				if (child.getNodeType() == Node.TEXT_NODE)
					value += child.getNodeValue();
			}
			return value;
		}
		else
			return null;
	}

	/**
	 * Return the first element whose local name matches the provided name
	 * within the specified parent.
	 * 
	 * @author Tony Ross
	 * @param parent
	 *            The element to search within
	 * @param name
	 *            The element name to match
	 * @return The first matching element instance or null if note is found
	 */
	private static Element getChildElement(Element parent, String name)
	{
		NodeList children = parent.getChildNodes();
		for (int index = 0; index < children.getLength(); index++)
		{
			Node child = children.item(index);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				if (child.getNodeName().equals(name))
					return (Element) child;
		}
		return null;
	}

	/**
	 * Retrieve a list of all elements whose local name matches the provided
	 * name within the specified parent.
	 * 
	 * @author Tony Ross
	 * @param parent
	 *            The element to search within
	 * @param name
	 *            The element name to match
	 * @return A list containing all matching element instances
	 */
	private static List<Element> getChildElements(Element parent, String name)
	{
		LinkedList<Element> childElements = new LinkedList<Element>();
		NodeList children = parent.getChildNodes();
		for (int index = 0; index < children.getLength(); index++)
		{
			Node child = children.item(index);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				if (name.equals("*") || child.getNodeName().equals(name))
					childElements.add((Element) child);
		}
		return childElements;
	}
}
