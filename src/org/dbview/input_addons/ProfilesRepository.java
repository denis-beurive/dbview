/*
	DbView - Graph Visualization
    Copyright (C) 2012  Denis BEURIVE

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.dbview.input_addons;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import java.net.URISyntaxException;
import org.jdom.input.SAXBuilder;
import java.io.File;
import org.dbview.conf.*;
import org.dbview.utils.*;
import java.net.URL;
import java.io.FileWriter;
import org.jdom.xpath.XPath;
import org.jdom.JDOMException;
import org.jdom.Attribute;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This class implements the profile's repository.</p>
 * <p>A profile is a set of value used by input add-ons to connect to their associated data source (Mysql...).</p>
 * @author Denis BEURIVE
 */
public class ProfilesRepository
{
    /**
     * XML representation of the list of profiles.
     */
    private static Document __profiles = null;

    /**
     * File that contains the XML description of the list of profiles.
     */
    private static File __repository   = null;

    /**
     * Initialize the repository.
     * @throws Exception
     */
    public static void init() throws Exception
    {
        if (null != ProfilesRepository.__profiles) { return; }
        // { throw new Exception("Unexpected error : The profile repository has already been initialized. It should not be initialized twice."); }

        // Build the path to the repository.
        ProfilesRepository.__initFile();

        // Shall we create an empty repository?
        if (! ProfilesRepository.__repository.exists())
        {
            Element root = new Element("profiles");
            root.setAttribute("created", org.dbview.utils.Date.now());
            Document doc = new Document(root);
            ProfilesRepository.__setUpdateDateTimeToNow(doc);
            ProfilesRepository.__write(doc);
        }

        // Load the repository.
        if (! ProfilesRepository.__repository .isFile())  { throw new Exception("Unexpected error : The entry that represents the profiles' repository \"" + ProfilesRepository.__repository.getPath() + "\" is not a regular file!"); }
        if (! ProfilesRepository.__repository .canRead()) { throw new Exception("Unexpected error : The entry that represents the profiles' repository \"" + ProfilesRepository.__repository.getPath() + "\" is not readable!"); }
        SAXBuilder builder = new SAXBuilder();
        ProfilesRepository.__profiles = builder.build(ProfilesRepository.__repository);
    }

    /**
     * Add a new profile to the repository.
     * @param in_profile Profile to add.
     * @throws ProfilesRepositoryException
     * @throws Exception
     */
    public static void add(AbstractConfiguration in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        ProfilesRepository.__add(in_profile.toXml());
    }

    /**
     * Given a profile, this method removes the associated profile from the repository.
     * @param in_profile Profile to remove.
     * @throws ProfilesRepositoryException
     * @throws JDOMException
     * @throws Exception
     */
    public static void remove(AbstractConfiguration in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        ProfilesRepository.__remove(in_profile.toXml());
    }

    /**
     * Given a profit's name, this method removes the associated profile from the repository.
     * @param in_name The name of the profile to remove.
     * @throws ProfilesRepositoryException
     * @throws JDOMException
     * @throws Exception
     */
    public static void remove(String in_name) throws ProfilesRepositoryException, JDOMException, Exception
    {
        if (null == ProfilesRepository.__profiles) { ProfilesRepository.init(); }
        Element conf = ProfilesRepository.__getProfileByName(in_name);
        if (null == conf) { throw new ProfilesRepositoryException("The name \"" + in_name + "\" is not associated to any profile."); }
        if (! ProfilesRepository.__profiles.getRootElement().removeContent(conf))
        { throw new ProfilesRepositoryException("Could not remove profile \"" + in_name + "\"."); }
        // Write to disk.
        ProfilesRepository.__setUpdateDateTimeToNow(null);
        ProfilesRepository.__write(null);
    }

    /**
     * Update a given profile.
     * @remark The removal uses the name of the profile
     * @param in_profile New profile.
     * @throws ProfilesRepositoryException
     * @throws JDOMException
     * @throws Exception
     */
    public static void update(AbstractConfiguration in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        ProfilesRepository.__update(in_profile.toXml());
    }

    /**
     * Return a literal representation of the profiles' repository.
     * @return Return a literal representation of the profiles' repository.
     */
    public static String print() throws Exception
    {
        if (null == ProfilesRepository.__profiles) { ProfilesRepository.init(); }
        return org.dbview.utils.Jdom.print(ProfilesRepository.__profiles);
    }

    /**
     * Return an instance of a given profile's configuration's holder.
     * @remark The returned instance is initialized using the XML representation extracted from the repository.
     * @param in_name Name of the profile.
     * @return If the requested profile exists, then the method returns an instance of the configuration's holder.
     *         Otherwise, it returns the value null.
     */
    public static AbstractConfiguration getProfileInstance(String in_name) throws ConfigurationException, JDOMException, Exception
    {
        if (null == ProfilesRepository.__profiles) { ProfilesRepository.init(); }
        Element profile = ProfilesRepository.__getProfileByName(in_name);
        if (null == profile) { return null; }
        String target = ProfilesRepository.__getProfileTarget(profile);
        InputCatalog a = new InputCatalog();
        AbstractConfiguration conf = (AbstractConfiguration)a.getAdaptor(target, "Configuration");
        conf.fromXml(profile, null, null);
        return conf;
    }

    /**
     * This method returns the list of all profiles' names stored in the profiles' repository.
     * @return The method returns the list of all profiles stored in the profiles' repository.
     * @throws JDOMException
     */
    public static ArrayList<String> getProfilesNames() throws JDOMException
    {
        ArrayList<String> profiles = new ArrayList<String>();
        XPath xpa = XPath.newInstance("configuration");
        @SuppressWarnings("unchecked")
        List<Element> list = (List<Element>)xpa.selectNodes(ProfilesRepository.__profiles.getRootElement());

        for (int i=0; i<list.size(); i++)
        {
            Element e = list.get(i);
            profiles.add(e.getAttributeValue("name"));
        }

        return profiles;
    }

    /**
     * This method tries to locate a given profile given its name.
     * @param in_name Name of the profile to locate.
     * @return If the profile, identified by the given name exists, then it is returned.
     *         Otherwise, the method returns the value null.
     * @throws JDOMException
     */
    private static Element __getProfileByName(String in_name) throws JDOMException
    {
        XPath xpa = XPath.newInstance("configuration[@name='" + in_name + "']");
        return (Element)xpa.selectSingleNode(ProfilesRepository.__profiles.getRootElement());
    }

    /**
     * Given a profile, this method returns its name.
     * @param in_profile Profile.
     * @return The method returns the profile's name.
     * @throws Exception
     */
    private static String __getProfileName(Element in_profile) throws Exception
    {
        Attribute attr = in_profile.getAttribute("name");
        if (null == attr) { throw new Exception("Unexpected error : Found a profile with no name!"); }
        return attr.getValue();
    }

    /**
     * Given a profile, this method returns the name of the associated input add-on.
     * @param in_profile Profile.
     * @return The method returns the name of the associated input add-on.
     * @throws Exception
     */
    private static String __getProfileTarget(Element in_profile) throws Exception
    {
        Attribute attr = in_profile.getAttribute("target");
        if (null == attr) { throw new Exception("Unexpected error : Found a profile with no target name!"); }
        return attr.getValue();
    }

    /**
     * This method creates an instance of class "File" that points to the profiles' repository.
     * @throws URISyntaxException
     * @throws Exception
     */
    private static void __initFile() throws URISyntaxException, Exception
    {
        URL resources_url = JavaVm.locateResource(Conf.get("repositoriesDirName"));
        if (null == resources_url) { throw new Exception("Unexpected error : Can not locate the directory where the profile repository is kept (\"" + Conf.get("repositoriesDirName") + "\")."); }
        ProfilesRepository.__repository = new File(resources_url.toURI().getPath(), Conf.get("profilesRepository"));
    }

    /**
     * This method writes the profiles to the file that contains the repository.
     * @param in_doc XML document to write (can be null!).
     * @warning The argument <i>in_doc</i> can be null.
     * @throws URISyntaxException
     * @throws Exception
     */
    private static void __write(Document in_doc) throws URISyntaxException, Exception
    {
        if (null == ProfilesRepository.__repository) { ProfilesRepository.__initFile(); }
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(org.jdom.output.Format.getPrettyFormat());
        FileWriter fw = new FileWriter(ProfilesRepository.__repository);
        in_doc = null == in_doc ? ProfilesRepository.__profiles : in_doc;
        fw.write(outputter.outputString(in_doc));
        fw.close();
    }

    /**
     * This method sets the value of the (XML) attribute "updated" to the current date ant time.
     * @param in_doc XML document to update (can be null!).
     * @warning The argument <i>in_doc</i> can be null.
     */
    private static void __setUpdateDateTimeToNow(Document in_doc)
    {
        in_doc = null == in_doc ? ProfilesRepository.__profiles : in_doc;
        in_doc.getRootElement().setAttribute("updated", org.dbview.utils.Date.now());
    }

    /**
     * Add a new profile to the repository.
     * @param in_profile Profile to add.
     * @throws ProfilesRepositoryException
     * @throws Exception
     */
    private static void __add(Element in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        if (null == ProfilesRepository.__profiles) { ProfilesRepository.init(); }
        String profile_name = ProfilesRepository.__getProfileName(in_profile);

        // Make sure that the profile does not already exist.
        Element conf = ProfilesRepository.__getProfileByName(profile_name);
        if (null != conf) { throw new ProfilesRepositoryException("The name \"" + profile_name + "\" is already associated to a profile."); }

        // Add the new profile.
        ProfilesRepository.__profiles.getRootElement().addContent(in_profile);

        // Write to disk.
        ProfilesRepository.__setUpdateDateTimeToNow(null);
        ProfilesRepository.__write(null);
    }

    /**
     * Update a given profile.
     * @remark The removal uses the name of the profile
     * @param in_profile New profile.
     * @throws ProfilesRepositoryException
     * @throws JDOMException
     * @throws Exception
     */
    private static void __update(Element in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        // Delete the profile (the removal uses the name of the profile).
        ProfilesRepository.__remove(in_profile);

        // Add the profile.
        ProfilesRepository.__add(in_profile);
    }

    /**
     * Given a profile, this method removes the profile from the repository.
     * @param in_profile The profile to remove.
     * @throws ProfilesRepositoryException
     * @throws JDOMException
     * @throws Exception
     */
    private static void __remove(Element in_profile) throws ProfilesRepositoryException, JDOMException, Exception
    {
        if (null == ProfilesRepository.__profiles) { ProfilesRepository.init(); }
        String profile_name = ProfilesRepository.__getProfileName(in_profile);
        ProfilesRepository.remove(profile_name);
    }
}
